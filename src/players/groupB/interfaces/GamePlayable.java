package players.groupB.interfaces;

import core.GameState;
import utils.ElapsedCpuTimer;

public interface GamePlayable {
    void setRootState(GameState gameState);
    void getActionToExecute();
}
