package players.groupB.helpers;
import core.GameState;
import players.rhea.evo.Individual;
import utils.Types;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ActionsHelper {

    private static HashMap<Integer, Types.ACTIONS> action_mapping;

    public static void fillIndividualWithRandomActions(Individual individual, Random gen) {
        for (int i = 0; i < individual.get_length(); i++) {
            individual.set_action(i,gen.nextInt(individual.get_max_actions()));
        }
    }

    public static Types.ACTIONS[] getAvailableActionsInArray() {
        ArrayList<Types.ACTIONS> actionsList = getAvailableActionsInArrayList();
        Types.ACTIONS[] actions = new Types.ACTIONS[actionsList.size()];

        int i = 0;
        for (Types.ACTIONS act : actionsList) {
            actions[i++] = act;
        }
        return actions;
    }

    public static HashMap<Integer, Types.ACTIONS> getAvailableActions () {

        ArrayList<Types.ACTIONS> availableActions = Types.ACTIONS.all();
        int max_actions = availableActions.size();
        action_mapping = new HashMap<>();
        for (int i = 0; i < max_actions; i++) {
            action_mapping.put(i, availableActions.get(i));
        }
        action_mapping.put(max_actions, Types.ACTIONS.ACTION_STOP);

        return action_mapping;
    }

    public static ArrayList<Types.ACTIONS> getAvailableActionsInArrayList(){
        return Types.ACTIONS.all();
    }

    public static ArrayList<Types.ACTIONS> getSafeRandomActions(GameState state, Random randomGenerator) {
        Types.TILETYPE[][] board = state.getBoard();
        ArrayList<Types.ACTIONS> actionsToTry = getAvailableActionsInArrayList();
        int width = board.length;
        int height = board[0].length;

        for(Types.ACTIONS nAction : getAvailableActionsInArrayList()) {

            Vector2d dir = nAction.getDirection().toVec();

            Vector2d pos = state.getPosition();
            int x = pos.x + dir.x;
            int y = pos.y + dir.y;

            if (x < 0 || y < 0  || x > width || y > height) {
                actionsToTry.remove(nAction);

            }
            if (x >= 0 && x < width && y >= 0 && y < height)
                if(board[y][x] == Types.TILETYPE.FLAMES) {
                    actionsToTry.remove(nAction);
                }
        }
        return actionsToTry;
    }

    public static int safeRandomAction(GameState state, Random randomGenerator) {
        Types.TILETYPE[][] board = state.getBoard();
        ArrayList<Types.ACTIONS> actionsToTry = getAvailableActionsInArrayList();
        int width = board.length;
        int height = board[0].length;

        while(actionsToTry.size() > 0) {

            int nAction = randomGenerator.nextInt(actionsToTry.size());
            Types.ACTIONS act = actionsToTry.get(nAction);
            Vector2d dir = act.getDirection().toVec();

            Vector2d pos = state.getPosition();
            int x = pos.x + dir.x;
            int y = pos.y + dir.y;

            if (x >= 0 && x < width && y >= 0 && y < height)
                if(board[y][x] != Types.TILETYPE.FLAMES)
                    return nAction;

            actionsToTry.remove(nAction);
        }

        //Uh oh...
        return randomGenerator.nextInt(getAvailableActionsInArrayList().size());
    }

}
