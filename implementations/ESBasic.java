package implementations;

import ea.Individual;

public class ESBasic implements Individual {
	
	// Just declaring some constants that will be used later	
	protected final static int DIMENSION = 100;
	protected final static double MIN_VAR = -DIMENSION;
	protected final static double MAX_VAR = DIMENSION;
	
	
	//at 80% mutation probability, around 6/7 children are mutated
	//this tends to mean each individual will likely continue
	//note - population contains the number of children
	protected final static double MUTATION_PROBABILITY = 0.8;
	protected final static double CROSSOVER_PROBABILITY = 0.25;
	//the learning rate of mutation
	protected final static double TAU = (MAX_VAR - MIN_VAR)*1/Math.sqrt(DIMENSION);
	//the minimum mutation
	protected final static double EPSILON = 0.025;
	
	protected final static boolean CROSS = true;
	
	// Individual's representation ("genotype")
	protected double[] vect;
	protected double sigma;
	
	// Mutation radius -- applies to ALL ESBasic individuals.
	//protected static double MUTATION_RADIUS = MUTATION_START_RADIUS;
	
	// You don't want the fitness() function to re-calculate the
	// fitness value every time it is called. Therefore, what I
	// do is store the value in a variable, and keep a boolean
	// flag indicating when the fitness value needs an update
	// (i.e. when the genotype has changed).
	protected double fitness;
	protected boolean fitnessNeedsUpdate = true;
	
	public ESBasic() {
		
		// Initialize each entry in the vector vect to a value in
		// the range [-DIMENSION, DIMENSION].
		vect = new double[DIMENSION];
		
		for(int i=0; i<DIMENSION; ++i){
			vect[i] = r_range();
			sigma = r_mut_range();
			}
		
	}
	
	protected ESBasic(double sep, double align, double coh) { 
		vect = new double[DIMENSION];
	}
	
	protected ESBasic(double [] genome, double sigma_gene) { 
		vect = genome;
		sigma = sigma_gene;
	}
	
	@Override
	public double fitness() {
		
		// Note that we return the negative of the fitness value
		// in order to make it a maximization problem.
		
		if (!fitnessNeedsUpdate) return fitness;
		
		// Computation of a specific function.
		double sum = 0.0;//, sum2 = 0.0;
		
		for (int i = 0; i < DIMENSION; i++) { sum += -Math.pow((vect[i] - 2), 2);}
		fitness = sum ;
		
		fitnessNeedsUpdate = false;
		return fitness;
		
	}
	
	//mutation of the sigma
	public void sigmaMutate() {
		if (Math.random() < 0.5){sigma -= TAU;}
		else{ sigma += TAU;}
		if (sigma < EPSILON) {sigma = EPSILON;}
	}
	
	@Override
	public void mutate() {
		
		//for ES implementation, sigma mutation should proceed before normal mutation - the best mutation genes are more likely to be passed on
		
		
		// Uniform random mutation within mutation radius.
		// MUTATION_PROBABILITY is the probability that any
		// given entry in this individual's genotype will be
		// mutated.
		double r = Math.random();
		if (r < MUTATION_PROBABILITY) {
			double mutr = Math.random();
			
			//mutate sigma before mutating the genes, it's possible sigma may not mutate
			if (mutr < MUTATION_PROBABILITY){
				sigmaMutate();
			}
			
			int mutPos = (int) Math.random()*DIMENSION;
			//positive or negative mutation
			if (Math.random() < 0.5) {vect[mutPos] = clamp(vect[mutPos] + sigma, MIN_VAR, MAX_VAR);}
			else {vect[mutPos] = clamp(vect[mutPos] - sigma, MIN_VAR, MAX_VAR);}
			
			
			
		}
		fitnessNeedsUpdate = true;
		
	}

	@Override
	public Individual crossover(Individual indiv) {
		//with ES, the crossover does not always need to happen
		//given the population, it will always be called. a clone of the calling parent should be returned
		
		// There is a CROSSOVER_PROBABILITY chance of
		// crossover occurring; if it does not occur then
		// one of the parents is copied at random to be
		// the child.
		
		if (CROSS &&( Math.random() <= CROSSOVER_PROBABILITY)) {
			
			ESBasic esbindiv = (ESBasic)indiv;
			double[] childGenome = new double[DIMENSION];
			
			for (int i = 0; i < DIMENSION; i++) {
				
				double alpha = Math.random();
				childGenome[i] = mix(vect[i], esbindiv.vect[i], alpha);
				
			}
			double childSigma = (esbindiv.sigma + sigma)/2.0;
			return new ESBasic(childGenome, childSigma);
			
		}
		else return new ESBasic(vect.clone(), sigma);
		
	}
	
	public void center_vars_simple(){
		//this function just centers the variables since all variables are based on relations between each other
		
		//TODO: for center_vars (non-simple method) include the multiplier so that the spread does not affect the ratio of variables before/after realignment
		
		//acquire the spread of the variables
		double min_dimen = vect[0];
		double max_dimen = vect[0];
		
		for (int i = 1; i < DIMENSION; ++i){
			if (vect[i] < min_dimen){min_dimen = vect[i];}
			else if (vect[i] > max_dimen){min_dimen = vect[i];}
		}
		
		double var_mid = (max_dimen - min_dimen)/2;
		double absolute_mid = (MAX_VAR-MIN_VAR)/2;
		
		//centers the values, no multiplier applied
		for (int i = 1; i < DIMENSION; ++i){
			vect[i] = vect[i] + (absolute_mid - var_mid);
		}
	}
	
	public void center_vars(){
		//this function just centers the variables since all variables are based on relations between each other
		
		//TODO: for center_vars (non-simple method) include the multiplier so that the spread does not affect the ratio of variables before/after realignment
		
		//acquire the spread of the variables
		double min_dimen = vect[0];
		double max_dimen = vect[0];
		double [] temp_vect = vect.clone();
		int pivot = 0;
		
		for (int i = 1; i < DIMENSION; ++i){
			if (vect[i] < min_dimen){min_dimen = vect[i];}
			else if (vect[i] > max_dimen){min_dimen = vect[i];}
		}
		
		double var_mid = (max_dimen - min_dimen)/2;
		double absolute_mid = (MAX_VAR-MIN_VAR)/2;
		
		//centers the values, simple multiplier applied (multiplier could make the result off-center)
		//the formula could be improved to make completely centered
		for (int i = 1; i < DIMENSION; ++i){
			//centers the value
			temp_vect[i] = vect[i] + (absolute_mid - var_mid);
			//modifies the value to be in line with ratio
			temp_vect[i] = temp_vect[i]/(vect[i]/vect[pivot]);
		}
		vect = temp_vect;
	}
	
	// Helper methods
	protected double clamp(double x, double min, double max) { if (x < min) x = min; if (x > max) x = max; return x; }
	protected double mix(double start, double end, double t) { return end * t + start * (1.0 - t); }
	protected double r_range(){return Math.random()*(MAX_VAR - MIN_VAR) + MIN_VAR;}
	protected double r_mut_range(){return Math.random()*(MAX_VAR - MIN_VAR)/2.0;}
}
