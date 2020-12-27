package players.groupB.interfaces;

import core.GameState;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import players.rhea.utils.FMBudget;

public interface MctsPlayable {
    Solution treePolicy(Solution emctSsol);
//    void rollOut(GameState state);
    void backUp(EMCTSsol node, double result);
    void setParamsHelper(GameState gameState, ParameterSet params);
}
