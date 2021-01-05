package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import players.rhea.evo.Individual;
import utils.ElapsedCpuTimer;
import utils.Types;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static players.groupB.helpers.ActionsHelper.*;
import static players.rhea.utils.Constants.*;
import static players.rhea.utils.Utilities.*;

public class EvoOperations implements EvoPlayable {

    private ParamsHelper paramsHelper;
    private Random randomGenerator;
    private ElapsedCpuTimer elapsedTimer;
    private EMCTSsol rootStateSolution;
    private GameState gameState;
    private Mutate mutationClass;
    private MctsPlayable mctsOperations;

    public EvoOperations(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }
    @Override
    public Solution mutate(Solution ch){
        EMCTSsol parent = (EMCTSsol)ch;
        EMCTSsol child = initializeChildFromParent(parent);
        int indexOfTheChild = parent.getChildren().indexOf(child);
        GameState gameState = this.gameState.copy();
        int indexOfGenomeToChange = this.randomGenerator.nextInt(parent.getPopulation().get_length());
        int depth = 0;
        while (!this.mctsOperations.finishRollout(gameState,depth)) {
            int action = child.getPopulation().get_action(depth);
            if (depth >= indexOfGenomeToChange) {
                ArrayList<Types.ACTIONS> safeRandomActions = getSafeRandomActions(gameState, this.randomGenerator);
                if (depth == indexOfGenomeToChange) {
                    //mutate one action to random action
                    int mutatedAction = safeRandomAction(gameState,this.randomGenerator);
                    boolean flag = false;
                    while (!flag) {
                        if (action != mutatedAction) {
                            action = mutatedAction;
                            flag = true;
                        }
                        else{
                            mutatedAction = safeRandomAction(gameState,this.randomGenerator);
                        }
                    }
                    child.getPopulation().set_action(indexOfGenomeToChange, action);
                }
                else{
                    //Greedily find the best action to repair
                    if (!safeRandomActions.contains(getAvailableActionsInArrayList().get(action))){
                        double maxQ = Double.NEGATIVE_INFINITY;
                        Types.ACTIONS actionToRepair = null;
                        GameState bestGameState = null;
                        for (Types.ACTIONS act : safeRandomActions) {
                            GameState gsCopy = gameState.copy();
                            this.mctsOperations.roll(gsCopy, act);
                            double valState = paramsHelper.getStateHeuristic().evaluateState(gsCopy);
                            double Q = Utils.noise(valState, Const.epsilon, this.randomGenerator.nextDouble());
                            if (Q > maxQ) {
                                maxQ = Q;
                                actionToRepair = act;
                                bestGameState = gsCopy;
                            }
                        }
                        gameState = bestGameState;
                        action = getAvailableActionsInArrayList().indexOf(actionToRepair);
                        child.getPopulation().set_action(depth, action);
                    }
                }
            }
            this.mctsOperations.roll(gameState, getAvailableActionsInArrayList().get(action));
            depth++;
        }
        return child;
    }



    @Override
    public Solution shift_buffer(Solution solution) {
        EMCTSsol sol = (EMCTSsol) solution;
        for (int j = 1; j < sol.getPopulation().get_length(); j++) {
            sol.getPopulation().set_action(j - 1, sol.getPopulation().get_action(j));
        }
        sol.getPopulation().set_action(sol.getPopulation().get_length()-1, this.randomGenerator.nextInt(getAvailableActionsInArrayList().size()));
        return sol;
    }

    private EMCTSsol initializeChildFromParent(EMCTSsol parent) {
        EMCTSsol child = new EMCTSsol();
        child.setPopulation(parent.getPopulation().copy());
        child.setParent(parent);
        parent.getChildren().add(child);
        return child;
    }

