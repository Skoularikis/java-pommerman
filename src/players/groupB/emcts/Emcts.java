package players.groupB.emcts;

import core.GameState;
import players.groupB.interfaces.GamePlayable;
import players.groupB.utils.EMCTSParams;
import players.heuristics.*;
import players.mcts.SingleTreeNode;
import players.rhea.utils.FMBudget;
import utils.ElapsedCpuTimer;
import utils.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static players.rhea.utils.Constants.*;
import static players.rhea.utils.Constants.WIN_SCORE_HEURISTIC;


public class Emcts implements GamePlayable {

    private Emcts[] children;
    private EMCTSParams params;
    private Random randomGenerator;
    private StateHeuristic stateHeuristic;
    private ElapsedCpuTimer elapsedTimer;

    public GameState rootState;
    private FMBudget fmBudget;

    public Emcts(EMCTSParams params, Random randomGenerator) {
        this.params = params;
        this.randomGenerator = randomGenerator;
        this.children = new Emcts[params.getAvailableActions().size()];
    }

    @Override
    public void setRootState(GameState gameState, ElapsedCpuTimer elapsedCpuTimer) {
        rootState = gameState;
        this.elapsedTimer = elapsedCpuTimer;
        fmBudget.reset();
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
    public void getActionToExecute() {
        boolean stop = false;
        while (!stop){
            createRootStateSolution(rootState.copy());
            EMCTS[] selected = treePolicy(state);
            //EVaulaute Selected
            backUp(selected, delta);
        }
    }

    private void createRootStateSolution(GameState copy) {
    }

    private Emcts treePolicy(GameState state) {
        return null;
    }
}

