package implementations;

import ea.Individual;
import ea.Population;
//import ea.Selector;
// ESPopulation class.

public class ESPopulation extends Population{
	
	//current two populations in case mulambda is used instead of mu+lambda
	protected Individual[] pop;
	protected Individual[] children;
	
	protected Individual[] pop_temp;
	protected int popsize;
	protected int gen;
	protected double maxFit;
	protected double avgFit;
	//protected int bestIndiv;
	protected int bestParent;
	protected int bestChild;
	
	protected static final int numoffspring = 7;
	protected static final boolean muplus = false;
	
	
	// Constructor takes an array of Individuals
	// as an initial population. Please make all
	// the elements in the Individuals array the
	// same class type; otherwise, the algorithm
	// will attempt to do inter-species breeding
	// and a runtime error will most likely occur.
	//@Override
	public ESPopulation(Individual[] population) {
		super(population);
		
		//probably unnecessary 
		popsize = population.length;
		
		gen = 0;
		pop = new Individual[popsize];
		for (int i = 0; i < popsize; i++) pop[i] = population[i];
		updateStats();
		//end probably unnecessary
		
		children = new Individual[popsize*numoffspring];
		pop_temp = new Individual[popsize*numoffspring];
		
		
	}
	
	// Simulates one generation using the parent
	// selection mechanism specificed by selector.
	public void runGeneration(ESSelector selector) {
		
		selector.update(this);
		repopulate(selector);
		updateStats();
		gen++;
		
	}
	
	public Individual at(int index) { return pop[index]; }
	
	public int size() { return popsize; }
	
	public int generation() { return gen; }
	
	public double maxFitness() { return maxFit; }
	
	public double avgFitness() { return avgFit; }
	
	public int numOffspring() {return numoffspring; }
	
	
	//TODO - how is the best individual chosen continuously? the list isn't sorted
	//TODO - go over method and implementation, make sure it works
	//@Override - this should be inherited from population, but it is not
	public Individual getBestIndividual() { 
		if (!muplus){return children[bestChild];}
		else{
			double childFit = pop[bestParent].fitness();
			double parentFit = children[bestChild].fitness();
			if (childFit > parentFit){return children[bestChild];}
			else {return pop[bestParent];}
		}
	}
	
	public Individual getBestParent() { return pop[bestParent]; }
	public Individual getBestChild() { return children[bestChild]; }
	
	//@Override - this should be inherited from population, but it is not
	protected void repopulate(ESSelector selector) {
		
		//crossover always exists, but may frequently return the main parent in ES
		for (int i = 1; i < popsize; i++){
			for (int j = 0; j < numoffspring; ++j){
				//just use i as parent 1
				int parent2_idx = selector.select();
				children[i*numoffspring + j] = pop[i].crossover(pop[parent2_idx]);
				
				children[i*numoffspring + j].mutate();				
			}
		}
		updateChildStats();
		
		double childFit;
		double parentFit;
		Individual bestChild;
		Individual bestParent;
		
		//repopulate based on muplus or lambda selection
		
		
		for (int i=0; i < popsize; i++){
			pop_temp[i] = getBestIndividual();
		}
		
		pop = pop_temp;
		
		updateStats();
	}
	
	protected void updateChildStats() {
		
		maxFit = children[0].fitness();
		bestChild = 0;
		for (int i = 1; i < popsize*numoffspring; i++) {
			
			if (children[i].fitness() > maxFit) {
				
				maxFit = children[i].fitness();
				bestChild = i;
				
			}
			
		}
		
	}
	
	protected void updatePopStats() {
		maxFit = pop[0].fitness();
		avgFit = 0.0;
		bestParent = 0;
		
		for (int i = 1; i < popsize; i++) {
			
			if (pop[i].fitness() > maxFit) {
				
				maxFit = pop[i].fitness();
				bestParent = i;
				
			}
			
			avgFit += pop[i].fitness();
			
		}
		
		avgFit /= (double)popsize;
	}
	
	//@Override
	protected void updateStats() {
		updatePopStats();
		
		if (children[0] != null) {updateChildStats();}
		
	}
	
}
