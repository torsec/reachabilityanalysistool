package it.polito.policytoollib.rule.selector.impl;


import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.util.RealBitSet;


public class EthernetPrioritySelector extends ExactMatchSelectorImpl {

	private static String [] StringValue={"Best Effort","Background","Spare","Excellent Effort","Controlled Load",
		"Video < 100 ms latency and jitter","Voice < 10 ms latency and jitter","Network Control"};
	private int ElementNumber=StringValue.length;
	

	private static String selName="IEEE 802.3 Priority";
	
	@Override
	public String getName() {
		return selName;
	}

	public EthernetPrioritySelector(){
		ranges = new RealBitSet(ElementNumber);
	}
	

	public void addRange(Object Value) throws InvalidRangeException {		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.SetBasedSelector#addRange(java.lang.Object)
	 */
	public void addRange(int i) throws InvalidRangeException{
		if (i<0 || i>= ElementNumber)
			throw new InvalidRangeException("Value: "+i);
		ranges.set(i);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.SetBasedSelector#addRange(java.lang.Object)
	 */
	public void addRange(String Value) throws InvalidRangeException {
		int value=0;
		boolean found=false;
		
		for (;value<StringValue.length && !found;value++)
				if (StringValue[value].equals(Value))
					found=true;	
		if (found)
			ranges.set(value);
		else throw new InvalidRangeException("Value: "+value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.Selector#selectorClone()
	 */
	public EthernetPrioritySelector selectorClone() {
		EthernetPrioritySelector pid = new EthernetPrioritySelector();
		pid.ranges = (RealBitSet)ranges.clone(); 
		pid.setLabel(this.getLabel());
		return pid;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		//int bitSet=0;
		String str="";
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str + "["+StringValue[i]+"] ";
		}
		return str;
	}

	/**
	 * 
	 * @return The maximum number of element admitted
	 */
	public int getElementsNumber() {
		return ElementNumber;
	}

	/**
	 * 
	 * @return The maximum number of element admitted
	 */
	public static int getElementNumber() {
		return StringValue.length;
	}
	@Override
	public String toSimpleString() {
		String str="";
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str +StringValue[i]+";";
		}
		return str;
	}
	
	@Override
	public String toSquidString() {
		String str="";
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str +StringValue[i]+" ";
		}
		return str;
	}


	@Override
	public String[] getPossibleValues() {
		// TODO Auto-generated method stub
		return StringValue;
	}

}
