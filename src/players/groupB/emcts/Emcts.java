package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ObjectHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.interfaces.GamePlayable;
import players.groupB.interfaces.MctsPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.optimisers.ParameterSet;
import utils.ElapsedCpuTimer;

import java.util.Random;


public class Emcts implements GamePlayable {
    //CPU Timer
    private ElapsedCpuTimer elapsedTimer;
    // For MCTS part
    private MctsPlayable mctsOperations;
    // For Evo part
    private EvoPlayable evoOperations;
    // For params
    private EMCTSParams params;
    // Random Generator
    private Random randomGenerator;
    // GameState
    public GameState rootState;

    public Emcts(ParameterSet params, Random randomGenerator) {
        //Set up EMCTS parameters
        this.params = (EMCTSParams)params;
        this.randomGenerator = randomGenerator;
        setUpElapsedCpuTimer(this.params);
        //Initialize MctsOperations
        this.mctsOperations = new MctsOperations(this.randomGenerator, this.elapsedTimer);
        //Initialize EvoOperations
        this.evoOperations = new EvoOperations(this.randomGenerator, this.elapsedTimer);
    }

    @Override
    public void setRootState(GameState gameState) {
        this.rootState = gameState;
        this.mctsOperations.setParamsHelper(rootState, this.params);
        this.evoOperations.setParamsHelper(rootState, this.params);
    }

    @Override
    public void getActionToExecute(boolean isRootState) {

        boolean stop = false;
        while (!stop){
            EMCTSsol rootSol = evoOperations.createRootStateSolution(isRootState);


//            mctsOperations.treePolicy(rootSol);

//            createRootStateSolution(rootState.copy());
//            EMCTSsol selected = treePolicy(rootState, root);
            //EVaulaute Selected
//            backUp(selected, delta);
            stop = true;
        }
    }

    private void setUpElapsedCpuTimer(EMCTSParams params) {
        ElapsedCpuTimer elapsedTimer = null;
        if (ObjectHelper.getIntValue(params.getParameterValue("budget_type")) == Const.BudgetType.TIME_BUDGET) {
            elapsedTimer = new ElapsedCpuTimer();
            elapsedTimer.setMaxTimeMillis(ObjectHelper.getIntValue(params.getParameterValue("time_budget")));
        }
        this.elapsedTimer = elapsedTimer;
    }






}

