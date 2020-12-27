package players.groupB.interfaces;

import core.GameState;
import players.groupB.utils.EMCTSParams;
import players.optimisers.ParameterSet;
import players.rhea.utils.FMBudget;

public interface MctsPlayable {
    void treePolicy(GameState gs);
    void rollOut(GameState state);
    void backUp(GameState node, double result);

    void setParamsHelper(GameState gameState, ParameterSet params);
}
