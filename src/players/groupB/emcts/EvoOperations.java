package players.groupB.emcts;

import core.Game;
import core.GameState;
import gnu.trove.set.hash.TIntHashSet;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.heuristics.StateHeuristic;
import players.optimisers.ParameterSet;
import players.rhea.evo.Individual;
import players.rhea.evo.Mutation;
import players.rhea.utils.FMBudget;
import utils.ElapsedCpuTimer;
import utils.Types;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static players.groupB.helpers.ActionsHelper.*;
import static players.groupB.helpers.ActionsHelper.getSafeRandomActions;
import static players.rhea.utils.Constants.*;
import static players.rhea.utils.Constants.EVALUATE_UPDATE_RAW;
import static players.rhea.utils.Utilities.*;

public class EvoOperations implements EvoPlayable {

    private ParamsHelper paramsHelper;
    private Random randomGenerator;
    private ElapsedCpuTimer elapsedTimer;
    private EMCTSsol rootStateSolution;
    private GameState gameState;
    private Mutate mutationClass;
    private MctsPlayable mctsOperations;
    private HashMap<Integer, Types.ACTIONS> action_mapping;
    private StateHeuristic stateHeuristic;
    private int evaluate_act = EVALUATE_ACT_LAST;
    private FMBudget fmBudget;


    public EvoOperations(Random randomGenerator, ElapsedCpuTimer elapsedTimer) {
        this.randomGenerator = randomGenerator;
        this.elapsedTimer = elapsedTimer;
    }

    @Override
    public void setParamsHelper(GameState gameState, ParameterSet params) {
        this.gameState = gameState;
        if (this.paramsHelper == null){
            this.paramsHelper = new ParamsHelper(gameState, params, this.randomGenerator);
            this.paramsHelper.setUpSuitableHeuristic(this.paramsHelper.getIntValue("heuristic_method"));
            this.mutationClass = new Mutate(this.paramsHelper, this.randomGenerator);
            this.fmBudget = new FMBudget(this.paramsHelper.getIntValue("fm_budget") );
        }
    }

    @Override
    public ParamsHelper getParamsHelper() {
        return this.paramsHelper;
    }

    @Override
    public EMCTSsol createRootStateSolution(boolean isRootState) {
        rootStateSolution = new EMCTSsol();
        if (isRootState){
            rootStateSolution.setParent(null);
        }
        if (rootStateSolution.getPopulation() == null){
            rootStateSolution.setPopulation(
                    new Individual(
                            this.paramsHelper.getIntValue("individual_length"),
                            randomGenerator,
                            getAvailableActions().size())
            );
            if (this.paramsHelper.getIntValue("init_type") == Const.InitType.INIT_RANDOM) {
                if (isRootState){
                    fillIndividualWithRandomActions(rootStateSolution.getPopulation(),randomGenerator);
                }
                rootStateSolution.setChildren(new ArrayList<EMCTSsol>());
                for (int i=0; i < getAvailableActions().size(); i++) {
                    EMCTSsol child = new EMCTSsol();
                    child.setParent(rootStateSolution);
                    rootStateSolution.getChildren().add(child);
                }

            }
            return rootStateSolution;
        }
        return null;
    }



