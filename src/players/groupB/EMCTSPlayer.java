package players.groupB;

import core.GameState;
import players.Player;
import players.optimisers.ParameterizedPlayer;
import utils.Types;

import java.util.Random;

public class EMCTSPlayer extends ParameterizedPlayer {

    /**
     * Random generator.
     */
    private Random m_rnd;

    /**
     * All actions available.
     */
    public Types.ACTIONS[] actions;

    protected EMCTSPlayer(long seed, int pId) {
        super(seed, pId);
    }

    @Override
    public Types.ACTIONS act(GameState gs) {
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
}
