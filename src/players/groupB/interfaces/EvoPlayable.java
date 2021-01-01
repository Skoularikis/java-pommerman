package players.groupB.interfaces;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;


public interface EvoPlayable {
    Solution createRootStateSolution(boolean isRootState);
    void setParamsHelper(GameState gameState, ParameterSet params);
    ParamsHelper getParamsHelper();
    double evaluate(Solution rootSol, boolean useMutationClass);

    Solution mutate(Solution sol);

    //Helpers
    void setMcts(MctsPlayable mctsPlayable);
}
