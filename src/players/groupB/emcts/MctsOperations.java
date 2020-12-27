package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ObjectHelper;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.optimisers.ParameterSet;
import players.rhea.utils.FMBudget;
import utils.ElapsedCpuTimer;

import java.util.Random;

public class MctsOperations implements MctsPlayable {
    // Elapsed Timer
    private ElapsedCpuTimer elapsedTimer;
    private ParamsHelper paramsHelper;
    private Random randomGenerator;
    private GameState gameState;

    public MctsOperations(Random randomGenerator, ElapsedCpuTimer elapsedTimer) {
        this.randomGenerator = randomGenerator;
//        ParamsHelper paramsHelper = new ParamsHelper((EMCTSParams) params);
//        this.children = new EMCTSsol()[this.params.getAvailableActions().size()];
//        this.fmBudget = new FMBudget(ObjectHelper.getIntValue(this.params.getParameterValue("fm_budget")));
//        fmBudget.reset();
        this.elapsedTimer = elapsedTimer;
    }


    @Override
    public void treePolicy(GameState gs) {

    }

    @Override
    public void rollOut(GameState state) {

    }

    @Override
    public void backUp(GameState node, double result) {

    }

    @Override
    public void setParamsHelper(GameState gameState, ParameterSet params) {
        this.gameState = gameState;
        if (this.paramsHelper == null){
            this.paramsHelper = new ParamsHelper(gameState, params, this.randomGenerator);
        }
    }

}
