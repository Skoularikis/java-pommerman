package players.groupB.emcts;

import core.GameState;
import players.groupB.helpers.ParamsHelper;
import players.groupB.interfaces.EvoPlayable;
import players.groupB.utils.Const;
import players.groupB.utils.EMCTSsol;
import players.groupB.utils.Solution;
import players.optimisers.ParameterSet;
import players.rhea.evo.Individual;
import players.rhea.evo.Mutation;
import players.rhea.utils.Utilities;
import utils.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static players.groupB.helpers.ActionsHelper.*;
import static players.rhea.utils.Constants.*;

public class EvoOperations implements EvoPlayable {

    private ParamsHelper paramsHelper;
    private Random randomGenerator;
    private ElapsedCpuTimer elapsedTimer;
    private EMCTSsol rootStateSolution;
    private GameState gameState;
    private Mutate mutationClass;
    private Individual[] population;

    public EvoOperations(Random randomGenerator, ElapsedCpuTimer elapsedTimer) {
        this.randomGenerator = randomGenerator;
        this.elapsedTimer = elapsedTimer;


    }

    @Override
    public void setParamsHelper(GameState gameState, ParameterSet params) {
        this.gameState = gameState;
        if (this.paramsHelper == null){
            this.paramsHelper = new ParamsHelper(gameState, params, this.randomGenerator);
            this.paramsHelper.setUpSuitableHeuristic(this.paramsHelper.getIntValue("heuristic_method"));
            mutationClass = new Mutate(paramsHelper, randomGenerator);
        }

    }

    @Override
    public ParamsHelper getParamsHelper() {
        return this.paramsHelper;
    }

    @Override
    public EMCTSsol createRootStateSolution(boolean isRootState) {
        rootStateSolution = new EMCTSsol();
        if (isRootState){
            rootStateSolution.setParent(null);
        }
        if (rootStateSolution.getPopulation() == null){
            rootStateSolution.setPopulation(
                    new Individual(
                            this.paramsHelper.getIntValue("individual_length"),
                            randomGenerator,
                            getAvailableActions().size())
            );
            if (this.paramsHelper.getIntValue("init_type") == Const.InitType.INIT_RANDOM) {
                if (isRootState){
                    fillIndividualWithRandomActions(rootStateSolution.getPopulation(),randomGenerator);
                }
                rootStateSolution.setChildren(new ArrayList<EMCTSsol>());
                for (int i=0; i < getAvailableActions().size(); i++) {
                    EMCTSsol child = new EMCTSsol();
                    child.setParent(rootStateSolution);
                    rootStateSolution.getChildren().add(child);
                }

            }
            return rootStateSolution;
        }
        return null;
    }



    @Override
    public double evaluate(Solution solution, boolean mutationClass) {
        return 0.0;
    }




//    public ArrayList mutate(EMCTSsol parent){
//        ArrayList<EMCTSsol> children = new ArrayList<EMCTSsol>(6);
//        for (EMCTSsol child: parent.getChildren()){
//            children.add(child);
//        }
//
//        Individual child;
//
//        if (this.paramsHelper.getIntValue("genetic_operator") == MUTATION_ONLY) {
//            //child = parent.getPopulation()[this.randomGenerator.nextInt(parent.getPopulation().get_length())].copy();
//
//        }
//        if (this.paramsHelper.getIntValue("genetic_operator") != CROSSOVER_ONLY) {
//            mutationClass.findGenesToMutate();
//        }
//
//        return children;
//    }


    @Override
    public Solution mutate(Solution p){

        EMCTSsol parent = (EMCTSsol)p;

        //EMCTSsol[] children = new EMCTSsol[parent.getChildren().size()];
        Individual individual = new Individual(parent.getPopulation().get_length(), this.randomGenerator, getAvailableActionsInArrayList().size());
        EMCTSsol child = new EMCTSsol();

        for (int i =0; i < parent.getPopulation().get_length(); i++) {
            if (this.paramsHelper.getIntValue("genetic_operator") == MUTATION_ONLY) {
                individual.set_action(i, parent.getPopulation().get_actions()[this.randomGenerator.nextInt(parent.getPopulation().get_length())]);
            }
            if (this.paramsHelper.getIntValue("genetic_operator") != CROSSOVER_ONLY) {
                mutationClass.findGenesToMutate();
            }
        }

        child.setPopulation(individual);

        return child;
    }

    @Override
    public void combine_and_sort_population(Solution offspring, Solution p){
        int startIdx = 0;

        EMCTSsol child = (EMCTSsol)offspring;
        EMCTSsol parent = (EMCTSsol)p;


        // Make sure we have enough individuals to choose from for the next population
       // if (parent.getChildren().size() < parent.getPopulation().get_length()) params.keep_parents_next_gen = true;
       // {
            //&& params.keep_parents_next_gen
            if (this.paramsHelper.getBooleanValue("elitism")) {
                // First no_elites individuals remain the same, the rest are replaced
                startIdx = 1;
            }
        //}
        //for (Individual i : population) {
       //     evaluate(i, null, params.evaluate_update);
       // }

            // If we should keep best individuals of parents + offspring, then combine array
        //offspring = Utilities.add_array_to_array(population, offspring, startIdx);
       //Arrays.sort(offspring, Comparator.reverseOrder());

        // Combine population with offspring, we keep only best individuals. If parents should not be kept, new
        // population is only best POP_SIZE offspring individuals.
        //int nextIdx = 0;
        //for (int i = startIdx; i < params.population_size; i++) {
        //    population[i] = offspring[nextIdx].copy();
        //    nextIdx ++;
        //}

       // if (this.paramsHelper.getBooleanValue("elitism")) {
            //If parents were kept to new generation and we had elites, population needs sorting again
       //     Arrays.sort(population, Comparator.reverseOrder());
      //  }





    }



    }

