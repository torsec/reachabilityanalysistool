package it.polito.policytoollib.rule.selector.impl;


import java.util.BitSet;

import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.util.RealBitSet;


public class StateSelector extends ExactMatchSelectorImpl {
	
	public StateSelector(){
		this.ranges = new RealBitSet(MAX_VALUE+1);
		this.ranges.flip(MIN_VALUE, MAX_VALUE+1);
		this.valueBits = new RealBitSet(8);
		this.careBits = new RealBitSet(8);
	}

	public static String [] State={"CWR","ECE","URG","ACK","PSH","RST","SYN","FIN"};
	
	private static int MAX_VALUE=255, MIN_VALUE=0;
	private static String selName="State";
	
	private BitSet valueBits;
	private BitSet careBits;
	
	@Override
	public String getName() {
		return selName;
	}

	
	public void addRange(Object Value) throws InvalidRangeException {
		if (Value instanceof java.lang.String)
			addRange((String)Value,1);
		else throw new InvalidRangeException();
	}
	//lasciare tutte le combinazioni in cui questo flag � al valore di bitValue
	public void addRange(String value, int bitValue) throws InvalidRangeException{
		boolean stop = false; int i;
		if (value.equalsIgnoreCase("any")){
			for(i=0;i<=MAX_VALUE;i++)
				ranges.set(i);
			return;
		}		
		for (i=0;i<State.length && !stop;i++) 
			if (value.equalsIgnoreCase(State[i]))
				stop = true;
		if (stop)
		{
			int k=--i;
			careBits.set(k);
			valueBits.set(k, bitValue==1);
			for(i=0;i<MAX_VALUE+1;i++)
				if(((i>>k)&1)!=bitValue)
					ranges.clear(i);
		}
		else
		{
			int val = Integer.parseInt(value);
			
			if (val<=MAX_VALUE && val>=MIN_VALUE)
				addRange(val);
			else throw new IllegalArgumentException();
		}
	}
	
	public void unsetFlag(String flag)
	{
		boolean stop = false; int i;
		for (i=0;i<State.length && !stop;i++) 
			if (flag.equalsIgnoreCase(State[i]))
				stop = true;
		if(stop)
		{
			i--;
			this.ranges = new RealBitSet(MAX_VALUE+1);
			this.ranges.flip(MIN_VALUE, MAX_VALUE+1);
			for(int j=0;j<State.length;j++)
				if(j!=i&&careBits.get(j))
				{
					try {
						if(valueBits.get(i)) addRange(State[j],1);
						else addRange(State[j],0);
					} catch (InvalidRangeException e) {
						//unreachable catch
					}
				}
		}
	}
	
	//return value 1=bit set to true 0=bit set to false -1=bit not set
	public int getFlag(String flag)
	{
		boolean stop = false; int i;
		for (i=0;i<State.length && !stop;i++) 
			if (flag.equalsIgnoreCase(State[i]))
				stop = true;
		if (stop)
		{
			i--;
			if(!careBits.get(i)) return -1;
			if(valueBits.get(i)) return 1;
			else return 0;
		}
		return -1;
	}
	
	public int[] getFlags()
	{
		int values[] = new int[8];
		int i = 0;
		for(String s : State)
			values[i++] = getFlag(s);
		return values;
	}
	
	public StateSelector selectorClone() {
		StateSelector pid = new StateSelector();
		pid.ranges = (RealBitSet)ranges.clone(); 
		return pid;
	}
	
	public String toString(){
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		String str="";
		
		for (int i = careBits.nextSetBit(0); i >= 0; i = careBits.nextSetBit(i+1))
		{
			if(valueBits.get(i)) str = str + "["+ State[i]+" Set] ";
			else str = str + "["+ State[i]+" Unset] ";
		}
		return str;
	}

	public String toSimpleString() {
		String str="";
		
		for (int i = careBits.nextSetBit(0); i >= 0; i = careBits.nextSetBit(i+1))
		{
			if(valueBits.get(i)) str = str +State[i]+" Set";
			else str = str +State[i]+" Unset";
			if(careBits.nextSetBit(i+1) >= 0)
				str = str + ";";
		}
		return str;
	}
	
	public String toSquidString() {
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		//int bitSet=0;
		String str="";
		
		for (int i = ranges.nextSetBit(0); i >= 0; i = ranges.nextSetBit(i+1)) {
				str = str + "["+ State[i]+"] ";
		 }
		return str;
	}

	public static int getMAX_VALUE() {
		return MAX_VALUE;
	}

	public static int getMIN_VALUE() {
		return MIN_VALUE;
	}

	@Override
	public int getElementsNumber() {
		return MAX_VALUE+1;
	}



	@Override
	public String[] getPossibleValues() {
		return State;
	}
}
