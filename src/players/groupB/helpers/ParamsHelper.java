package players.groupB.helpers;

import core.GameState;
import players.groupB.utils.Const;
import players.heuristics.*;

import java.util.Random;

import players.optimisers.ParameterSet;
import players.rhea.utils.FMBudget;
import utils.ElapsedCpuTimer;

import static players.rhea.utils.Constants.*;
import static players.rhea.utils.Constants.WIN_SCORE_HEURISTIC;

public class ParamsHelper {

    private ElapsedCpuTimer elapsedTimer;
    private FMBudget fmBudget;
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

    public int getIntValue(String name) {
        return ObjectHelper.getIntValue(this.params.getParameterValue(name));
    }

    public String getStringValue(String name) {
        return ObjectHelper.getStringValue(this.params.getParameterValue(name));
    }

    public double getDoubleValue(String name) {
        return ObjectHelper.getDoubleValue(this.params.getParameterValue(name));
    }

    public boolean getBooleanValue(String name){
        return ObjectHelper.getBooleanValue(this.params.getParameterValue(name));
    }


    public ElapsedCpuTimer getElapsedTimer() {
        return elapsedTimer;
    }

    public FMBudget getFmBudget() {
        return fmBudget;
    }

    public void initializeBudgets() {
        this.elapsedTimer = new ElapsedCpuTimer();
        this.elapsedTimer.setMaxTimeMillis(getIntValue("time_budget"));
        this.fmBudget = new FMBudget(getIntValue("fm_budget") );
        this.fmBudget.reset();
    }

    public boolean gotBudget(int iterationsRemaining) {
        boolean gotBudget = true;
        int budget_type = getIntValue("budget_type");
        if (budget_type == Const.BudgetType.TIME_BUDGET) {
            gotBudget = this.elapsedTimer.enoughBudgetIteration(break_ms);
        } else if (budget_type == Const.BudgetType.ITERATION_BUDGET) {
            gotBudget = iterationsRemaining > 0;
        } else if (budget_type == Const.BudgetType.FM_BUDGET) {
            if (fmBudget != null) {
                gotBudget = fmBudget.enoughBudgetIteration();
            } else {
                gotBudget = this.fmBudget.enoughBudgetIteration();
            }
        }
        return gotBudget;
    }

    public void endIteration() {
        int budget_type = getIntValue("budget_type");
        if (budget_type == TIME_BUDGET) {
            this.elapsedTimer.endIteration();
        } else if (budget_type == FM_BUDGET) {
            if (fmBudget != null) {
                fmBudget.endIteration();
            } else {
                this.fmBudget.endIteration();
            }
        }
    }

}
