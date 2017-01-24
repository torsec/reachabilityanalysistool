package it.polito.policytoollib.rule.selector.impl;

import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Vector;


public class IntMatcherSelector implements ExactMatchSelector {

	private HashSet<Integer> values;
	private static String selname="Integer Matcher";
	private String label;
	
	boolean negated=false;

	
	@Override
	public String getLabel() {
		if (label!=null)
			return label;
		return "";
	}

	@Override
	public void setLabel(String label) {
		this.label=label;
		
	}
	
	public IntMatcherSelector(){
		values = new HashSet<Integer>();
	}
	
	public void addRange(Integer value) throws InvalidRangeException {
		values.add(value);
	}
	
	public void addRange(String Value) throws InvalidRangeException {
		try{
			Integer value = Integer.parseInt(Value);
			values.add(value);
		} catch(NumberFormatException e)
		{
			throw new InvalidRangeException();
		}
	}
	
	@Override
	public void addRange(Object Value) throws InvalidRangeException {
		if (Value instanceof java.lang.String)
			addRange((String) Value);
		else if (Value instanceof java.lang.Integer)
			addRange((Integer) Value);
		else throw new InvalidRangeException();
		
	}
	
	public void addRange(Integer start, Integer end) throws InvalidRangeException {
		if(start>end) throw new InvalidRangeException();
		else
			for(int i=start; i<=end; i++) addRange(i);
	}
	
	public void addRange(String start, String end) throws InvalidRangeException {
		try{
			Integer intStart = Integer.parseInt(start);
			Integer intEnd = Integer.parseInt(end);
			
			addRange(intStart,intEnd);
		} catch(NumberFormatException e)
		{
			throw new InvalidRangeException();
		}
	}
	
	public void addRange(Object start, Object end) throws InvalidRangeException {
		if (start instanceof java.lang.String && end instanceof java.lang.String)
			addRange((String) start, (String) end);
		else if (start instanceof java.lang.Integer && end instanceof java.lang.Integer)
			addRange((Integer) start, (Integer) end);
		else throw new InvalidRangeException();
		
	}

	@Override
	public BitSet getPointSet() {
		return null;
	}

