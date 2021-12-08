package implementations;

//import ESPopulation;
import ea.Population;
import ea.Selector;

// ESSelector selection. 
// canonical ES selection selects any of the population with equal possibility
// the primary force driving ES improvement is the number of children

public class ESSelector implements Selector{//TODO  
	
	protected Population curPop = null;
	
	
	@Override //- TODO have ESPopulation inherit correctly
	public void update(Population pop) {
		
		curPop = pop;
		
	}
	
	@Override //- TODO have ESPopulation inherit correctly
	public int select() {
		//return a random number within the pop size
		return (int)Math.floor(curPop.size()*Math.random());
	}
	

	@Override //- TODO have ESPopulation inherit correctly
	public int size() { if (curPop == null) return 0; return curPop.size(); }
	
	
}
