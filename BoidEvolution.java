import java.util.Arrays;

import ea.Individual;
import ea.Selector;
import implementations.ProportionalSelector;
import implementations.BoidPopulation;
import implementations.BoidIndividual;
//import boidEvolution.*;


public class BoidEvolution {
    // CONSTANTS
    protected static final int POPULATION_SIZE = 5;    
    protected static final int RUNS = 1;
    protected static final int MAX_GENERATIONS = 10;
    protected final static double MUTATION_RADIUS_DECAY = 0.98; // NOT SURE IF NEEDED

    public static void main(String[] args) {
		double[] predatorResults = new double[MAX_GENERATIONS];
		double[] preyResults = new double[MAX_GENERATIONS];
		
		for (int run = 0; run < RUNS; run++) {
			
			// First we initialize all the individuals.
			Individual[] predatorBoids = new Individual[POPULATION_SIZE];
			Individual[] preyBoids = new Individual[POPULATION_SIZE];
			for (int i = 0; i < POPULATION_SIZE; i++) {
                predatorBoids[i] = new BoidIndividual();
                preyBoids[i] = new BoidIndividual();
            }
			
			// Create selector
            // create new populations
            BoidPopulation predatorPopulation = new BoidPopulation(predatorBoids);
            BoidPopulation preyPopulation = new BoidPopulation(preyBoids);
			Selector selector = new ProportionalSelector();
			
			
			for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
				
                //#################### 
                // START COEVOLUTION
                //#################### 
                
                // perform for number of trials
                for (int trial = 0; trial < BoidIndividual.getNumTrials(); trial++) {
                    // shuffle all individuals
                    predatorPopulation.shuffle();
                    preyPopulation.shuffle();


                    // simulate competitions/trials between matching predator and prey
                    for (int i = 0; i < POPULATION_SIZE; i++) {
                        /*
                        simResult = sim.simulate(predatorPopulation.getIndividual(i).getGenome(), preyPopulation.getIndividual(i).getGenome());

                        // posibly manipulatae simResult if not handled in sim.simulate()

                        // update trial fitnesses; prey and predator fitnesses are opposite
                        predatorPopulation.getIndividual(i).setTrial(trial, simResult);
                        preyPopulation.getIndividual(i).setTrial(trial, -simResult);

                        */
                        
                    }
                }


                //####################
                // END COEVOLUTION
                //####################

                // Update individual populations
				predatorPopulation.runGeneration(selector);
				preyPopulation.runGeneration(selector);

                predatorResults[generation] = predatorPopulation.maxFitness();
                preyResults[generation] = preyPopulation.maxFitness();
			}
            
            
            System.out.println("Run #" + run + " Results....");
            for (int gen = 0; gen < MAX_GENERATIONS; gen++) {
                System.out.println("\tGeneration " + Integer.toString(gen + 1) + "/" + Integer.toString(MAX_GENERATIONS) 
                    + ": predator optimum = " + Double.toString(predatorResults[gen]) 
                    + ", prey optimum = " + Double.toString(preyResults[gen]));
            }
		}
		
    }
}
