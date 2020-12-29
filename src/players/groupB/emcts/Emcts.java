package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ObjectHelper;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.interfaces.GamePlayable;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSParams;
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


public class Emcts implements GamePlayable {
    //CPU Timer
    private ElapsedCpuTimer elapsedTimer;
    // For MCTS part
    private MctsPlayable mctsOperations;
    // For Evo part
    private EvoPlayable evoOperations;
    // For params
    private EMCTSParams params;
    // Random Generator
    private Random randomGenerator;
    // GameState
    public GameState gameState;
    // EMCTSsol
    public EMCTSsol currentRootStateSolution;

    public Emcts(ParameterSet params, Random randomGenerator) {
        //Set up EMCTS parameters
        this.params = (EMCTSParams)params;
        this.randomGenerator = randomGenerator;
        setUpElapsedCpuTimer(this.params);
        //Initialize MctsOperations
        this.mctsOperations = new MctsOperations(this.randomGenerator, this.elapsedTimer);
        //Initialize EvoOperations
        this.evoOperations = new EvoOperations(this.randomGenerator, this.elapsedTimer);
    }

    @Override
    public void setRootState(GameState gameState, Solution currentRootState) {
        this.gameState = gameState;
        this.mctsOperations.setParamsHelper(gameState, this.params);
        this.evoOperations.setParamsHelper(gameState, this.params);
        if (currentRootState == null) { // Root of the tree
            ParamsHelper paramsHelper = evoOperations.getParamsHelper();
            this.currentRootStateSolution = createRootStateSolutionWithGreedyAction(gameState, paramsHelper);
        }
        else { // Leaf node
            this.currentRootStateSolution = (EMCTSsol) currentRootState;
        }
    }

    @Override
    public void getActionToExecute(boolean isRootState) {

        boolean stop = false;
        EMCTSsol selectedSolution = null;
        int indx = 0;
        while (!stop) {
            EMCTSsol curSol = this.currentRootStateSolution.copy();
            //MutatedChildren here
            EMCTSsol child = curSol.getChildren().get(indx);
            if (child.getVisited_count() == 0) {
                evoOperations.evaluate(child, true);
            }
            else {
                child = (EMCTSsol)mctsOperations.treePolicy(curSol);
                indx = 0;
            }
            this.mctsOperations.backUp(child);
            indx++;


            stop = true;
        }
        this.currentRootStateSolution = selectedSolution;
    }


    private EMCTSsol createRootStateSolutionWithGreedyAction(GameState gs, ParamsHelper paramsHelper) {
        EMCTSsol rootSolution = new EMCTSsol();
        rootSolution.setPopulation(
                new Individual(
                        paramsHelper.getIntValue("individual_length"),
                        randomGenerator,
                        getAvailableActions().size()));
        int depth = 0;
        GameState gameState = gs.copy();
        while (!this.mctsOperations.finishRollout(gameState,depth)) {
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
                    currentBestGameSate = gs;
                }
            }
            rootSolution.getPopulation().set_action(depth, getAvailableActionsInArrayList().indexOf(bestAction));
            depth++;
            gameState = currentBestGameSate;
        }
        return rootSolution;
    }

    private void setUpElapsedCpuTimer(EMCTSParams params) {
        ElapsedCpuTimer elapsedTimer = null;
        if (ObjectHelper.getIntValue(params.getParameterValue("budget_type")) == Const.BudgetType.TIME_BUDGET) {
            elapsedTimer = new ElapsedCpuTimer();
            elapsedTimer.setMaxTimeMillis(ObjectHelper.getIntValue(params.getParameterValue("time_budget")));
        }
        this.elapsedTimer = elapsedTimer;
    }
}

