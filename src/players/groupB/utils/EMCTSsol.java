package players.groupB.utils;
import players.rhea.evo.Individual;

public class EMCTSsol {

    private Individual population;
    private Individual parent;
    private int visited_count = 0;

    public Individual getPopulation() {
        return population;
    }

    public void setPopulation(Individual population) {
        this.population = population;
    }

    public Individual getParent() {
        return parent;
    }

    public void setParent(Individual parent) {
        this.parent = parent;
    }

    public int getVisited_count() {
        return visited_count;
    }

    public void increaseVisitedCount() {
        this.visited_count++;
    }

}
