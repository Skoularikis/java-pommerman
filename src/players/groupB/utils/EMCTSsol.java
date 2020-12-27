package players.groupB.utils;
import players.rhea.evo.Individual;

import java.util.ArrayList;

public class EMCTSsol {

    private EMCTSsol parent;
    private Individual population;
    private int visited_count = 0;
    private ArrayList<EMCTSsol> children;

    public Individual getPopulation() {
        return population;
    }

    public void setPopulation(Individual population) {
        this.population = population;
    }

    public EMCTSsol getParent() {
        return parent;
    }

    public void setParent(EMCTSsol parent) {
        this.parent = parent;
    }

    public int getVisited_count() {
        return visited_count;
    }

    public void increaseVisitedCount() {
        this.visited_count++;
    }

    public ArrayList<EMCTSsol> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<EMCTSsol> children) {
        this.children = children;
    }
}
