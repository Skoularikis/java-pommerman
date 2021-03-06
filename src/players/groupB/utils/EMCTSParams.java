package players.groupB.utils;

import players.groupB.utils.Const;
import players.optimisers.ParameterSet;
import utils.Pair;
import utils.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static players.rhea.utils.Constants.*;


public class EMCTSParams implements ParameterSet {
    //Budgets
    private int budget_type = Const.BudgetType.ITERATION_BUDGET;
    public int fm_budget = Const.BudgetType.FM_BUDGET;
    private int time_budget = 40;
    // player ID
    private int playerID;
    //Genetic Operator
    private int genetic_operator = Const.GeneticOperators.MUTATION_ONLY;
    private int mutation_type = Const.GeneticOperators.MUTATION_BIAS;
    //Evaluate
    private int evaluate_update = Const.Evaluation.EVALUATE_UPDATE_RAW;
    private int evaluate_act = Const.Evaluation.EVALUATE_ACT_LAST;
    //Rollout depth
    private int rollout_depth = 8;
    //Init Type
    private int init_type = Const.InitType.INIT_RANDOM;
    //Heuristic Class to use
    private int heuristic_method = Const.Heuristics.CUSTOM_HEURISTIC;


    private int individual_length = 12;
    private int iteration_budget = 200;
    private double mutation_rate = 0.1;

    private int tree_depth = 8;
    private boolean shift_buffer = true;

    private int gene_size = 1;
    private int mutation_gene_count = 1;
    private boolean elitism = false;




    @Override
    public void setParameterValue(String name, Object value) {
        switch(name) {
            case "time_budget":
                time_budget = (int)value;
                break;
            case "rollout_depth":
                rollout_depth = (int)value;
                break;
            case "evaluate_update":
                evaluate_update = (int)value;
                break;
            case "genetic_operator":
                genetic_operator = (int) value;
                break;
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
            case "gene_size":
                gene_size = (int) value;
                break;
            case "mutation_type":
                mutation_type = (int) value;
                break;
            case "mutation_gene_count":
                mutation_gene_count = (int) value;
                break;
            case "elitism":
                elitism = (boolean) value;
                break;
            case "evaluate_act":
                evaluate_act = (int) value;
                break;
            case "playerID":
                playerID = (int) value;
                break;

        }
    }

    @Override
    public Object getParameterValue(String root) {
        switch(root) {
            case "time_budget":
                return time_budget;
            case "rollout_depth":
                return rollout_depth;
            case "evaluate_update":
                return evaluate_update;
            case "genetic_operator":
                return genetic_operator;
            case "gene_size":
                return gene_size;
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
            case "mutation_type":
                return mutation_type;
            case "mutation_gene_count":
                return mutation_gene_count;
            case "elitism":
                return elitism;
            case "evaluate_act":
                return evaluate_act;
            case "playerID":
                return playerID;
        }
        return null;
    }

    @Override
    public ArrayList<String> getParameters() {
        return null;
    }

    @Override
    public Map<String, Object[]> getParameterValues() {
        HashMap<String, Object[]> parameterValues = new HashMap<>();
        parameterValues.put("gene_size", new Integer[]{1, 2, 3, 4, 5});
        parameterValues.put("mutation_type", new Integer[]{MUTATION_UNIFORM, MUTATION_BIT, MUTATION_BIAS});
        parameterValues.put("mutation_gene_count", new Integer[]{1, 2, 3, 4, 5});
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
