package it.polito.policytoollib.test.policy;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;

import org.junit.Test;

public class PolicyTest {

	@Test
	public void policy(){
		PolicyImpl policyImpl = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test");
	}
	
	@Test
	public void clonePolicy() throws NoExternalDataException, IncompatibleExternalDataException, DuplicateExternalDataException, UnsupportedSelectorException{
		PolicyImpl policy = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test");
		LinkedHashMap<String, Selector> selectors = new LinkedHashMap<String, Selector>();
		policy.insertRule(new GenericRule(FilteringAction.ALLOW, new ConditionClause(selectors), "Test2"),1);
		
		
		Policy policy_clone = policy.policyClone();
		
		for(GenericRule rule:policy.getRuleSet())
			assertFalse(policy_clone.getRuleSet().contains(rule));
	}
}
