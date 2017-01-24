package it.polito.policytoollib.rule.selector;

import it.polito.policytoollib.exception.rule.InvalidRangeException;

import java.util.BitSet;


public interface ExactMatchSelector extends Selector {

 /**
   * Adds a Range into the Selector.
   *
   * @param Value of the Range
   */	
	public void addRange(Object Value) throws InvalidRangeException;
	
	public BitSet getPointSet();
	
	public int getElementsNumber();
	
	
	public String[] getPossibleValues();
}
