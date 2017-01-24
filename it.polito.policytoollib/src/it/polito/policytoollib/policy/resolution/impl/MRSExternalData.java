package it.polito.policytoollib.policy.resolution.impl;

import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;

public class MRSExternalData implements Cloneable{
	
	protected int internalPriority;
	
	protected SetEnum set;
	
	public MRSExternalData(int priority, SetEnum set){
		internalPriority = priority;
		this.set = set;
	}
	
	public MRSExternalData(String data) throws IncompatibleExternalDataException{
		try {
			String datas[] = data.split(" ");
			this.set = SetEnum.valueOf(datas[0]);
			internalPriority = Integer.decode(datas[2]);
		} catch (Exception e) {
			throw new IncompatibleExternalDataException();
		}
	}

	public int getInternalPriority() {
		return internalPriority;
	}

	public SetEnum getSet() {
		return set;
	}
	
	public MRSExternalData clone(){
		
		return new MRSExternalData(internalPriority, set);
	
	}
	
	public String toString(){
		return this.set+" prio: "+internalPriority;
	}
	
		
}