	@Override
	public void complement() {
		negated = !negated;
		
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

	@Override
	public void empty() {
		values.clear();
	}

	@Override
	public String getName() {
		return selname;
	}

	@Override
	public void intersection(Selector s) throws IllegalArgumentException {
			HashSet<Integer> result = new HashSet<Integer>();
		
			if (this.negated == ((IntMatcherSelector)s).negated){
				HashSet<Integer> out=values, in=((IntMatcherSelector)s).values;
				
				if (values.size()<((IntMatcherSelector)s).values.size()){
					out = ((IntMatcherSelector)s).values;
					in = values;
				}
				
				for (Integer s1 : out)
					if (in.contains(s1)) //verificare se funziona
						result.add(s1);
						
			}
			
			//values.clear();
			values = result;
			
			//TODO valutare comportamento se sono negati
			
			if (!this.label.equalsIgnoreCase(s.getLabel()))
				if(!s.getLabel().equals(""))
					label = label + " || "+s.getLabel();
		
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public boolean isEquivalent(Selector s) throws IllegalArgumentException {
		boolean found=false;
		if (this.negated == ((IntMatcherSelector)s).negated && values.size()==((IntMatcherSelector)s).values.size()){
			
			for (Integer s1 : values){
				found = false;
				if (((IntMatcherSelector)s).values.contains(s1)) //verificare se funziona
					found = true;
				if (!found)
					break;
			}
					
		}
		return found;
	}

	@Override
	public boolean isFull() {
		System.err.println("Not allowe dfor this selector");
		return false;
	}

	@Override
	public boolean isIntersecting(Selector s) throws IllegalArgumentException {
		
		
		if (this.negated == ((IntMatcherSelector)s).negated){
			HashSet<Integer> out=values, in=((IntMatcherSelector)s).values;
			
			if (values.size()<((IntMatcherSelector)s).values.size()){
				out = ((IntMatcherSelector)s).values;
				in = values;
			}
			
			for (Integer s1 : out)
				if (in.contains(s1)) //verificare se funziona
					return true;
					
		}
		
		return false;
	}

	@Override
	public boolean isSubset(Selector s) throws IllegalArgumentException {
		boolean found=false;
		if (this.negated == ((IntMatcherSelector)s).negated && values.size()<((IntMatcherSelector)s).values.size()){
			
			for (Integer s1 : values){
				found = false;
				if (((IntMatcherSelector)s).values.contains(s1)) //verificare se funziona
					found = true;
				if (!found)
					break;
			}
					
		}
		return found;
	}

	@Override
	public boolean isSubsetOrEquivalent(Selector s) throws IllegalArgumentException {
		boolean found=false;
		if (this.negated == ((IntMatcherSelector)s).negated && values.size()<=((IntMatcherSelector)s).values.size()){
			
			for (Integer s1 : values){
				found = false;
				if (((IntMatcherSelector)s).values.contains(s1)) //verificare se funziona
					found = true;
				if (!found)
					break;
			}
					
		}
		return found;
	}

	@Override
	public long length() {
		return values.size();
	}

	@Override
	public IntMatcherSelector selectorClone() {
		IntMatcherSelector sm = new IntMatcherSelector();
		
		for (Integer s : values)
			sm.values.add(s);
		
		sm.setLabel(this.getLabel());
		return sm;
	}

	@Override
	public void setMinus(Selector s) throws IllegalArgumentException {
		if (this.negated == ((IntMatcherSelector)s).negated)
			for (Integer s1 : ((IntMatcherSelector)s).values)
				values.remove(s1);
		
		if (!this.label.equalsIgnoreCase(s.getLabel()))
			if(!s.getLabel().equals(""))
				label = label + " || "+s.getLabel();
	}

	@Override
	public String toSimpleString() {
		StringBuffer sb = new StringBuffer();
		
		for (Integer s : values){
			sb.append(s);
			sb.append(";");
		}
		
		return sb.toString();
	}
	
	@Override
	public String toSquidString() {
		StringBuffer sb = new StringBuffer();
		
		for (Integer s : values){
			sb.append(s);
			sb.append(" ");
		}
		
		return sb.toString();
	}

	@Override
	public void union(Selector s) throws IllegalArgumentException {
		if (this.negated == ((IntMatcherSelector)s).negated)
			for (Integer s1 : ((IntMatcherSelector)s).values)
				values.remove(s1);
		
		if (!this.label.equalsIgnoreCase(s.getLabel()))
			if(!s.getLabel().equals(""))
				label = label + " || "+s.getLabel();
		
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(selname);
		sb.append(": ");
		
		for (Integer s : values){
			sb.append(s);
			sb.append("; ");
		}
		
		return sb.toString();
	}

	public String toRangeString() {
		StringBuffer sb = new StringBuffer();
		
		Integer[] sortedValues = new Integer[values.size()];
		sortedValues = values.toArray(sortedValues);
		Arrays.sort(sortedValues);
		for(int i=0; i<sortedValues.length; i++)
		{
			sb.append("[");
			Integer start = sortedValues[i];
			Integer end = sortedValues[i];
			for(int j=i+1; j<sortedValues.length; j++)
			{
				if(sortedValues[j]==sortedValues[j-1]+1)
					if(j==sortedValues.length-1)
					{
						end = sortedValues[j];
						i=j;
						break;
					}
					else
						continue;
				end = sortedValues[j-1];
				i=j-1;
				break;
			}
			sb.append(start);
			sb.append(",");
			sb.append(end);
			sb.append("]");
		}
		
		return sb.toString();
	}
	
	@Override
	public int getElementsNumber() {
		return -1;
	}
	
	@Override
	public int getFirstAssignedValue() {
		if (values!=null)
			if(values.size()>0)
				return values.iterator().next();
		
		return 0;
	}

	@Override
	public boolean isPoint() {
		if (values.size()==1)
			return true;
		
		return false;
	}

	@Override
	public void full() {
	}


	@Override
	public String[] getPossibleValues() {
		String[] retValue = new String[values.size()];
		int i = 0;
		for(Integer val : values)
			retValue[++i] = val.toString();
		return retValue;
	}
}
