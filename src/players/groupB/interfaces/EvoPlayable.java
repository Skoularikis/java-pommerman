package players.groupB.interfaces;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import players.rhea.evo.Individual;


public interface EvoPlayable {
    double evaluate(Solution rootSol);
    Solution mutate(Solution sol);
    Solution shift_buffer(Solution solution);
    //Helpers
    void setMcts(MctsPlayable mctsPlayable);
    void setParamsHelper(GameState gameState, ParameterSet params);
    ParamsHelper getParamsHelper();

}
