package players.groupB;
import core.GameState;
import players.Player;
import players.optimisers.ParameterizedPlayer;
import utils.Types;

import java.util.Random;
TestPlayer
public class  extends ParameterizedPlayer {


    protected TestPlayer(long seed, int pId) {
        super(seed, pId);
    }

    @Override
    public void reset(long l, int i) {

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
