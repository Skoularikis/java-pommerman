package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ObjectHelper;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.mcts.SingleTreeNode;
import players.optimisers.ParameterSet;
import players.rhea.utils.FMBudget;
import utils.ElapsedCpuTimer;
import utils.Utils;

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
        EMCTSsol selected = null;
        if (!this.gameState.isTerminal()) {
            if (fullyExpanded(emctSsol)) {
                double bestValue = -Double.MAX_VALUE;
                for (EMCTSsol child : emctSsol.getChildren()){
                    double uctValue = uctValue(child);
                    if (uctValue > bestValue) {
                        selected = child;
                        bestValue = uctValue;
                    }
                }
                if (selected == null)
                {
                    throw new RuntimeException("Warning! returning null: " + bestValue + " : " + emctSsol.getChildren().size() + " " +
                            + emctSsol.getBounds()[0] + " " + emctSsol.getBounds()[1]);
                }
            }
        }
        return selected;
    }

    @Override
    public void rollOut(GameState state) {

    }

    @Override
    public double uctValue(Solution solution) {
        EMCTSsol child = (EMCTSsol)solution;
        double hvVal = child.getPopulation().get_value();
        double childValue =  hvVal / (child.getVisited_count() + Const.epsilon);
        childValue = Utils.normalise(childValue, child.getBounds()[0], child.getBounds()[1]);
        double uctValue = childValue +
                Const.K * Math.sqrt(Math.log(child.getVisited_count() + 1) / (child.getVisited_count() + Const.epsilon));
        uctValue = Utils.noise(uctValue, Const.epsilon, this.randomGenerator.nextDouble());
        return uctValue;
    }

    @Override
    public void backUp(Solution node) {
        EMCTSsol n = (EMCTSsol)node;
        while(n.getParent() != null)
        {
            n.increaseVisitedCount();
            double parentTotValue = n.getParent().getPopulation().get_value();
            double currentValue = n.getPopulation().get_value();
            n.getParent().getPopulation().set_value((parentTotValue + currentValue));

            double result = n.getParent().getPopulation().get_value();

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

    private boolean fullyExpanded(EMCTSsol emctSsol) {
        for (EMCTSsol sol : emctSsol.getChildren()) {
            if (sol.getPopulation() == null) {
                return false;
            }
        }
        return true;
    }

}
