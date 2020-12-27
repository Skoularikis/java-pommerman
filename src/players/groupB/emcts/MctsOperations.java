package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ObjectHelper;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.mcts.SingleTreeNode;
import players.optimisers.ParameterSet;
import players.rhea.utils.FMBudget;
import utils.ElapsedCpuTimer;

import java.util.Random;

public class MctsOperations implements MctsPlayable {
    // Elapsed Timer
    private ElapsedCpuTimer elapsedTimer;
    private ParamsHelper paramsHelper;
    private Random randomGenerator;
    private GameState gameState;

    public MctsOperations(Random randomGenerator, ElapsedCpuTimer elapsedTimer) {
        this.randomGenerator = randomGenerator;
//        ParamsHelper paramsHelper = new ParamsHelper((EMCTSParams) params);
//        this.children = new EMCTSsol()[this.params.getAvailableActions().size()];
//        this.fmBudget = new FMBudget(ObjectHelper.getIntValue(this.params.getParameterValue("fm_budget")));
//        fmBudget.reset();
        this.elapsedTimer = elapsedTimer;
    }


    @Override
    public Solution treePolicy(Solution sol) {
        EMCTSsol emctSsol = (EMCTSsol)sol;
        //NEED TO ADD MORE IN THE TREE OR IN THE METHOD IMPLEMENTING THE MUTATION (BETTER THERE)
        if (!this.gameState.isTerminal()) {
            if (notFullyExpanded(emctSsol)) {
                //return Mutated population
            }
        }
        return null;
    }


//    @Override
//    public void rollOut(GameState state) {
//
//    }

    @Override
    public void backUp(EMCTSsol node, double result) {
        EMCTSsol n = node;
        while(n != null)
        {
            n.increaseVisitedCount();

            double totValue = 0.0;
            totValue += result;

            if (result < n.getBounds()[0]) {
                n.setBounds1(result);
            }
            if (result > n.getBounds()[1]) {
                n.setBounds2(result);
            }
            n = n.getParent();
        }
    }

    @Override
    public void setParamsHelper(GameState gameState, ParameterSet params) {
        this.gameState = gameState;
        if (this.paramsHelper == null){
            this.paramsHelper = new ParamsHelper(gameState, params, this.randomGenerator);
        }
    }

    private boolean notFullyExpanded(EMCTSsol emctSsol) {
        for (EMCTSsol sol : emctSsol.getChildren()) {
            if (sol.getPopulation() == null) {
                return true;
            }
        }
        return false;
    }

}
