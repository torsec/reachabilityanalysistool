package it.polito.policytoollib.policy.anomaly.utils;

import it.polito.policytoollib.exception.policy.DuplicatedRuleException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.resolution.ResolutionComparison;
import it.polito.policytoollib.rule.impl.GenericRule;

import java.util.Comparator;


public class RuleComparator implements Comparator<GenericRule>{
	private ResolutionStrategy resolver;
	
	public RuleComparator(Policy policy) {
		this.resolver = policy.getResolutionStrategy();
	}
	
	@Override
	public int compare(GenericRule r1, GenericRule r2) {
		ResolutionComparison comp = null;
		try {
			comp = resolver.compare(r1, r2);
		} catch (NoExternalDataException e) {
			e.printStackTrace();
		} catch (DuplicatedRuleException e) {
			e.printStackTrace();
		} catch (UnmanagedRuleException e) {
			e.printStackTrace();
		}
		if( comp == ResolutionComparison.UNIVERSALLY_LESS) return 1;
		else if(comp == ResolutionComparison.UNIVERSALLY_GREATER) return -1;
		else return 0;
	}
}