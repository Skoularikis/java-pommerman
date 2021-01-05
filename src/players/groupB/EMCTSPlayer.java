package players.groupB;

import core.GameState;
import players.Player;
import players.groupB.emcts.Emcts;
import players.groupB.interfaces.GamePlayable;
import players.groupB.utils.EMCTSParams;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import players.optimisers.ParameterizedPlayer;
import utils.Types;

import java.util.Random;

import static players.groupB.helpers.ActionsHelper.getAvailableActionsInArrayList;


public class EMCTSPlayer extends ParameterizedPlayer {

    private ParameterSet params;
    private GamePlayable gamePlayable;
    private Random randomGenerator;
    private Solution currentSolution = null;

    protected EMCTSPlayer(long seed, int pId) {
        super(seed, pId);
    }

    public EMCTSPlayer(long seed, int playerID, EMCTSParams params) {
        super(seed, playerID, params);
        reset(seed, playerID);
    }

    @Override
    public void reset(long seed, int playerID) {
        super.reset(seed, playerID);

        // Make sure we have parameters
        this.params = getParameters();
        if (this.params == null) {
            this.params = new EMCTSParams();
            super.setParameters(this.params);
        }
        this.params.setParameterValue("playerID", playerID - Types.TILETYPE.AGENT0.getKey());
        // Set up random generator
        this.randomGenerator = new Random(seed);
        // Root of the tree
        gamePlayable = new Emcts(this.params, this.randomGenerator);
    }

    @Override
    public Types.ACTIONS act(GameState gs) {
        // Initialize Current Root Solution
        gamePlayable.setRootState(gs, this.currentSolution);
        // Play Action
        gamePlayable.getActionToExecute();
        // get Best/Most Visited solution
        EMCTSsol bestSolution = (EMCTSsol)gamePlayable.getBestSolution();

        this.currentSolution = bestSolution;

        Types.ACTIONS bestAction = getAvailableActionsInArrayList().get(bestSolution.getPopulation().get_action(0));

        return bestAction;
    }

    @Override
    public int[] getMessage() {
        // default message
        return new int[Types.MESSAGE_LENGTH];
    }

    @Override
    public Player copy() {
        return new EMCTSPlayer(seed, playerID,(EMCTSParams) params);
    }



}
