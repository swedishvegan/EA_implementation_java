package implementations;

import ea.Individual;

public class AckleyIndividual implements Individual {
	
	// Just declaring some constants that will be used later	
	protected final static double PI = 3.14159;
	protected final static double E = 2.71828;
	protected final static int DIMENSION = 30;
	protected final static double MUTATION_START_RADIUS = 10.0;
	protected final static double MUTATION_PROBABILITY = 0.1;
	protected final static double CROSSOVER_PROBABILITY = 0.5;
	
	// Individual's representation ("genotype")
	protected double[] x;
	
	// Mutation radius -- applies to ALL ackley individuals.
	protected static double MUTATION_RADIUS = MUTATION_START_RADIUS;
	
	public static void decayMutationRadius(double decay) { MUTATION_RADIUS *= decay; }
	public static void resetMutationRadius() { MUTATION_RADIUS = MUTATION_START_RADIUS; }
	
	// You don't want the fitness() function to re-calculate the
	// fitness value every time it is called. Therefore, what I
	// do is store the value in a variable, and keep a boolean
	// flag indicating when the fitness value needs an update
	// (i.e. when the genotype has changed).
	protected double ackleyValue;
	protected boolean fitnessNeedsUpdate = true;
	
	public AckleyIndividual() {
		
		// Initialize each entry in the vector x to a value in
		// the range [-DIMENSION, DIMENSION].
		
		x = new double[DIMENSION];
		for (int i = 0; i < DIMENSION; i++) x[i] = (Math.random() - 0.5) * 2.0 * (double)DIMENSION;
		
	}
	
	protected AckleyIndividual(double[] genome) { x = genome; }
	
	@Override
	public double fitness() {
		
		// Note that we return the negative of the fitness value
		// in order to make it a maximization problem.
		
		if (!fitnessNeedsUpdate) return -ackleyValue;
		
		// Computation of Ackley's function.
		double sum1 = 0.0, sum2 = 0.0;
		for (int i = 0; i < DIMENSION; i++) { sum1 += x[i] * x[i]; sum2 += Math.cos(2.0 * PI * x[i]); }
		sum1 = -0.2 * Math.sqrt(sum1 / (double)DIMENSION); sum2 /= (double)DIMENSION;
		ackleyValue = -20.0 * Math.exp(sum1) - Math.exp(sum2) + 20.0 + E;
		
		fitnessNeedsUpdate = false;
		return -ackleyValue;
		
	}

	@Override
	public void mutate() {
		
		// Uniform random mutation within mutation radius.
		// MUTATION_PROBABILITY is the probability that any
		// given entry in this individual's genotype will be
		// mutated.
		
		for (int i = 0; i < DIMENSION; i++) {
			
			if (Math.random() <= MUTATION_PROBABILITY) {
				
				double r = (Math.random() - 0.5) * 2.0 * MUTATION_RADIUS;
				x[i] = clamp(x[i] + r, -(double)DIMENSION, (double)DIMENSION);
				
				// We flag the fitness to be recalculated
				// since the genotype was changed.
				fitnessNeedsUpdate = true;
				
			}	
		}	
	}

	@Override
	public Individual crossover(Individual indiv) {
		
		// There is a CROSSOVER_PROBABILITY chance of
		// crossover occurring; if it does not occur then
		// one of the parents is copied at random to be
		// the child.
		
		if (Math.random() <= CROSSOVER_PROBABILITY) {
			
			AckleyIndividual aindiv = (AckleyIndividual)indiv;
			double[] childGenome = new double[DIMENSION];
			
			for (int i = 0; i < DIMENSION; i++) {
				
				double alpha = Math.random();
				childGenome[i] = mix(x[i], aindiv.x[i], alpha);
				
			}
			
			return new AckleyIndividual(childGenome);
			
		}
		else if (Math.random() <= 0.5) return this;
		else return indiv;
		
	}
	
	// Helper methods
	protected double clamp(double x, double min, double max) { if (x < min) x = min; if (x > max) x = max; return x; }
	protected double mix(double start, double end, double t) { return end * t + start * (1.0 - t); }
	
}
