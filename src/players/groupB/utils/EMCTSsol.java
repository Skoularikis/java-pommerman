package players.groupB.utils;
import players.rhea.evo.Individual;
import utils.Utils;

import java.util.Arrays;
import java.util.Random;


public class EMCTSsol {

    private Individual[] population;
    private Individual[] parents;
    public boolean visited = false;
    private int reward_val;

    
    public int getReward_val() {
        return reward_val;
    }

    public void setReward_val(int reward_val) {
        this.reward_val = reward_val;
    }

    public Individual[] getPopulation() {
        return population;
    }

    public Individual[] getParents() {
        return parents;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setPopulation(Individual[] population) {
        this.population = population;
    }

    public void setParents(Individual[] parents) {
        this.parents = parents;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }









}
