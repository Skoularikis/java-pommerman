package players.groupB.interfaces;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;

public interface EvoPlayable {
    Solution createRootStateSolution(boolean isRootState);
    void setParamsHelper(GameState gameState, ParameterSet params);
    ParamsHelper getParamsHelper();
    void evaluate(Solution rootSol);
}
