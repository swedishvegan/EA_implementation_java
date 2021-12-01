import ea.Population;
import ea.Individual;
import ea.Selector;
import implementations.ProportionalSelector;
import implementations.BoidIndividual;

import java.util.Random;

public class BoidEvolution {
    // CONSTANTS
    protected static final int POPULATION_SIZE = 5;    
    protected static final int RUNS = 1;
    protected static final int MAX_GENERATIONS = 10;
    protected final static double MUTATION_RADIUS_DECAY = 0.98; // NOT SURE IF NEEDED

    public static void main(String[] args) {
		double[] predatorResults = new double[RUNS];
		double[] preyResults = new double[RUNS];
		
		for (int run = 0; run < RUNS; run++) {
			
			// First we initialize all the individuals.
			Individual[] predatorBoids = new Individual[POPULATION_SIZE];
			Individual[] preyBoids = new Individual[POPULATION_SIZE];
			for (int i = 0; i < POPULATION_SIZE; i++) {
                predatorBoids[i] = new BoidIndividual();
                preyBoids[i] = new BoidIndividual();
            }
			
			// Create selector.
			Selector selector = new ProportionalSelector();
			
			// Reset BoidIndividual mutation radius for each run.
			BoidIndividual.resetMutationRadius();
			
			for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
				
                //#################### 
                // START COEVOLUTION
                //#################### 
                
                // perform for number of trials
                for (int trial = 0; trial < BoidIndividual.getNumTrials(); trial++) {
                    // shuffle all individuals
                    /*
                    shuffle(predatorBoids);
                    shuffle(preyBoids);
                    */

                    // simulate competitions between matching predator and prey
                    for (int i = 0; i < POPULATION_SIZE; i++) {
                        /*
                        simResult = sim.simulate(predatorBoids[i].getGenome(), preyBoids[i].getGenome());

                        // posibly manipulatae simResult if not handled in sim.simulate()

                        // update trial fitnesses; prey and predator fitnesses are opposite
                        predatorBoids[i].setTrial(trial, simResult);
                        preyBoids[i].setTrial(trial, -simResult);
                        
                        */
                    }
                }

                // create new populations
                Population predatorPopulation = new Population(predatorBoids);
                Population preyPopulation = new Population(preyBoids);

                //####################
                // END COEVOLUTION
                //####################

                // Update individual populations
				predatorPopulation.runGeneration(selector);
				preyPopulation.runGeneration(selector);
				BoidIndividual.decayMutationRadius(MUTATION_RADIUS_DECAY);

                predatorResults[run] = predatorPopulation.maxFitness();
                preyResults[run] = preyPopulation.maxFitness();
			}
            
            System.out.println("Run #" + run + " Results....");
            for (int gen = 0; gen < MAX_GENERATIONS; gen++) {
                System.out.println("\tGeneration " + Integer.toString(gen + 1) + "/" + Integer.toString(MAX_GENERATIONS) 
                    + ": predator optimum = " + Double.toString(predatorResults[gen]) 
                    + ", prey optimum = " + Double.toString(preyResults[gen]));
            }
		}
		
    }

        // Implementing Fisher–Yates shuffle
        public static void shuffle(Individual[] array) {
            int index;
            Individual temp;
            Random random = new Random();
            for (int i = array.length - 1; i > 0; i--) {
                index = random.nextInt(i + 1);
                temp = array[index];
                array[index] = array[i];
                array[i] = temp;
            }
        }  
}
