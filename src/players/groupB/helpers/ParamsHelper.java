package players.groupB.helpers;

import core.GameState;
import players.heuristics.*;

import java.util.Random;

import players.optimisers.ParameterSet;

import static players.rhea.utils.Constants.*;
import static players.rhea.utils.Constants.WIN_SCORE_HEURISTIC;

public class ParamsHelper {
    private GameState gameState;
    private Random randomGenerator;
    private StateHeuristic stateHeuristic;
    private ParameterSet params;

    public ParamsHelper(GameState gs, ParameterSet params, Random randomGenerator) {
        this.params = params;
        this.randomGenerator = randomGenerator;
        this.gameState = gs;
    }

    public void setUpSuitableHeuristic(int suitableHeuristic) {
        switch (suitableHeuristic) {
            case PLAYER_COUNT_HEURISTIC:
                stateHeuristic = new PlayerCountHeuristic();
                break;
            case CUSTOM_HEURISTIC:
                stateHeuristic = new CustomHeuristic(gameState);
                break;
            case ADVANCED_HEURISTIC:
                stateHeuristic = new AdvancedHeuristic(gameState, randomGenerator);
                break;
            default:
            case WIN_SCORE_HEURISTIC:
                stateHeuristic = new WinScoreHeuristic();
                break;
        }
    }

    public StateHeuristic getStateHeuristic() {
        return stateHeuristic;
    }

    public int getIndividualLength() {
        return ObjectHelper.getIntValue(this.params.getParameterValue("individual_length"));
    }

    public int getInitType() {
        return ObjectHelper.getIntValue(this.params.getParameterValue("init_type"));
    }


}
