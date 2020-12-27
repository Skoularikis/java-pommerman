package players.groupB.interfaces;

import core.GameState;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.optimisers.ParameterSet;

public interface EvoPlayable {
    EMCTSsol createRootStateSolution();
    void setParamsHelper(GameState gameState, ParameterSet params);
}
