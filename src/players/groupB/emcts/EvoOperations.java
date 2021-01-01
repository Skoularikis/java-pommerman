package players.groupB.emcts;

import core.Game;
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
import java.util.Random;

import static players.groupB.helpers.ActionsHelper.*;
import static players.groupB.helpers.ActionsHelper.getSafeRandomActions;

public class EvoOperations implements EvoPlayable {

    private ParamsHelper paramsHelper;
    private Random randomGenerator;
    private ElapsedCpuTimer elapsedTimer;
    private EMCTSsol rootStateSolution;
    private GameState gameState;
    private Mutate mutationClass;
    private MctsPlayable mctsOperations;

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

    @Override
    public double evaluate(Solution solution, boolean mutationClass) {
        return 0.0;
    }

    //Helpers
    @Override
    public void setMcts(MctsPlayable mctsPlayable) {
        this.mctsOperations = mctsPlayable;
    }


}
