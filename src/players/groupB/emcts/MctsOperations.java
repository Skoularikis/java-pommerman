package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import utils.Types;
import utils.Utils;

import java.util.Random;

import static players.groupB.helpers.ActionsHelper.getAvailableActionsInArrayList;

public class MctsOperations implements MctsPlayable {
    private ParamsHelper paramsHelper;
    private Random randomGenerator;
    private GameState gameState;
    //For Evo Operations
    private EvoPlayable evoOperations;

    public MctsOperations(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public Solution treePolicy(Solution sol) {
        EMCTSsol cur = (EMCTSsol)sol;
        //find stop
        while (!this.gameState.isTerminal())
        {
            if (!notFullyExpanded(cur)) {
                return this.evoOperations.mutate(cur);
            } else {
                cur = (EMCTSsol)uct(cur);;
            }
        }
        return cur;
    }

    @Override
    public Solution expand(Solution sol) {
        EMCTSsol cur = (EMCTSsol)sol;
        int bestAction = 0;
        double bestValue = -1;
        for (int i = 0; i < cur.getChildren().size(); i++) {
            double x = cur.getChildren().get(i).getPopulation().get_value();
            if (x > bestValue) {
                bestAction = i;
                bestValue = x;
            }
        }
        EMCTSsol child = cur.getChildren().get(bestAction);
        child.increaseVisitedCount();
        return child;
    }

    @Override
    public Solution uct(Solution solution) {
        EMCTSsol emctSsol = (EMCTSsol)solution;
        EMCTSsol selected = null;
        if (!this.gameState.isTerminal()) {

            double bestValue = -Double.MAX_VALUE;
            for (EMCTSsol child : emctSsol.getChildren()){
                double uctValue = uctValue(child);
                if (uctValue > bestValue) {
                    selected = child;
                    bestValue = uctValue;
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
    public void rollOut(GameState state) {

    }

    @Override
    public void roll(GameState gs, Types.ACTIONS act) {
        //Simple, all random first, then my position.
        int nPlayers = 4;
        Types.ACTIONS[] actionsAll = new Types.ACTIONS[4];
        int playerId = gs.getPlayerId() - Types.TILETYPE.AGENT0.getKey();
        for(int i = 0; i < nPlayers; ++i)
        {
            if(i == playerId)
            {
                actionsAll[i] = act;
            }
            else{
                int actionIdx = this.randomGenerator.nextInt(gs.nActions()); //mcts -> right, rhea -> place a bomb, random player -> rugth
                actionsAll[i] = Types.ACTIONS.all().get(actionIdx);
            }
        }
        gs.next(actionsAll);
    }



    @Override
    public void backUp(Solution node, double result) {
        EMCTSsol n = (EMCTSsol)node;
        while(n != null)
        {
            n.increaseVisitedCount();
            n.getPopulation().set_value(n.getPopulation().get_value() + result);
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
    public boolean finishRollout(GameState gameState, int thisDepth) {
        if (thisDepth >= this.paramsHelper.getIntValue("individual_length"))      //rollout end condition.
            return true;

        if (gameState.isTerminal())               //end of game
            return true;

        return false;
    }

    @Override
    public boolean notFullyExpanded(Solution solution) {
        EMCTSsol emctSsol = (EMCTSsol)solution;
        for (EMCTSsol sol : emctSsol.getChildren()) {
            if (sol != null && emctSsol.getChildren().size() == getAvailableActionsInArrayList().size()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setEvoPlayable(EvoPlayable evoPlayable) {
        this.evoOperations = evoPlayable;
    }

    @Override
    public void setParamsHelper(GameState gameState, ParameterSet params) {
        this.gameState = gameState;
        if (this.paramsHelper == null){
            this.paramsHelper = new ParamsHelper(gameState, params, this.randomGenerator);
            this.paramsHelper.setUpSuitableHeuristic(this.paramsHelper.getIntValue("heuristic_method"));
            this.paramsHelper.initializeBudgets();
        }
    }



}
