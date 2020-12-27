package players.groupB.interfaces;

import core.GameState;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import players.rhea.utils.FMBudget;

public interface MctsPlayable {
    Solution treePolicy(Solution solution);
    void rollOut(GameState state);
    double uctValue(Solution solution);
    void backUp(Solution node);
    void setParamsHelper(GameState gameState, ParameterSet params);
}
