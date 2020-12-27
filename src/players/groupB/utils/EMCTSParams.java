package players.groupB.utils;

import players.groupB.utils.Const;
import players.optimisers.ParameterSet;
import utils.Pair;
import utils.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EMCTSParams implements ParameterSet {
    //FM budget
    public int fm_budget = Const.BudgetType.FM_BUDGET;


    private int budget_type = 1;
    private int individual_length = 12;
    private int iteration_budget = 200;
    private double mutation_rate = 0.5;
    private int heuristic_method = Const.Heuristics.CUSTOM_HEURISTIC;
    private int tree_depth = 8;
    private boolean shift_buffer = true;
    private int init_type = Const.InitType.INIT_RANDOM;

    private int time_budget = 40;

    @Override
    public void setParameterValue(String name, Object value) {
        switch(name) {
            case "init_type":
                init_type = (int) value;
                break;
            case "shift_buffer":
                shift_buffer = (boolean) value;
                break;
            case "fm_budget":
                fm_budget = (int) value;
                break;
            case "budget_type":
                budget_type = (int) value;
                break;
            case "individual_length":
                individual_length = (int) value;
                break;
            case "iteration_budget":
                iteration_budget = (int) value;
                break;
            case "mutation_rate":
                mutation_rate = (double) value;
                break;
            case "heuristic_method":
                heuristic_method = (int) value;
                break;
            case "tree_depth":
                tree_depth = (int) value;
                break;
        }
    }

    @Override
    public Object getParameterValue(String root) {
        Object value = null;
        switch(root) {
            case "init_type":
                return init_type;
            case "shift_buffer":
                return shift_buffer;
            case "fm_budget":
                return fm_budget;
            case "budget_type":
                return budget_type;
            case "individual_length":
                return individual_length;
            case "iteration_budget":
                return iteration_budget;
            case "mutation_rate":
                return mutation_rate;
            case "heuristic_method":
                return heuristic_method;
        }
        return null;
    }

    @Override
    public ArrayList<String> getParameters() {
        return null;
    }

    @Override
    public Map<String, Object[]> getParameterValues() {
        return null;
    }

    @Override
    public Pair<String, ArrayList<Object>> getParameterParent(String parameter) {
        return null;
    }

    @Override
    public Map<Object, ArrayList<String>> getParameterChildren(String root) {
        return null;
    }

    @Override
    public Map<String, String[]> constantNames() {
        return null;
    }




}
