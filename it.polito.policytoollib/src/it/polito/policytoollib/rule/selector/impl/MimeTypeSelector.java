package it.polito.policytoollib.rule.selector.impl;


import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.util.RealBitSet;



public class MimeTypeSelector extends ExactMatchSelectorImpl {
	
	private static String selName="Protocol Type";
	private static int MIN_VALUE=0;
	private static int MAX_VALUE=MimeType.value.length-1;

	public static int getMIN_VALUE() {
		return MIN_VALUE;
	}

	public static int getMAX_VALUE() {
		return MAX_VALUE;
	}

	public MimeTypeSelector(){
		ranges = new RealBitSet(MimeType.value.length+1);
	}
	
	@Override
	public void addRange(Object Value) throws InvalidRangeException {
		if (Value instanceof java.lang.String)
			addRange((String)Value);
		else throw new InvalidRangeException();
		
	}
	
	
	public void addRange(String Value){
		boolean stop = false;
		int i=0;
		for (i=0;i<=MimeType.value.length && !stop;i++) {
			if (Value.equalsIgnoreCase(MimeType.value[i]))
				stop = true;
		}
		if (stop) {
			ranges.set(--i);;
			
		} else throw new IllegalArgumentException("Not found: "+Value);
	}
	
	public void addRange(int value){
//		boolean stop = false;
		if (0 < value || value>= MimeType.value.length) {
			ranges.set(value);		
		} else throw new IllegalArgumentException();

	}


	@Override
	public String getName() {
		return selName;
	}

	@Override
	public MimeTypeSelector selectorClone() {
		MimeTypeSelector mts = new MimeTypeSelector();
		mts.ranges = (RealBitSet) ranges.clone();
		
		mts.setLabel(this.getLabel());
		return mts;
	}

	@Override
	public String toSimpleString() {
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "any";

		StringBuffer sb = new StringBuffer();
		
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
				sb.append(MimeType.value[i]);
				sb.append(";");
			}
		 
		return sb.toString();
	}
	
	@Override
	public String toSquidString() {
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "any";

		StringBuffer sb = new StringBuffer();
		
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
				sb.append(MimeType.value[i]);
				sb.append(" ");
			}
		 
		return sb.toString();
	}
	
	public String toString(){
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "any";

		StringBuffer sb = new StringBuffer();
		
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
				sb.append(MimeType.value[i]);
				sb.append(", ");
			}
		 
		return sb.toString();
	}

	@Override
	public int getElementsNumber() {
		return MimeType.value.length;
	}


	@Override
	public String[] getPossibleValues() {
		return MimeType.value;
	}

}
