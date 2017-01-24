package it.polito.policytoollib.rule.selector.impl;


import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.util.RealBitSet;


public class TosValueSelector extends ExactMatchSelectorImpl {

	private static String [] BitValue = {"0000","0001","0010","0100","1000","1111"};  
	private static String [] StringValue = {"Default","Minimize Monetary Cost","Maximize Reliability",
											"Maximize Throughput","Minimize Delay","Maximize Security"};
	
	private int ElementNumber=BitValue.length;
	private static String selName="Tos Value";
	
	
	public TosValueSelector(){
		ranges = new RealBitSet(ElementNumber);
	}


			
	public void addRange(Object Value) throws InvalidRangeException {

	}
	
	public void addRange(int i) throws InvalidRangeException{
		if (i<0 || i>= ElementNumber)
			throw new InvalidRangeException("Value: "+i);
		ranges.set(i);
	}
	
	public void addRange(String Value) throws InvalidRangeException {
		int value=0;
		boolean found=false;
		if (Value.length()==4){
			for (;value<BitValue.length && !found;value++)
				if (BitValue[value].equals(Value))
					found=true;				
		} else if (Value.length()>4) {
			for (;value<StringValue.length && !found;value++)
				if (StringValue[value].equals(Value))
					found=true;	
		} else throw new InvalidRangeException("Value: "+value);
		//value-- ??
		if (found)
			ranges.set(value);
		else throw new InvalidRangeException("Value: "+value);
	}
	
	public TosValueSelector selectorClone() {
		TosValueSelector pid = new TosValueSelector();
		pid.ranges = (RealBitSet)ranges.clone(); 
		return pid;
	}
	
	public String toString(){
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		//int bitSet=0;
		String str="";
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str + "["+StringValue[i]+"-"+BitValue[i]+"] ";
		}
		return str;
		
	}

	public int getElementsNumber() {
		return ElementNumber;
	}
	
	public static int getElementNumber() {
		return BitValue.length;
	}
	
	

	@Override
	public String toSimpleString() {
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		//int bitSet=0;
		String str="";
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str +BitValue[i]+";";
		}
		return str;
		
	}
	
	@Override
	public String toSquidString() {
		String str="";
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str +BitValue[i]+";";
		}
		return str;
	}

	@Override
	public String getName() {
		return selName;
	}




	@Override
	public String[] getPossibleValues() {
		return StringValue;
	}


}
