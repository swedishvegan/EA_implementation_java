import ea.Individual;
import ea.Selector;
import implementations.ESPopulation;
import implementations.ESSelector;
import implementations.ESBasic;

// This file is supposed to provide a short demo of how one would
// use my EA. Basically, there are three components: (1) the
// Population class, (2) the Selector interface (must be implemented
// by the user), and (3) the Individual interface (must also be
// implemented by the user). Feel free to go look in the source code;
// I commented everything to make it readable.

public class ESMain {
	
	protected final static int POP_SIZE = 250;
	protected final static int TRIALS = 10;
	protected final static int MAX_GENERATIONS = 10;
	
	public static void main(String[] args) {
		
		//the goal is to find the global minimum of ESBasic's function
		//the attempt is to test out that the features of the program are understood
		
		
		double[] results = new double[TRIALS];
		
		for (int trial = 0; trial < TRIALS; trial++) {
			
			// First we initialize all the individuals.
			Individual[] individuals = new Individual[POP_SIZE];
			for (int i = 0; i < POP_SIZE; i++) {individuals[i] = new ESBasic();}
			
			// Create population and selector.
			ESPopulation population = new ESPopulation(individuals);
			ESSelector selector = new ESSelector();
			
			
			for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
				
				population.runGeneration(selector);
				
			}
			
			results[trial] = population.minFitness();
			
		}
		
		System.out.println("Results....");
		for (int trial = 0; trial < TRIALS; trial++) System.out.println("\tTrial " + Integer.toString(trial + 1) + "/" + Integer.toString(TRIALS) + ": optimum = " + Double.toString(results[trial]));
		
	}
	
}