    @Override
    public Solution mutate(Solution ch){
        EMCTSsol parent = (EMCTSsol)ch;
        EMCTSsol child = initializeChildFromParent(parent);

        int idx = parent.getChildren().indexOf(child);
        GameState gameState = this.gameState.copy();
        int depth = 0;
        while (!this.mctsOperations.finishRollout(gameState,depth)) {
            int action = child.getPopulation().get_action(depth);
            if (depth >= idx) {
                ArrayList<Types.ACTIONS> safeRandomActions = getSafeRandomActions(gameState, this.randomGenerator);
                if (depth == idx) {
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
                    child.getPopulation().set_action(depth, action);
                }
                else{
                    //Greedily find the best action to repair
                    if (!safeRandomActions.contains(safeRandomActions.get(action))){
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

    private EMCTSsol initializeChildFromParent(EMCTSsol parent) {
        EMCTSsol child = new EMCTSsol();
        child.setPopulation(parent.getPopulation());
        child.setParent(parent);
        parent.getChildren().add(child);
        return child;
    }


//        Individual individual = new Individual(parent.getPopulation().get_length(), this.randomGenerator, getAvailableActionsInArrayList().size());
//        individual.set_action(i, parent.getPopulation().get_actions()[this.randomGenerator.nextInt(parent.getPopulation().get_length())]);
//
//
//        EMCTSsol child = new EMCTSsol();
//        for (int i =0; i < parent.getPopulation().get_length(); i++) {
//            if (this.paramsHelper.getIntValue("genetic_operator") == Const.GeneticOperators.MUTATION_ONLY) {
//                individual.set_action(i, parent.getPopulation().get_actions()[this.randomGenerator.nextInt(parent.getPopulation().get_length())]);
//
//            }
//            if (this.paramsHelper.getIntValue("genetic_operator") != Const.GeneticOperators.CROSSOVER_ONLY) {
//                mutationClass.findGenesToMutate();
//            }
//        }
//        child.setPopulation(individual);
//        return child;

    private int evaluateRollout(double[] values, GameState copy, int length, Solution sol,
                                Mutation mutation) {

        EMCTSsol child = (EMCTSsol)sol;
        // Keep track of where the rollout stopped (in case of early terminal state).
        int lastIdx = 0;

        // Retrieve the list of genes to mutate.
        TIntHashSet genesToMutate = null;
        if (mutation != null) {
            genesToMutate = mutation.getGenesToMutate();
        }

        // Roll through the actions
        for (int i = 0; i < length; i++) {
            // Stop if the state reached is terminal
            if (!copy.isTerminal()) {
                if (child.getPopulation() != null) {
                    // Mutate gene if needed to a new random value.
                   // if (genesToMutate != null && genesToMutate.contains(i)) {
                   //     mutation.mutateGeneToNewValue(child.getPopulation(), i);
                  //  }
                    // Advance the state with the action in the individual
                    //advanceState(copy, action_mapping.get(child.getPopulation().get_action(i)));

                } //else {  // No individual passed, doing random rollout
                  //  ArrayList<Types.ACTIONS> acts = Types.ACTIONS.all();
                  //  int bound = rootStateSolution;
                  //  Types.ACTIONS action = Types.ACTIONS.ACTION_STOP;
                  //  if (bound > 0) {
                  //      action = acts.get(random.nextInt(bound));
                  //  }
                  //  advanceState(copy, action);
                //}

                // Signal we used 1 FM call
                fmBudget.use();

                // Save the value of this state in the values array and update lastIdx reached.
                if ((this.paramsHelper.getIntValue("evaluate_act") == EVALUATE_ACT_DELTA || this.paramsHelper.getIntValue("evaluate_act") == EVALUATE_ACT_LAST)
                        && (i != length - 1)) {  // This only needs last state evaluated, speed up execution
                    values[i + 1] = 0;
                } else {  // In all other cases we need all intermediate state values.
                    values[i + 1] = evaluateState(copy);
                }
                lastIdx = i;
            } else {
                break;
            }
        }
        if (lastIdx < length - 1) {
            // Broke out of the loop early, end of game
            values[lastIdx + 1] = evaluateState(copy);
        }
        lastIdx++;

        return lastIdx;
    }
//    public void advanceState(GameState gs, Types.ACTIONS action) {
//        int nPlayers = 4;
//        Types.ACTIONS[] actionsAll = new Types.ACTIONS[nPlayers];
//
//        for (int i = 0; i < nPlayers; ++i) {
//            if (playerID == i) {
//                actionsAll[i] = action;
//            } else {
//                actionsAll[i] = opponentModel(gs);
//            }
//        }
//
//        gs.next(actionsAll);
//    }

    @Override
    public double evaluate(Solution sol, Mutate mutation, int evaluation_update) {

        EMCTSsol child = (EMCTSsol)sol;

        double[] values = new double[child.getPopulation().get_length() + 1];

        //EMCTSsol stateObsCopy = rootStateSolution.copy();

        GameState stateObsCopy = this.gameState.copy();

        //if (params.evaluate_act == EVALUATE_ACT_LAST) {  // This doesn't need first state value
        //    values[0] = 0;
        //} else {
        values[0] = evaluateState(stateObsCopy);  // Evaluate current state
        //}

        // Evaluate subsequent states obtained by rolling through the actions
        int lastIdx = evaluateRollout(values, stateObsCopy, child.getPopulation().get_length(), child, mutation);

        if (lastIdx < values.length - 1) {
            // We stopped early, trim the values array to remove trailing 0s
            values = Arrays.copyOfRange(values, 0, lastIdx + 1);
        }

        // Get the value of the rollout according to the evaluation method.
        double state_value = getRolloutValue(values);

        // We may need to do extra rollouts from the end of the state reached previously, if not terminal.
        //if (params.mc_rollouts && !stateObsCopy.isTerminal()) {
        //    state_value = MCrollouts(stateObsCopy, values);
        //}

        // Update value according to update rule
        double update_value;

        switch(evaluation_update) {
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
        switch(evaluate_act) {
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
    //public double evaluate(Solution rootSol, boolean useMutationClass){
    //    return 0.0;
    //}
    public double evaluateState(GameState a_gameState) {
        return this.stateHeuristic.evaluateState(a_gameState);
    }


    //Helpers
    @Override
    public void setMcts(MctsPlayable mctsPlayable) {
        this.mctsOperations = mctsPlayable;
    }


}
