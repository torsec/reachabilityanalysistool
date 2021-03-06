package it.polito.policytoollib.policy.resolution.impl;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.DuplicatedRuleException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.policy.externaldata.ExternalDataManager;
import it.polito.policytoollib.policy.resolution.ExternalDataResolutionStrategy;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.resolution.ResolutionComparison;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;


public class MRSResolutionStrategy extends ExternalDataResolutionStrategy<GenericRule, MRSExternalData> {

	private static final String label = "MSR-GRS-LRS Resolution Strategy";
	
	public MRSResolutionStrategy() {
		this.priorities = new ExternalDataManager<GenericRule, MRSExternalData>(false);
	}
	

	@Override
	public Action composeActions(GenericRule r1, GenericRule r2) throws NoExternalDataException {
		if(!(priorities.isRuleManaged(r1) && priorities.isRuleManaged(r2)))
				throw new NoExternalDataException();
		
		int res = compareRules(r1, r2);
		
		switch (res) {
		case -1:
			return r1.getAction();
		
		case 1:
			return r2.getAction();
		}

		//From here res==0
		
		return r1.getAction();

	}

	

	@Override
	public Action composeActions(GenericRule[] rules) throws NoExternalDataException {
		if (rules==null)
			throw new NullPointerException();
		
		if (rules.length<=0)
			throw new Error("No rules in array");
		
		GenericRule min = rules[0];
		
		for (GenericRule r : rules) {
			if(!(priorities.isRuleManaged(r)))
				throw new NoExternalDataException();
			
			if (compareRules(min,r)==1)
				min = r;
		}
		
		return min.getAction();
	}
	
	
	@Override
	public ResolutionComparison compare(GenericRule r1, GenericRule r2) throws NoExternalDataException, DuplicatedRuleException, UnmanagedRuleException {
		
		if(!(priorities.isRuleManaged(r1) && priorities.isRuleManaged(r2)))
			throw new NoExternalDataException();

		
		int res = compareRules(r1, r2);
		
		switch (res) {
		case -1:
			return ResolutionComparison.UNIVERSALLY_GREATER;
		
		case 1:
			return ResolutionComparison.UNIVERSALLY_LESS;
		}

		//From here res==0

		return ResolutionComparison.EQUIVALENT;
	}

	@Override
	public boolean isActionEquivalent(GenericRule r1, GenericRule r2) {
		return r1.getAction().equals(r2.getAction());
	}

	@Override
	public ResolutionStrategy cloneResolutionStrategy() {
		MRSResolutionStrategy clone = new MRSResolutionStrategy();
		clone.priorities = priorities.cloneExternalDataManager();
		return clone;
	}

	@Override
	public MRSExternalData composeExternalData(GenericRule r1, GenericRule r2) throws NoExternalDataException {
		if(!(priorities.isRuleManaged(r1) && priorities.isRuleManaged(r2)))
			throw new NoExternalDataException();
		
		int res = compareRules(r1, r2);
		
		switch (res) {
		case -1:
			return priorities.getExternalData(r1).clone();
		
		case 1:
			return priorities.getExternalData(r2).clone();
		}

		//From here res==0	
		
		return priorities.getExternalData(r2).clone();	
	}


	@Override
	public MRSExternalData composeExternalData(GenericRule[] rules) throws NoExternalDataException {
		if (rules==null)
			throw new Error("No rules in array");;
		
		if (rules.length<=0)
			throw new Error("No rules in array");
		
		GenericRule min = rules[0];
		
		for (GenericRule r : rules) {
			if(!(priorities.isRuleManaged(r)))
				throw new NoExternalDataException();
			
			if (compareRules(min,r)==1)
				min = r;
		}
		
		return priorities.getExternalData(min).clone();
	}

	
	/**
	 * 
	 * @return 1 if this > data, -1 if this < data, 0 if equivalent
	 */
	private int compareRules(GenericRule r1, GenericRule r2){
		
		MRSExternalData p1 = priorities.getExternalData(r1);
		MRSExternalData p2 = priorities.getExternalData(r2);
		
		if(p1.set==p2.set)
			if(p1.internalPriority<p2.internalPriority)
				return -1;
			else if (p2.internalPriority<p1.internalPriority)
				return 1;
			else return 0;
				
//				if(r1.getAction().equals(r2.getAction()))
//				return 0;
//			else if(r1.getAction().equals(FilteringAction.DENY))
//				return -1;
//			else return 1;
		
		//Case of different set
		if(p1.set==SetEnum.MRS)
			return -1;
		
		if(p2.set==SetEnum.MRS)
			return 1;
		
		//Now we check if in the case that one rule belongs to GRS and the other one 
		//belongs to LRSi
		
		if (p1.set==SetEnum.GRS) 
			if (r2.isConditionSubset(r1))
				return 1;
			else if (r1.isConditionSubsetOrEquivalent(r2))
				return -1;
		
		if (p2.set==SetEnum.GRS) 
			if (r1.isConditionSubset(r2))
				return -1;
			else if (r2.isConditionSubsetOrEquivalent(r1))
				return 1;
		
		//DTP
		if(r1.getAction().equals(r2.getAction())) 
			return 0;	
		
		if (r1.getAction().equals(FilteringAction.DENY)) 
			return -1;
		
		return 1;
	}
	
	@Override
	public void setExternalData(GenericRule rule, MRSExternalData externalData){
		try {
			priorities.setExternalData(rule, externalData);
		} catch (DuplicateExternalDataException e) {
			//unreachable catch
		}
		
	}
	
	@Override
	public void setExternalData(GenericRule rule, String externalData)	throws IncompatibleExternalDataException {
		try {
			priorities.setExternalData(rule, new MRSExternalData(externalData));
		} catch (DuplicateExternalDataException e) {
			//unreachable catch
		}
	}

	@Override
	public MRSExternalData getExternalData(GenericRule rule) {
		return priorities.getExternalData(rule);
	}

	@Override
	public void clearExternalData(GenericRule rule) {
		priorities.clearExternalData(rule);
	}
	
	@Override
	public boolean isRuleManaged(GenericRule rule) {
		return priorities.isRuleManaged(rule);
	}


	@Override
	public String toString() {
		return label;
	}
	
	@Override
	public String getExternalDataClassName() {
		return MRSExternalData.class.getName();
	}
	
	@Override
	public boolean isExternalDataValid(String externalData) {
		try {
			new MRSExternalData(externalData);
			return true;
		} catch (IncompatibleExternalDataException e) {
			return false;
		}
	}


	@Override
	public void setExternalDataNoCheck(GenericRule rule,
			MRSExternalData externalData) {
		setExternalData(rule,externalData);
		
	}


	@Override
	public void setExternalDataNoCheck(GenericRule rule, String externalData)
			throws IncompatibleExternalDataException {
		setExternalData(rule,externalData);
		
	}
}
