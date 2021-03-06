package it.polito.policytoollib.rule.selector.impl;

import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;

import java.util.BitSet;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.OtherOperations;
import dk.brics.automaton.RegExp;



public class DomainSelector implements ExactMatchSelector {
	
	protected Automaton automata;
	protected static String selName = "Domain Selector";
	private String label;
	
	public Automaton getAutomaton(){
		if (automata!=null)
			return automata.clone();
		return new Automaton();
	}
	
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
	
	public DomainSelector(){
	}
	
	public void addRange(Object regexp) throws InvalidRangeException {
		try {
			String s = (String) regexp;
			addRange(s);
		} catch (ClassCastException e){
			throw new InvalidRangeException("Not a String");
		}	
	}
	
	public void addRange(String regexp) throws InvalidRangeException {
		
		char [] str = regexp.toCharArray();
		
		StringBuffer sb = new StringBuffer();
		sb.append(str[0]);
		
		for (int i=1;i<str.length;i++){
			if (str[i]=='.')
				sb.append("\\");
			sb.append(str[i]);
		}
		regexp = sb.toString();
		
		if (automata == null)
			automata = (new RegExp(regexp)).toAutomaton();
		else automata = BasicOperations.union(automata,(new RegExp(regexp)).toAutomaton());
		automata.determinize();
		
	}

	//@Override
	//TODO
/*	public BitSet getPointSet() {
		return null;
	}*/

	@Override
	public void complement() {
		automata = BasicOperations.complement(automata);
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
		automata = (new RegExp("")).toAutomaton();
	}

	@Override
	public String getName() {
		return selName;
	}

	@Override
	public void intersection(Selector s) throws IllegalArgumentException {
		automata = BasicOperations.intersection(automata, ((DomainSelector)s).automata);
		if (!this.label.equalsIgnoreCase(s.getLabel()))
			if(!s.getLabel().equals(""))
				label = label + " && "+s.getLabel();
	}

	@Override
	public boolean isEmpty() {
		return automata.isEmpty();
	}

	@Override
	public boolean isEquivalent(Selector s) throws IllegalArgumentException {
		return OtherOperations.equivalent(automata, ((DomainSelector)s).automata);
	}

	@Override
	public boolean isFull() {
		//TODO
		System.err.println("Not yet implemented");
		return false;
	}

	@Override
	public boolean isIntersecting(Selector s) throws IllegalArgumentException {
		return OtherOperations.intersecting(automata, ((DomainSelector)s).automata);
	}

	@Override
	public boolean isSubset(Selector s) throws IllegalArgumentException {
		return OtherOperations.subsetNotEquivalent(automata, ((DomainSelector)s).automata);
	}

	@Override
	public boolean isSubsetOrEquivalent(Selector s) throws IllegalArgumentException {
		return BasicOperations.subsetOf(automata, ((DomainSelector)s).automata);
	}

	@Override
	public long length() {
		return 0;
	}

	@Override
	public DomainSelector selectorClone() {
		DomainSelector r = new DomainSelector();
		r.automata = getAutomaton();
		r.label = label;
		return r;
	}

	@Override
	public void setMinus(Selector s) throws IllegalArgumentException {
		automata = BasicOperations.minus(automata, ((DomainSelector)s).automata);
		if (!this.label.equalsIgnoreCase(s.getLabel()))
			if(!s.getLabel().equals(""))
				label = label + " && !"+s.getLabel();
	}

	@Override
	public String toSimpleString() {
		return "to implement";
	}
	
	@Override
	public String toSquidString() {
		return "to implement";
	}

	public String toString(){
		if (automata!= null)
			return automata.toString();
		return "Empty";
	}
	@Override
	public void union(Selector s) throws IllegalArgumentException {
		automata = BasicOperations.union(automata, ((DomainSelector)s).automata);
		automata.determinize();
		if (!this.label.equalsIgnoreCase(s.getLabel()))
			if(!s.getLabel().equals(""))
				label = label + " || "+s.getLabel();
	}

	@Override
	public BitSet getPointSet() {
		return null;
	}

	@Override
	public int getElementsNumber() {
		return -1;
	}

	@Override
	public int getFirstAssignedValue() {
		return 0;
	}

	@Override
	public boolean isPoint() {
		return false;
	}

	@Override
	public void full() {
		
	}

	@Override
	public String[] getPossibleValues() {
		return null;
	}

}
