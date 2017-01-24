package it.polito.policytool.ui.view.ProjectExplorer;

import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.utils.PolicyType;

import java.util.Collection;

public class NamedSet {
	
	private PolicyType policyType;
	private Collection<Policy> set;
	
	public NamedSet(PolicyType policyType, Collection<Policy> set){
		this.policyType = policyType;
		this.set = set;
	}

	public PolicyType getPolicyType(){
		return this.policyType;
	}
	
	public Collection<Policy> getSet(){
		return this.set;
	}
}
