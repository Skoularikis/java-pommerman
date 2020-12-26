package players.groupB;

import core.GameState;
import players.Player;
import players.groupB.emcts.Emcts;
import players.groupB.interfaces.GamePlayable;
import players.groupB.utils.EMCTSParams;
import players.optimisers.ParameterizedPlayer;
import utils.ElapsedCpuTimer;
import utils.Types;

import java.util.Random;

import static players.rhea.utils.Constants.TIME_BUDGET;

public class EMCTSPlayer extends ParameterizedPlayer {

    private EMCTSParams params;
    private GamePlayable gamePlayable;

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
        this.params = (EMCTSParams) getParameters();
        if (this.params == null) {
            this.params = new EMCTSParams();
            super.setParameters(this.params);
        }
        // Set up random generator
        Random randomGenerator = new Random(seed);

        //Initialize interface
        gamePlayable = new Emcts(this.params, randomGenerator);
    }

    @Override
    public Types.ACTIONS act(GameState gs) {
        ElapsedCpuTimer elapsedTimer = null;
        if (params.budget_type == TIME_BUDGET) {
            elapsedTimer = new ElapsedCpuTimer();
            elapsedTimer.setMaxTimeMillis(params.time_budget);
        }

        return null;
    }

    @Override
    public int[] getMessage() {
        return new int[0];
    }

    @Override
    public Player copy() {
        return null;
    }

    private void setup(GameState rootState, ElapsedCpuTimer elapsedTimer) {
//        gInterface.initTick(rootState, elapsedTimer);
    }
}
