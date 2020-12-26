package players.groupB.emcts;

import core.GameState;
import players.groupB.interfaces.GamePlayable;
import players.groupB.utils.EMCTSParams;
import players.heuristics.*;
import players.rhea.utils.FMBudget;
import utils.ElapsedCpuTimer;
import utils.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static players.rhea.utils.Constants.*;
import static players.rhea.utils.Constants.WIN_SCORE_HEURISTIC;


public class Emcts implements GamePlayable {

    private EMCTSParams params;
    private Random randomGenerator;
    private StateHeuristic stateHeuristic;
    private ElapsedCpuTimer elapsedTimer;

    public GameState rootState;
    private FMBudget fmBudget;

    private HashMap<Integer, Types.ACTIONS> action_mapping;

    public Emcts(EMCTSParams params, Random randomGenerator) {
        this.params = params;
        this.randomGenerator = randomGenerator;
    }

    @Override
    public void setRootState(GameState gameState, ElapsedCpuTimer elapsedCpuTimer) {

        rootState = gameState;
        this.elapsedTimer = elapsedCpuTimer;
        fmBudget.reset();
        getAvailableActions();
        switch (params.getHeuristic_method()) {
            case PLAYER_COUNT_HEURISTIC:
                stateHeuristic = new PlayerCountHeuristic();
                break;
            case CUSTOM_HEURISTIC:
                stateHeuristic = new CustomHeuristic(rootState);
                break;
            case ADVANCED_HEURISTIC:
                stateHeuristic = new AdvancedHeuristic(rootState, randomGenerator);
                break;
            default:
            case WIN_SCORE_HEURISTIC:
                stateHeuristic = new WinScoreHeuristic();
                break;


        }
    }

        @Override
        public void getActionToExecute () {

        }

        @Override
        public void getAvailableActions () {

            ArrayList<Types.ACTIONS> availableActions = Types.ACTIONS.all();
            int max_actions = availableActions.size();
            action_mapping = new HashMap<>();
            for (int i = 0; i < max_actions; i++) {
                action_mapping.put(i, availableActions.get(i));
            }
            action_mapping.put(max_actions, Types.ACTIONS.ACTION_STOP);

        }

    }

