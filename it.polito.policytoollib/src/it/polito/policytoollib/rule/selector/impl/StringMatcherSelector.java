package it.polito.policytoollib.rule.selector.impl;

import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;

import java.util.BitSet;
import java.util.HashSet;


public class StringMatcherSelector implements ExactMatchSelector {

	private static String selname="String Matcher";
	private HashSet<String> values;
	private String label;
	
	boolean negated=false;

	public StringMatcherSelector(){
		values = new HashSet<String>();
	}
	@Override
	public void addRange(Object Value) throws InvalidRangeException {
		if (Value instanceof java.lang.String)
			addRange((String)Value);
		else throw new InvalidRangeException();
		
	}
	
	public void addRange(String Value) throws InvalidRangeException {
		values.add(Value);
	}

	@Override
	public BitSet getPointSet() {
		return null;
	}

	@Override
	public void complement() {
		negated = !negated;
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
		
			HashSet<String> result = new HashSet<String>();
		
			if (this.negated == ((StringMatcherSelector)s).negated){
				HashSet<String> out=values, in=((StringMatcherSelector)s).values;
				
				if (values.size()<((StringMatcherSelector)s).values.size()){
					out = ((StringMatcherSelector)s).values;
					in = values;
				}
				
				for (String s1 : out)
					if (in.contains(s1)) //verificare se funziona
						result.add(s1);
						
			}
			
			//values.clear();
			values = result;
			
			//TODO valutare comportamento se sono negati
		
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public boolean isEquivalent(Selector s) throws IllegalArgumentException {
		boolean found=false;
		if (this.negated == ((StringMatcherSelector)s).negated && values.size()==((StringMatcherSelector)s).values.size()){
			
			for (String s1 : values){
				found = false;
				if (((StringMatcherSelector)s).values.contains(s1)) //verificare se funziona
					found = true;
				if (!found)
					break;
			}
					
		}
		return found;
	}

	@Override
	public boolean isFull() {
		System.err.println("Not allowed for this selector");
		return false;
	}

	@Override
	public boolean isIntersecting(Selector s) throws IllegalArgumentException {
		
		
		if (this.negated == ((StringMatcherSelector)s).negated){
			HashSet<String> out=values, in=((StringMatcherSelector)s).values;
			
			if (values.size()<((StringMatcherSelector)s).values.size()){
				out = ((StringMatcherSelector)s).values;
				in = values;
			}
			
			for (String s1 : out)
				if (in.contains(s1)) //verificare se funziona
					return true;
					
		}
		
		return false;
	}

	@Override
	public boolean isSubset(Selector s) throws IllegalArgumentException {
		boolean found=false;
		if (this.negated == ((StringMatcherSelector)s).negated && values.size()<((StringMatcherSelector)s).values.size()){
			
			for (String s1 : values){
				found = false;
				if (((StringMatcherSelector)s).values.contains(s1)) //verificare se funziona
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
		if (this.negated == ((StringMatcherSelector)s).negated && values.size()<=((StringMatcherSelector)s).values.size()){
			
			for (String s1 : values){
				found = false;
				if (((StringMatcherSelector)s).values.contains(s1)) //verificare se funziona
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
	public StringMatcherSelector selectorClone() {
		StringMatcherSelector sm = new StringMatcherSelector();
		
		for (String s : values)
			sm.values.add(s);
		
		sm.setLabel(this.getLabel());
		return sm;
	}

	@Override
	public void setMinus(Selector s) throws IllegalArgumentException {
		if (this.negated == ((StringMatcherSelector)s).negated)
			for (String s1 : ((StringMatcherSelector)s).values)
				values.remove(s1);
	}

	@Override
	public String toSimpleString() {
		StringBuffer sb = new StringBuffer();
		
		for (String s : values){
			sb.append(s);
			sb.append(";");
		}
		
		return sb.toString();
	}
	
	@Override
	public String toSquidString() {
		StringBuffer sb = new StringBuffer();
		
		for (String s : values){
			sb.append(s);
			sb.append(" ");
		}
		
		return sb.toString();
	}

	@Override
	public void union(Selector s) throws IllegalArgumentException {
		if (this.negated == ((StringMatcherSelector)s).negated)
			for (String s1 : ((StringMatcherSelector)s).values)
				values.remove(s1);
		
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(selname);
		sb.append(": ");
		
		for (String s : values){
			sb.append(s);
			sb.append("; ");
		}
		
		return sb.toString();
	}
	
	public String[] toStringVector() {
		
		return (String[]) values.toArray(new String[values.size()]);
	}

	@Override
	public String getLabel() {
		if (label!=null)
			return label;
		return "";
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
		
	}
	@Override
	public int getElementsNumber() {
		return -1;
	}
	
	public int getFirstAssignedValue() {
		if (values!=null)
			if(values.size()>0)
				return values.iterator().next().hashCode();
		
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
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String[] getPossibleValues() {
		return (String[]) values.toArray();
	}

}
