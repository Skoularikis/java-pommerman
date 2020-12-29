package players.groupB.interfaces;

import core.GameState;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import utils.Types;

import java.util.ArrayList;

public interface MctsPlayable {
    Solution treePolicy(Solution solution);

    double uctValue(Solution solution);
    void backUp(Solution node);
    void setParamsHelper(GameState gameState, ParameterSet params);
    boolean finishRollout(GameState gameState, int thisDepth);


    void rollOut(GameState state);
    void roll(GameState gameState, Types.ACTIONS actions);

}
