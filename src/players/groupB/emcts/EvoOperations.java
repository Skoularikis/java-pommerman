package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.optimisers.ParameterSet;
import players.rhea.evo.Individual;
import utils.ElapsedCpuTimer;

import java.util.Random;

import static players.groupB.helpers.ActionsHelper.fillIndividualWithRandomActions;
import static players.groupB.helpers.ActionsHelper.getAvailableActions;

public class EvoOperations implements EvoPlayable {

    private ParamsHelper paramsHelper;
    private Random randomGenerator;
    private ElapsedCpuTimer elapsedTimer;
    private EMCTSsol rootStateSolution;
    private GameState gameState;

    public EvoOperations(Random randomGenerator, ElapsedCpuTimer elapsedTimer) {
        this.randomGenerator = randomGenerator;
        this.elapsedTimer = elapsedTimer;
    }

    @Override
    public void setParamsHelper(GameState gameState, ParameterSet params) {
        this.gameState = gameState;
        if (this.paramsHelper == null){
            this.paramsHelper = new ParamsHelper(gameState, params, this.randomGenerator);
        }
    }

    @Override
    public EMCTSsol createRootStateSolution() {
        rootStateSolution = new EMCTSsol();
        rootStateSolution.setParent(null);
        if (rootStateSolution.getPopulation() == null){
            rootStateSolution.setPopulation(
                    new Individual(
                            this.paramsHelper.getIndividualLength(),
                            randomGenerator,
                            getAvailableActions().size())
            );
            if (this.paramsHelper.getInitType() == Const.InitType.INIT_RANDOM) {
                fillIndividualWithRandomActions(rootStateSolution.getPopulation(),randomGenerator);
            }
            return rootStateSolution;
        }
        return null;
    }
}
