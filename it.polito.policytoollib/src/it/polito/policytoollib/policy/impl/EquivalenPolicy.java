package it.polito.policytoollib.policy.impl;

import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.semilattice.Semilattice;
import it.polito.policytoollib.rule.impl.GenericRule;

public class EquivalenPolicy {
	private ComposedPolicy policy;
	private CanonicalForm can;
	private Semilattice<GenericRule> sl;
	
	public EquivalenPolicy(ComposedPolicy policy, CanonicalForm can, Semilattice<GenericRule> sl){
		this.policy = policy;
		this.can = can;
		this.sl = sl;
	}

	public ComposedPolicy getPolicy() {
		return policy;
	}

	public CanonicalForm getCan() {
		return can;
	}

	public Semilattice<GenericRule> getSl() {
		return sl;
	}
	
	
}
