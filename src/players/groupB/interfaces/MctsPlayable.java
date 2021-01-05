package players.groupB.interfaces;

import core.GameState;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import utils.Types;

public interface MctsPlayable {
    Solution treePolicy(Solution sol);
    Solution uct(Solution solution);

    Solution expand(Solution cur);

    void rollOut(GameState state);
    void roll(GameState gameState, Types.ACTIONS actions);

    void backUp(Solution node, double value);

    //Extra
    boolean finishRollout(GameState gameState, int thisDepth);
    boolean notFullyExpanded(Solution solution);

    //Helpers
    void setEvoPlayable(EvoPlayable evoPlayable);
    void setParamsHelper(GameState gameState, ParameterSet params);

    //Repair Policy
    int repairPolicy(GameState gameState, int action);

}
