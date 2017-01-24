package it.polito.policytoollib.rule.selector.impl;


import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.util.RealBitSet;


public class DSCPSelector extends ExactMatchSelectorImpl{

	private static int MAX_VALUE=63, MIN_VALUE=0;
	private static String [] StringValue=
	{
		"CS0","","","","","","","","CS1","",
		"AF11","","AF12","","AF13","","CS2","","AF21","",
		"AF22","","AF23","","CS3","","AF31","","AF32","",
		"AF33","","CS4","","AF41","","AF42","","AF43","",
		"CS5","","","","VOICE-ADMIT","","EF PHB","","CS6","",
		"CS7","","","","","","","","","",
		"","","",""
	};
	private static String selName="DSCP";
	
	@Override
	public String getName() {
		return selName;
	}
	
	public DSCPSelector(){
		ranges = new RealBitSet(MAX_VALUE+1);
	}

	
	public void addRange(Object Value) throws InvalidRangeException {
	}
	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.SetBasedSelector#addRange(java.lang.Object)
	 */
	public void addRange(Integer value)throws InvalidRangeException{
		if (value<MIN_VALUE || value>MAX_VALUE)
			throw new InvalidRangeException("Value: "+value);
		
		ranges.set(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.SetBasedSelector#addRange(java.lang.Object)
	 */
	public void addRange(int value) throws InvalidRangeException{
		if (value<MIN_VALUE || value>MAX_VALUE)
			throw new InvalidRangeException("Value: "+value);
		
		ranges.set(value);
	}
	
	public void addRange(String Value) throws InvalidRangeException {
		int value=0;
		boolean found=false;
		if(Value.equalsIgnoreCase("EF")||Value.equalsIgnoreCase("PHB"))
		{
			value = 46;
			found = true;
		}
		else 
			for (;value<StringValue.length && !found;value++)
				if (StringValue[value].equalsIgnoreCase(Value))
					found=true;	
		if (found)
			ranges.set(value);
		else throw new InvalidRangeException("Value: "+value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.Selector#selectorClone()
	 */
	public DSCPSelector selectorClone() {
		DSCPSelector pid = new DSCPSelector();
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
			str = str + "["+i+"] ";
		}
		return str;
	}

	/**
 	* 
 	* @return the maximum value admitted
 	*/
	public static int getMAX_VALUE() {
		return MAX_VALUE;
	}

	/**
 	* 
 	* @return the minumum value admitted
 	*/
	public static int getMIN_VALUE() {
		return MIN_VALUE;
	}

	@Override
	public String toSimpleString() {
		String str="";
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str + i+";";
		}
		return str;
	}
	
	@Override
	public String toSquidString() {
		String str="";
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
			str = str + i+" ";
		}
		return str;
	}

	@Override
	public int getElementsNumber() {
		return MAX_VALUE+1;
	}

	@Override
	public String[] getPossibleValues() {
		// TODO Auto-generated method stub
		return StringValue;
	}


	
}
