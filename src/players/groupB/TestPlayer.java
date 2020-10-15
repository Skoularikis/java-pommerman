package players.groupB;
import core.GameState;
import players.Player;
import utils.Types;

import java.util.Random;

public class TestPlayer extends Player {

    protected TestPlayer(long seed, int pId ) {
        super(seed, pId);
    }
    @Override
    public Types.ACTIONS act(GameState gameState) {
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
