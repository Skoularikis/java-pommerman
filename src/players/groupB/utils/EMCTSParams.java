package players.groupB.utils;

import players.optimisers.ParameterSet;
import utils.Pair;

import java.util.ArrayList;
import java.util.Map;


public class EMCTSParams implements ParameterSet {

    private int budget_type;
    private int individual_length;
    private int iteration_budget;
    private double mutation_rate;
    private int heuristic_method;

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
