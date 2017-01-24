package it.polito.policytoollib.rule.selector.impl;

import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.util.RealBitSet;


public abstract class ExactMatchSelectorImpl implements ExactMatchSelector {
	
	protected RealBitSet ranges;
	private String label="";
	private static String selName="Generic Exact Match Selector";
	
	//private SelectorFactory<T> factory;
	@Override
	public String getName() {
		return selName;
	}
	public boolean isPoint(){
		if (ranges!=null)
			if (ranges.cardinality()==1)
				return true;
		
		return false;
	}
	
	public int getFirstAssignedValue(){
		return ranges.nextSetBit(0);
	}
	
	public String getLabel() {
			return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public RealBitSet getPointSet(){
		return ranges;
	}
	
	protected RealBitSet getRealBitSet(){
		return ranges;
	}
	
//	public SelectorFactory<T> getFactory() {
//		return factory;
//	}
	/**
	  * Calculates the complement of the specified Selector.

	  */
	public void complement() {
		ranges.flip(0, ranges.size()-1);
		int sub=0;
		if (label.charAt(0)=='!'){
			sub++;
			if(label.charAt(0)=='(')
				sub++;
			label = label.substring(sub);
			if(label.charAt(label.length()-1)==')')
				label = (label.subSequence(0, label.length()-2)).toString();
		} else label = "!("+label+")";
			
	}

	public void empty() {
		ranges.clear();		
	}

	/**
	 * Calculates intersection between this Selector and Selector s
	 * 
	 * @param s 
	 * 		
	 * 
	 * @throws polito.conflicts.range.IllegalArgumentException
	 */
	public void intersection(Selector s) throws IllegalArgumentException {		
		ranges.and(((ExactMatchSelector) s).getPointSet());
		if (!this.label.equalsIgnoreCase(((ExactMatchSelector) s).getLabel()))
			if(!((ExactMatchSelector) s).getLabel().equals(""))
				label = label + " && "+((ExactMatchSelector) s).getLabel();
		
	}

	public boolean isSubsetOrEquivalent(Selector s) throws IllegalArgumentException {
		
		RealBitSet rclone = (RealBitSet) ranges.clone();
		rclone.andNot(((ExactMatchSelector) s).getPointSet());
		
		return rclone.isEmpty();
	}
	
	public boolean isSubset(Selector s) throws IllegalArgumentException {
		
		RealBitSet rclone = (RealBitSet) ranges.clone();
		rclone.andNot(((ExactMatchSelector) s).getPointSet());
		
		return rclone.isEmpty() && ranges.cardinality()< ((ExactMatchSelector) s).getPointSet().cardinality();
	}

/*	- a is contained (ma non uguale) b
	a.andNot(b)
	a.isEmpty() && b.cardinality() == a.cardinality()*/
	
	public boolean isEmpty() {
		return ranges.isEmpty();
	}

	public boolean isEquivalent(Selector s) throws IllegalArgumentException {
//		ExactMatchSelector ems = (ExactMatchSelector) s;
		
		RealBitSet rclone = (RealBitSet) ranges.clone();
		rclone.andNot(((ExactMatchSelector) s).getPointSet());
		
		return rclone.isEmpty() && ranges.cardinality() == ((ExactMatchSelector) s).getPointSet().cardinality();
		
	}

	public boolean isFull() {
		ranges.flip(0, ranges.size());
		
		boolean res = ranges.isEmpty();
		ranges.flip(0, ranges.size());
		
		return res;
	}

	public boolean isIntersecting(Selector  s) throws IllegalArgumentException {
		//ExactMatchSelector ems = (ExactMatchSelector) s;
		return ranges.intersects(((ExactMatchSelector) s).getPointSet());
	}

	/**
	  * Calculates the set minus between this Selector and Selector s.
	  *
	  * @param	s
	  * 	 Selector
	  * @throws polito.conflicts.range.IllegalArgumentException
	  */
	public void setMinus(Selector s) throws IllegalArgumentException {
		ranges.andNot(((ExactMatchSelector) s).getPointSet());	
		if (!this.label.equalsIgnoreCase(((ExactMatchSelector) s).getLabel()))
			if(!((ExactMatchSelector) s).getLabel().equals(""))
				label = label + " && !"+((ExactMatchSelector) s).getLabel();
	}

	/**
	 * Calculates union between this Selector and Selector s
	 * 
	 * @param s
	 * 		Selector
	 * @throws polito.conflicts.range.IllegalArgumentException
	 */	
	public void union(Selector s) throws IllegalArgumentException {
		ranges.or(((ExactMatchSelector) s).getPointSet());
		if (!this.label.equalsIgnoreCase(((ExactMatchSelector) s).getLabel()))
			if(!((ExactMatchSelector) s).getLabel().equals(""))
				label = label + " || "+((ExactMatchSelector) s).getLabel();
		
	}
	


	public long length(){
		return ranges.cardinality(); 
	}
	
	
	@Override
	public void full() {
		ranges.set(0, ranges.size(), true);
	}
	
	
}
