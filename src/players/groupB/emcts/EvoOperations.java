package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import players.rhea.evo.Individual;
import utils.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Random;

import static players.groupB.helpers.ActionsHelper.*;

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
            this.paramsHelper.setUpSuitableHeuristic(this.paramsHelper.getIntValue("heuristic_method"));
        }
    }

    @Override
    public ParamsHelper getParamsHelper() {
        return this.paramsHelper;
    }

    @Override
    public EMCTSsol createRootStateSolution(boolean isRootState) {
        rootStateSolution = new EMCTSsol();
        if (isRootState){
            rootStateSolution.setParent(null);
        }
        if (rootStateSolution.getPopulation() == null){
            rootStateSolution.setPopulation(
                    new Individual(
                            this.paramsHelper.getIntValue("individual_length"),
                            randomGenerator,
                            getAvailableActions().size())
            );
            if (this.paramsHelper.getIntValue("init_type") == Const.InitType.INIT_RANDOM) {
                if (isRootState){
                    fillIndividualWithRandomActions(rootStateSolution.getPopulation(),randomGenerator);
                }
                rootStateSolution.setChildren(new ArrayList<EMCTSsol>());
                for (int i=0; i < getAvailableActions().size(); i++) {
                    EMCTSsol child = new EMCTSsol();
                    child.setParent(rootStateSolution);
                    rootStateSolution.getChildren().add(child);
                }

            }
            return rootStateSolution;
        }
        return null;
    }

    @Override
    public double evaluate(Solution solution, boolean mutationClass) {
        return 0.0;
    }


}
