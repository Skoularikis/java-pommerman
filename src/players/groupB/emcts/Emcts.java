package players.groupB.emcts;

import core.GameState;
import players.groupB.interfaces.GamePlayable;
import players.groupB.utils.EMCTSParams;
import utils.ElapsedCpuTimer;
import utils.Types;

import java.util.ArrayList;
import java.util.Random;

public class Emcts implements GamePlayable {

    private EMCTSParams params;
    private Random randomGenerator;

    public Emcts(EMCTSParams params, Random randomGenerator) {
        this.params = params;
        this.randomGenerator = randomGenerator;
    }

    @Override
    public void setRootState(GameState gameState, ElapsedCpuTimer elapsedCpuTimer) {

    }

    @Override
    public void getActionToExecute() {

    }

    @Override
    public ArrayList<Types.ACTIONS> getAvailableActions() {
        return null;
    }
}
