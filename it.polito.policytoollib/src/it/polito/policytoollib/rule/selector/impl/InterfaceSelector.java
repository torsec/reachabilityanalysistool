package it.polito.policytoollib.rule.selector.impl;

import java.util.LinkedList;
import java.util.List;

import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.util.RealBitSet;


public class InterfaceSelector extends ExactMatchSelectorImpl {
	
	public String [] interfaceNames;
	private int MAX_VALUE=0,MIN_VALUE=0;
	private static String selName="Interface";

	public InterfaceSelector(String[] interfaceNames) {
		if(interfaceNames==null || interfaceNames.length==0)
		{
			this.ranges=new RealBitSet();
			return;
		}
		this.ranges=new RealBitSet(interfaceNames.length);
		this.interfaceNames = interfaceNames;
		MAX_VALUE=interfaceNames.length-1;
	}
	
	public InterfaceSelector(String[] interfaceNames, InterfaceSelector s) {
		this.interfaceNames = interfaceNames;
		this.ranges=new RealBitSet(interfaceNames.length);
		MAX_VALUE=interfaceNames.length-1;
		for(String selectedItf : s.getSelectedInterfaces())
			for(int i=0; i<interfaceNames.length; i++)
				if(interfaceNames[i].equals(selectedItf))
				{
					ranges.set(i);
					break;
				}
	}

	public String[] getInterfaceName() {
		return interfaceNames;
	}

	@Override
	public void addRange(Object Value) throws InvalidRangeException {
		if (Value instanceof java.lang.String)
			addRange((String)Value);
		else if (Value instanceof java.lang.Integer)
			addRange((Integer)Value);
		else throw new InvalidRangeException();
	}
	
	public void addRange(String value) throws InvalidRangeException{
		boolean stop = false;
		int i=0;
		
		if (value.equalsIgnoreCase("any")){
			for(i=0;i<=MAX_VALUE;i++)
				ranges.set(i);
			return;
		}
		
		for (i=0;i<interfaceNames.length && !stop;i++) {
			if (value.equalsIgnoreCase(interfaceNames[i]))
				stop = true;
		}
		if (stop) {
			addRange(--i);
			
		} else {
			int val = Integer.parseInt(value);
			
			if (val<=MAX_VALUE && val>=MIN_VALUE)
				addRange(val);
			else throw new IllegalArgumentException();
		}

	}
	
	public void addRange(int value) throws InvalidRangeException{
		if (value<MIN_VALUE || value>MAX_VALUE){
			System.out.println(value);
			throw new InvalidRangeException("Value: "+value);
		}
		
		ranges.set(value);
	}

	public int getMAX_VALUE() {
		return MAX_VALUE;
	}

	public int getMIN_VALUE() {
		return MIN_VALUE;
	}

	@Override
	public int getElementsNumber() {
		return MAX_VALUE+1;
	}

	@Override
	public Selector selectorClone() {
		InterfaceSelector is = new InterfaceSelector(this.interfaceNames);
		is.ranges = (RealBitSet)ranges.clone(); 
		return is;
	}

	@Override
	public String toSimpleString() {
		return toString();
	}
	
	@Override
	public String toString(){
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		//int bitSet=0;
		String str="";
		
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str + "["+i+"-"+interfaceNames[i]+"] ";
		 }
		return str;
	}

	@Override
	public String toSquidString() {
		return toString();
	}

	@Override
	public String getName() {
		return selName;
	}


	@Override
	public String[] getPossibleValues() {
		return interfaceNames;
	}
	
	public List<String> getSelectedInterfaces() {
		List<String> list = new LinkedList<String>();
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1))
		{
			// operate on index i here
			list.add(interfaceNames[i]);
			if (i == Integer.MAX_VALUE)
				break; // or (i+1) would overflow
		}
		return list;
	}
}
