package it.polito.policytoollib.test.classifier;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.Test;

public class IntersectingRuleTest {

	
	@Test
	public void intersection() throws IncompatibleExternalDataException, DuplicateExternalDataException, InvalidRangeException, UnsupportedSelectorException {
		PolicyImpl policyImpl = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test");

		// --------------------Rule1
		LinkedHashMap<String, Selector> ss1 = new LinkedHashMap<String, Selector>();
		PortSelector ps1 = new PortSelector();
		ss1.put("PS", ps1);
		ps1.addRange(10, 20);
		ps1.addRange(30, 40);
		ConditionClause cc1 = new ConditionClause(ss1);
		GenericRule rule1 = new GenericRule(FilteringAction.ALLOW, cc1, "R1");
		policyImpl.insertRule(rule1, 1);

		// --------------------Rule2
		LinkedHashMap<String, Selector> ss2 = new LinkedHashMap<String, Selector>();
		PortSelector ps2 = new PortSelector();
		ps2.addRange(15, 35);
		ss2.put("PS", ps2);
		ConditionClause cc2 = new ConditionClause(ss2);
		GenericRule rule2 = new GenericRule(FilteringAction.ALLOW, cc2, "R2");
		policyImpl.insertRule(rule2, 2);

		// --------------------Rule3
		LinkedHashMap<String, Selector> ss3 = new LinkedHashMap<String, Selector>();
		PortSelector ps3 = new PortSelector();
		ps3.addRange(5, 5);
		ss3.put("PS1", ps3);
		//ss3.put("PS", ps3);
		ConditionClause cc3 = new ConditionClause(ss3);
		GenericRule rule3 = new GenericRule(FilteringAction.ALLOW, cc3, "R3");
		policyImpl.insertRule(rule3, 3);

		Assert.assertEquals(3,policyImpl.getRuleClassifier().getIntersectingRules(rule2).size());
		
		String rules = "";
		for(GenericRule r:policyImpl.getRuleClassifier().getIntersectingRules(rule2)){
			rules+=r.getName()+"-";
		}
		
		Assert.assertEquals("R1-R2-R3-",rules);
	}
	
}
