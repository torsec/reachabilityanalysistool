package it.polito.policytoollib.rule.selector.impl;


import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.util.RealBitSet;


public class HTTPMethodSelector extends ExactMatchSelectorImpl {

	public static String [] values={"GET","HEAD","POST","PUT","DELETE","TRACE","CONNECT","OPTIONS","PURGE"};
	
	private static int MAX_VALUE=8, MIN_VALUE=0;
	private static String selName="HTTP Method";
	
	public HTTPMethodSelector(){
		//TODO
		//factory = ProtocolIDSelectorFactory.getInstance();
		ranges = new RealBitSet(MAX_VALUE+1);
		//System.err.println(ranges.size());
		
	}
	
	@Override
	public void addRange(Object Value) throws InvalidRangeException {
		if (Value instanceof java.lang.String)
			addRange((String)Value);
		else throw new InvalidRangeException();
		
	}
	
	public void addRange(String value) throws InvalidRangeException{
		boolean stop = false;
		int i=0;
		for (i=0;i<=MAX_VALUE && !stop;i++) {
			if (value.equalsIgnoreCase(values[i]))
				stop = true;
		}
		if (stop) {
			ranges.set(--i);
			
		} else throw new IllegalArgumentException();
	}
	
	public void addRange(int value) throws InvalidRangeException{
//		boolean stop = false;

		
		if (MIN_VALUE <= value || value>MAX_VALUE) {
			ranges.set(value);
			
		} else throw new IllegalArgumentException();
	}



	@Override
	public String getName() {
		return selName;
	}

	@Override
	public HTTPMethodSelector selectorClone() {
		HTTPMethodSelector pid = new HTTPMethodSelector();
		pid.ranges = (RealBitSet)ranges.clone(); 
		pid.setLabel(this.getLabel());
		return pid;
	}

	@Override
	public String toSimpleString() {
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		String str="";
		
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)){ 
				str = str + values[i];
				if(ranges.nextSetBit(i+1) >= 0)
					str = str + ";";
		}
		
		 
		return str;
	}
	
	@Override
	public String toSquidString() {
		String str="";
		
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) 
				str = str + " "+ values[i];
		 
		return str;
	}

	public String toString(){
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		String str="";
		
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) 
				str = str + "["+values[i]+"] ";
		 
		return str;
	}

	@Override
	public int getElementsNumber() {
		return values.length;
	}
	public static int getMAX_VALUE() {
		return MAX_VALUE;
	}

	public static int getMIN_VALUE() {
		return MIN_VALUE;
	}

	@Override
	public String[] getPossibleValues() {
		return values;
	}
}
