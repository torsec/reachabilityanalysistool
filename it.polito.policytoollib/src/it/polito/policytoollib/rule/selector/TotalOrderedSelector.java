package it.polito.policytoollib.rule.selector;

import it.polito.policytoollib.exception.rule.InvalidRangeException;

import java.math.BigInteger;


public interface TotalOrderedSelector extends Selector{

	/**
	 * Adds a Range into the Selector.
	 * 
	 * @param Value
	 * 			the punctiform range 
	 * @throws InvalidRangeException
	 */
	public void addRange(Object Value) throws InvalidRangeException;
	 
    /**
     *  Adds a Range into the Selector.
     *
     * @param Start End 
     * 		  points of the Range
     */
	public void addRange(Object Start, Object End) throws InvalidRangeException;
	
	public BigInteger[] getRanges();
	
	public String getMin();
	public String getMax();
}
