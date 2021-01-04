package players.groupB.utils;
import core.GameState;
import players.heuristics.StateHeuristic;
import players.rhea.evo.Individual;

import java.util.ArrayList;

public class EMCTSsol extends Solution {

    private EMCTSsol parent;
    private Individual population;
    private ArrayList<EMCTSsol> children = new ArrayList<EMCTSsol>();
    private double[] bounds = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    private int rollout_depth;

    public double[] getBounds() {
        return bounds;
    }
    public void setBounds(double[] bounds) {
        this.bounds = bounds;
    }
    public void setBounds1(double bounds1) {
        this.bounds[0] = bounds1;
    }
    public void setBounds2(double bounds2) {
        this.bounds[1] = bounds2;
    }

    public void setPopulation(Individual population) {
        this.population = population;
    }

    public Individual getPopulation() {
        return population;
    }

    public void setParent(EMCTSsol parent) {
        this.parent = parent;
    }

    public EMCTSsol getParent() {
        return parent;
    }

    public void setChildren(ArrayList<EMCTSsol> children) {
        this.children = children;
    }

    public ArrayList<EMCTSsol> getChildren() {
        return children;
    }

    public EMCTSsol copy() {
        EMCTSsol a = new EMCTSsol();
        a.setChildren(this.getChildren());
        a.setParent(this.getParent());
        a.setPopulation(this.getPopulation().copy());
        a.setVisited_count(this.getVisited_count());
        a.setBounds1(this.getBounds()[0]);
        a.setBounds2(this.getBounds()[1]);
        a.setBounds(this.getBounds());
        return a;
    }

    public int getRollout_depth() {
        return rollout_depth;
    }

    public void setRollout_depth(int rollout_depth) {
        this.rollout_depth = rollout_depth;
    }
}
