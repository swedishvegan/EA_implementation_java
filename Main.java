import ea.Population;
import ea.Individual;
import ea.Selector;
import implementations.ProportionalSelector;
import implementations.AckleyIndividual;

// This file is supposed to provide a short demo of how one would
// use my EA. Basically, there are three components: (1) the
// Population class, (2) the Selector interface (must be implemented
// by the user), and (3) the Individual interface (must also be
// implemented by the user). Feel free to go look in the source code;
// I commented everything to make it readable.

public class Main {
	
	protected final static int POP_SIZE = 500;
	protected final static int TRIALS = 10;
	protected final static int MAX_GENERATIONS = 400;
	protected final static double MUTATION_RADIUS_DECAY = 0.98;
	
	public static void main(String[] args) {
		
		// The goal is to find the global minimum of Ackley's function.
		// We know that this value is zero, so this program serves to
		// test & make sure that my EA code is working properly.
		
		double[] results = new double[TRIALS];
		
		for (int trial = 0; trial < TRIALS; trial++) {
			
			// First we initialize all the individuals.
			Individual[] individuals = new Individual[POP_SIZE];
			for (int i = 0; i < POP_SIZE; i++) individuals[i] = new AckleyIndividual();
			
			// Create population and selector.
			Population population = new Population(individuals);
			Selector selector = new ProportionalSelector();
			
			// Reset AckleyIndividual mutation radius for each trial.
			AckleyIndividual.resetMutationRadius();
			
			for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
				
				population.runGeneration(selector);
				AckleyIndividual.decayMutationRadius(MUTATION_RADIUS_DECAY);
				
			}
			
			results[trial] = -population.maxFitness();
			
		}
		
		System.out.println("Results....");
		for (int trial = 0; trial < TRIALS; trial++) System.out.println("\tTrial " + Integer.toString(trial + 1) + "/" + Integer.toString(TRIALS) + ": optimum = " + Double.toString(results[trial]));
		
	}
	
}
