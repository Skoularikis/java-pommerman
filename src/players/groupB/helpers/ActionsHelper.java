package players.groupB.helpers;
import players.rhea.evo.Individual;
import utils.Types;

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
}
