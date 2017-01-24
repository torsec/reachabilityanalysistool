package it.polito.policytoollib.policy.externaldata;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.policy.resolution.ExternalDataResolutionStrategy;

import java.util.HashMap;
import java.util.Map.Entry;


@SuppressWarnings("hiding")
public class ExternalDataManager<GenericRule, S> {

	protected HashMap<GenericRule, S> data;
	boolean unique;
	
	public boolean isUnique() {
		return unique;
	}

	public ExternalDataManager(boolean unique) {
		data = new HashMap<GenericRule, S>();
		this.unique=unique;
	}
	
	protected ExternalDataManager(HashMap<GenericRule, S> data, boolean unique) {
		this.data = data;
		this.unique = unique;
	}

	public void printExternalData()
	{
		StringBuffer buffer = new StringBuffer();
		for(GenericRule rule: data.keySet())
		{
			buffer.append(rule.toString());
			buffer.append(" -->"+data.get(rule).toString()+"\n");
		}
		System.out.println(buffer);
	}
	
	public void printExternalData(GenericRule rule)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(rule.toString());
		buffer.append(" -->"+data.get(rule).toString()+"\n");
		System.out.println(buffer);
	}
	
	/**
	 * 
	 * @param rule
	 * @param data
	 * @throws DuplicateExternalDataException 
	 */
	public void setExternalData(GenericRule rule, S externalData) throws DuplicateExternalDataException{
		if(unique)
			if(data.containsValue(externalData))
				throw new DuplicateExternalDataException();
		
		this.data.put(rule, externalData);
	}
	
	public void setExternalDataNoCheck(GenericRule rule, S externalData){
		this.data.put(rule, externalData);
	}
	
	public void clearExternalData(GenericRule rule){
		data.remove(rule);
	}
	
	public boolean isRuleManaged(GenericRule rule){
		return data.containsKey(rule);
	}
	
	public S getExternalData(GenericRule rule){
		return data.get(rule);
	}
	
	@SuppressWarnings("unchecked")
	public ExternalDataManager<GenericRule, S> cloneExternalDataManager(){
		return new ExternalDataManager<GenericRule, S>((HashMap<GenericRule, S>) data.clone(), unique);
	}
	
	public boolean isExternalDataAssociated(S externalData){
		return data.containsValue(externalData);
	}
	
	public void cloneExternalData(GenericRule from, GenericRule to) throws NoExternalDataException {
		if(!data.containsKey(from)){
			throw new NoExternalDataException();
		}
		data.put(to, data.get(from));
	}
	
	public void importFrom(ExternalDataResolutionStrategy<GenericRule, S> resolutionStrategy, GenericRule rule){
		S ed = resolutionStrategy.getExternalData(rule);
		data.put(rule, ed);
	}
	
	public void clear(){
		data.clear();
	}

	public HashMap<GenericRule, S> getData() {
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public void shiftAll(int positionsToFree) throws IncompatibleExternalDataException
	{
		HashMap<GenericRule, Integer> new_data = new HashMap<GenericRule, Integer>();
		for(Entry<GenericRule, S> e : data.entrySet())
			new_data.put(e.getKey(), ((Integer)e.getValue())+positionsToFree);
		data=(HashMap<GenericRule, S>) new_data;
	}
}