    private int evaluateRollout(double[] values, GameState copy, int length, Solution sol) {

        EMCTSsol child = (EMCTSsol)sol;
        // Keep track of where the rollout stopped (in case of early terminal state).
        int lastIdx = 0;

        // Roll through the actions
        for (int i = 0; i < length; i++) {
            // Stop if the state reached is terminal
            if (!copy.isTerminal()) {
                if (child.getPopulation() != null) {
                    // Advance the state with the action in the individual
                    this.mctsOperations.roll(copy, getAvailableActionsInArrayList().get(child.getPopulation().get_action(i)));

                }

                // Signal we used 1 FM call
                this.paramsHelper.getFmBudget().use();

                // Save the value of this state in the values array and update lastIdx reached.
                if ((this.paramsHelper.getIntValue("evaluate_act") == EVALUATE_ACT_DELTA || this.paramsHelper.getIntValue("evaluate_act") == EVALUATE_ACT_LAST)
                        && (i != length - 1)) {  // This only needs last state evaluated, speed up execution
                    values[i + 1] = 0;
                } else {  // In all other cases we need all intermediate state values.
                    values[i + 1] = this.paramsHelper.getStateHeuristic().evaluateState(copy);
                }
                lastIdx = i;
            } else {
                break;
            }
        }
        if (lastIdx < length - 1) {
            // Broke out of the loop early, end of game
            values[lastIdx + 1] = this.paramsHelper.getStateHeuristic().evaluateState(copy);
        }
        lastIdx++;

        return lastIdx;
    }

    @Override
    public double evaluate(Solution sol) {

        EMCTSsol child = (EMCTSsol)sol;

        double[] values = new double[child.getPopulation().get_length() + 1];
        GameState stateObsCopy = this.gameState.copy();
        if (this.paramsHelper.getIntValue("evaluate_act") == EVALUATE_ACT_LAST) {  // This doesn't need first state value
            values[0] = 0;
        } else {
            values[0] = this.paramsHelper.getStateHeuristic().evaluateState(stateObsCopy);  // Evaluate current state
        }

        // Evaluate subsequent states obtained by rolling through the actions
        int lastIdx = evaluateRollout(values, stateObsCopy, child.getPopulation().get_length(), child);

        if (lastIdx < values.length - 1) {
            // We stopped early, trim the values array to remove trailing 0s
            values = Arrays.copyOfRange(values, 0, lastIdx + 1);
        }

        // Get the value of the rollout according to the evaluation method.
        double state_value = getRolloutValue(values);

        // Update value according to update rule
        double update_value;

        switch(this.paramsHelper.getIntValue("evaluate_update")) {
            case EVALUATE_UPDATE_DELTA: update_value = child.getPopulation().get_value() - state_value; break;
            case EVALUATE_UPDATE_AVERAGE: update_value = (child.getPopulation().get_value() + state_value) / 2; break;
            case EVALUATE_UPDATE_MIN: update_value = Math.min(child.getPopulation().get_value(), state_value); break;
            case EVALUATE_UPDATE_MAX: update_value = Math.max(child.getPopulation().get_value(), state_value); break;
            default:
            case EVALUATE_UPDATE_RAW: update_value = state_value;
        }

        // Set the individual's value and return it
        child.getPopulation().set_value(update_value);
        return update_value;
    }
    private double getRolloutValue(double[] values) {
        double state_value;
        int length = values.length;
        switch(this.paramsHelper.getIntValue("evaluate_act")) {
            case EVALUATE_ACT_DELTA:
                state_value = values[length - 1] - values[0];
                break;
            case EVALUATE_ACT_AVG:
                state_value = get_avg(values);
                break;
            case EVALUATE_ACT_MIN:
                state_value = get_min(values);
                break;
            case EVALUATE_ACT_MAX:
                state_value = get_max(values);
                break;
            case EVALUATE_ACT_DISCOUNT:
                state_value = 0;
                for (int i = 0; i < length; i++) {
                    state_value += Math.pow(0.99, i) * values[i];
                }
                break;
            default:
            case EVALUATE_ACT_LAST:
                state_value = values[length - 1];
                break;
        }

        return state_value;
    }

    //Helpers
    @Override
    public void setMcts(MctsPlayable mctsPlayable) {
        this.mctsOperations = mctsPlayable;
    }

    @Override
    public void setParamsHelper(GameState gameState, ParameterSet params) {
        this.gameState = gameState;
        if (this.paramsHelper == null){
            this.paramsHelper = new ParamsHelper(gameState, params, this.randomGenerator);
            this.paramsHelper.setUpSuitableHeuristic(this.paramsHelper.getIntValue("heuristic_method"));
            this.mutationClass = new Mutate(this.paramsHelper, this.randomGenerator);
            this.paramsHelper.initializeBudgets();
        }
    }

    @Override
    public ParamsHelper getParamsHelper() {
        return this.paramsHelper;
    }



}
