package players.groupB;

import core.GameState;
import players.Player;
import players.groupB.emcts.Emcts;
import players.groupB.helpers.ObjectHelper;
import players.groupB.interfaces.GamePlayable;
import players.groupB.utils.EMCTSParams;
import players.optimisers.ParameterSet;
import players.optimisers.ParameterizedPlayer;
import utils.Types;
import java.util.Random;


public class EMCTSPlayer extends ParameterizedPlayer {

    private ParameterSet params;
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
        this.params = getParameters();
        if (this.params == null) {
            this.params = new EMCTSParams();
            super.setParameters(this.params);
        }
        // Set up random generator
        Random randomGenerator = new Random(seed);

        //Initialize interface
        gamePlayable = new Emcts(this.params, randomGenerator);
;
    }

    @Override
    public Types.ACTIONS act(GameState gs) {
        gamePlayable.setRootState(gs);
//        gamePlayable.getActionToExecute();

        return null;
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
