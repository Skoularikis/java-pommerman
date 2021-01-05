package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.interfaces.GamePlayable;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.mcts.MCTSParams;
import players.mcts.SingleTreeNode;
import players.optimisers.ParameterSet;
import players.rhea.evo.Individual;
import utils.Types;
import utils.Utils;

import java.util.Random;

import static players.groupB.helpers.ActionsHelper.*;
import static players.groupB.helpers.ActionsHelper.getAvailableActionsInArrayList;

public class Emcts implements GamePlayable {
    //paramsHelper
    private ParamsHelper paramsHelper;
    // For MCTS part
    private MctsPlayable mctsOperations;
    // For Evo part
    private EvoPlayable evoOperations;
    // For params
    private EMCTSParams params;
    // Random Generator
    private Random randomGenerator;
    // GameState
    public GameState rootGameState;
    // EMCTSsol
    public EMCTSsol currentRootStateSolution;

    public Emcts(ParameterSet params, Random randomGenerator) {
        //Set up EMCTS parameters
        this.params = (EMCTSParams)params;
        this.randomGenerator = randomGenerator;
        this.mctsOperations = new MctsOperations(this.randomGenerator);
        this.evoOperations = new EvoOperations(this.randomGenerator);
        this.evoOperations.setMcts(this.mctsOperations);
        this.mctsOperations.setEvoPlayable(this.evoOperations);
    }

    @Override
    public void setRootState(GameState gameState, Solution currentRootState) {
        this.rootGameState = gameState;
        this.mctsOperations.setParamsHelper(gameState,this.params);
        this.evoOperations.setParamsHelper(gameState,this.params);
        this.paramsHelper = evoOperations.getParamsHelper();
        if (currentRootState == null) { // Root of the tree
            this.currentRootStateSolution = createRootStateSolutionWithGreedyAction(this.paramsHelper);
        }
        else { // Leaf node
            EMCTSsol sol = (EMCTSsol)currentRootState;
            sol.getPopulation().set_value(0);
            this.currentRootStateSolution = new EMCTSsol();
            this.currentRootStateSolution.setPopulation(sol.getPopulation());
            this.currentRootStateSolution = (EMCTSsol) this.evoOperations.shift_buffer(this.currentRootStateSolution);
//            System.out.println(this.currentRootStateSolution);
        }
    }

    @Override
    public void getActionToExecute() {
        boolean stop = false;
        int numIters = 0;
        while (!stop) {
            EMCTSsol curSol = this.currentRootStateSolution.copy();
            EMCTSsol selected = (EMCTSsol)mctsOperations.treePolicy(curSol);
            double evalValue = evoOperations.evaluate(selected);
            this.mctsOperations.backUp(selected, evalValue);
            this.currentRootStateSolution.increaseVisitedCount();
            if (numIters == 200) {
                stop = true;
            }
            else{
                numIters++;
            }
        }
    }

    @Override
    public Solution getBestSolution() {
        int selected = -1;
        double bestValue = -Double.MAX_VALUE;
        boolean allEqual = true;
        double first = -1;

        for (int i=0; i<this.currentRootStateSolution.getChildren().size(); i++) {
            EMCTSsol child = this.currentRootStateSolution.getChildren().get(i);
            if(child != null)
            {
                if(first == -1)
                    first = child.getVisited_count();
                else if(first != child.getVisited_count())
                {
                    allEqual = false;
                }

                double childValue = child.getVisited_count();
                childValue = Utils.noise(childValue, Const.epsilon, this.randomGenerator.nextDouble());     //break ties randomly
                if (childValue > bestValue) {
                    bestValue = childValue;
                    selected = i;
                }
            }
        }
        if (selected == -1)
        {
            selected = 0;
        }else if(allEqual)
        {
            //If all are equal, we opt to choose for the one with the best Q.
            selected = bestAction();
        }
        return this.currentRootStateSolution.getChildren().get(selected);
//        return getAvailableActionsInArrayList().get(actionToExecute);
    }

    private int bestAction() {
        int selected = -1;
        double bestValue = -Double.MAX_VALUE;

        for (int i=0; i<this.currentRootStateSolution.getChildren().size(); i++) {
            EMCTSsol child = this.currentRootStateSolution.getChildren().get(i);
            if(child != null) {
                double childValue = child.getPopulation().get_value() / (child.getVisited_count() + Const.epsilon);
                childValue = Utils.noise(childValue, Const.epsilon, this.randomGenerator.nextDouble());     //break ties randomly
                if (childValue > bestValue) {
                    bestValue = childValue;
                    selected = i;
                }
            }
        }

        if (selected == -1)
        {
            System.out.println("Unexpected selection!");
            selected = 0;
        }

        return selected;
    }

    private EMCTSsol createRootStateSolutionWithGreedyAction(ParamsHelper paramsHelper) {
        EMCTSsol rootSolution = new EMCTSsol();
        rootSolution.setPopulation(
                new Individual(
                        paramsHelper.getIntValue("individual_length"),
                        randomGenerator,
                        getAvailableActions().size()));
        int depth = 0;
        GameState gameState = this.rootGameState.copy();
        while (!this.mctsOperations.finishRollout(this.rootGameState, depth)) {
            double maxQ = Double.NEGATIVE_INFINITY;
            Types.ACTIONS bestAction = null;
            GameState currentBestGameSate = null;
            for (Types.ACTIONS act : getSafeRandomActions(gameState,this.randomGenerator)) {
                GameState gsCopy = gameState.copy();
                this.mctsOperations.roll(gsCopy, act);
                double valState = paramsHelper.getStateHeuristic().evaluateState(gsCopy);
                double Q = Utils.noise(valState, Const.epsilon, this.randomGenerator.nextDouble());
                //System.out.println("Action:" + action + " score:" + Q);
                if (Q > maxQ) {
                    maxQ = Q;
                    bestAction = act;
                    currentBestGameSate = gsCopy;
                }
            }
            rootSolution.getPopulation().set_action(depth, getAvailableActionsInArrayList().indexOf(bestAction));
            depth++;
            gameState = currentBestGameSate;
        }
        return rootSolution;
    }
    
}

