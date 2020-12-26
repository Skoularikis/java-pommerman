package players.groupB;

import core.GameState;
import players.Player;
import players.groupB.emcts.Emcts;
import players.groupB.interfaces.GamePlayable;
import players.groupB.utils.EMCTSParams;
import players.optimisers.ParameterizedPlayer;
import utils.ElapsedCpuTimer;
import utils.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static players.rhea.utils.Constants.TIME_BUDGET;

public class EMCTSPlayer extends ParameterizedPlayer {

    private EMCTSParams params;
    private GamePlayable gamePlayable;

    private HashMap<Integer, Types.ACTIONS> action_mapping;

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
        if (params.getBudget_type() == TIME_BUDGET) {
            elapsedTimer = new ElapsedCpuTimer();
            elapsedTimer.setMaxTimeMillis(params.getTime_budget());
        }
        gamePlayable.setRootState(gs, elapsedTimer);
        gamePlayable.getActionToExecute();
    }

    @Override
    public int[] getMessage() {
        // default message
        return new int[Types.MESSAGE_LENGTH];
    }

    @Override
    public Player copy() {
        return new EMCTSPlayer(seed, playerID, params);
    }

    public HashMap<Integer, Types.ACTIONS> getAvailableActions () {

        ArrayList<Types.ACTIONS> availableActions = Types.ACTIONS.all();
        int max_actions = availableActions.size();
        action_mapping = new HashMap<>();
        for (int i = 0; i < max_actions; i++) {
            action_mapping.put(i, availableActions.get(i));
        }
        action_mapping.put(max_actions, Types.ACTIONS.ACTION_STOP);

        return action_mapping;
    }

}
