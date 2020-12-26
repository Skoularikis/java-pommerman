package players.groupB.interfaces;

import core.GameState;
import utils.ElapsedCpuTimer;
import utils.Types;

import java.util.ArrayList;

public interface GamePlayable {
    void setRootState(GameState gameState, ElapsedCpuTimer elapsedCpuTimer);
    void getActionToExecute();
    ArrayList<Types.ACTIONS> getAvailableActions();
}
