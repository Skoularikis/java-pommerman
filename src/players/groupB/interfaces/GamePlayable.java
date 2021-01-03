package players.groupB.interfaces;

import core.GameState;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import utils.ElapsedCpuTimer;
import utils.Types;

public interface GamePlayable {
    void setRootState(GameState gameState, Solution currentSolution);
    void getActionToExecute();

    Solution getBestSolution();
}
