package players.groupB.utils;

import players.optimisers.ParameterSet;
import utils.Pair;

import java.util.ArrayList;
import java.util.Map;


public class EMCTSParams implements ParameterSet {

    private int budget_type = 1;
    private int individual_length = 12;
    private int iteration_budget = 200;
    private double mutation_rate = 0.5;
    private int heuristic_method = 2;
    private int tree_depth = 8;

    //
    private int time_budget = 40;

    public int getBudget_type() {
        return budget_type;
    }

    public void setBudget_type(int budget_type) {
        this.budget_type = budget_type;
    }

    public int getIndividual_length() {
        return individual_length;
    }

    public void setIndividual_length(int individual_length) {
        this.individual_length = individual_length;
    }

    public int getIteration_budget() {
        return iteration_budget;
    }

    public void setIteration_budget(int iteration_budget) {
        this.iteration_budget = iteration_budget;
    }

    public double getMutation_rate() {
        return mutation_rate;
    }

    public void setMutation_rate(double mutation_rate) {
        this.mutation_rate = mutation_rate;
    }

    public int getHeuristic_method() {
        return heuristic_method;
    }

    public void setHeuristic_method(int heuristic_method) {
        this.heuristic_method = heuristic_method;
    }

    public int getTree_depth() {
        return tree_depth;
    }

    public void setTree_depth(int tree_depth) {
        this.tree_depth = tree_depth;
    }

    public int getTime_budget() {
        return time_budget;
    }

    public void setTime_budget(int time_budget) {
        this.time_budget = time_budget;
    }

    @Override
    public void setParameterValue(String param, Object value) {
    }

    @Override
    public Object getParameterValue(String root) {
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
