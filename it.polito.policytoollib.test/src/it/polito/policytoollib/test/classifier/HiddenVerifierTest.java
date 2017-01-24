package it.polito.policytoollib.test.classifier;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.DuplicatedRuleException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
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
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

public class HiddenVerifierTest {

	
	@Test
	public void isHidden() throws IncompatibleExternalDataException, DuplicateExternalDataException, InvalidRangeException, UnsupportedSelectorException, NoExternalDataException, DuplicatedRuleException, UnmanagedRuleException {
		PolicyImpl policyImpl = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test");
		


		// --------------------Rule1
		LinkedHashMap<String, Selector> ss1 = new LinkedHashMap<String, Selector>();
		PortSelector ps11 = new PortSelector();
		ss1.put("PS1", ps11);
		ps11.addRange(5, 25);
		PortSelector ps12 = new PortSelector();
		ss1.put("PS2", ps12);
		ps12.addRange(5, 25);
		ConditionClause cc1 = new ConditionClause(ss1);
		GenericRule rule1 = new GenericRule(FilteringAction.ALLOW, cc1, "R1");
		policyImpl.insertRule(rule1, 1);

		// --------------------Rule2
		LinkedHashMap<String, Selector> ss2 = new LinkedHashMap<String, Selector>();
		PortSelector ps21 = new PortSelector();
		ps21.addRange(20, 35);
		ss2.put("PS1", ps21);
		PortSelector ps22 = new PortSelector();
		ps22.addRange(10, 35);
		ss2.put("PS2", ps22);
		ConditionClause cc2 = new ConditionClause(ss2);
		GenericRule rule2 = new GenericRule(FilteringAction.ALLOW, cc2, "R2");
		policyImpl.insertRule(rule2, 2);

		// --------------------Rule3
		LinkedHashMap<String, Selector> ss3 = new LinkedHashMap<String, Selector>();
		PortSelector ps31 = new PortSelector();
		ps31.addRange(10, 20);
		ss3.put("PS1", ps31);
		PortSelector ps32 = new PortSelector();
		ps32.addRange(20, 40);
		ss3.put("PS2", ps32);
		ConditionClause cc3 = new ConditionClause(ss3);
		GenericRule rule3 = new GenericRule(FilteringAction.ALLOW, cc3, "R3");
		policyImpl.insertRule(rule3, 3);
		
		// --------------------Rule4
		LinkedHashMap<String, Selector> ss4 = new LinkedHashMap<String, Selector>();
		PortSelector ps41 = new PortSelector();
		ss4.put("PS1", ps41);
		ps41.addRange(15, 30);
		PortSelector ps42 = new PortSelector();
		ss4.put("PS2", ps42);
		ps42.addRange(15, 30);
		ConditionClause cc4 = new ConditionClause(ss4);
		GenericRule rule4 = new GenericRule(FilteringAction.ALLOW, cc4, "R4");
		policyImpl.insertRule(rule4, 4);

		GenericRule[] rule_list1 = new GenericRule[2];
		
		rule_list1[0] = rule2;
		rule_list1[1] = rule3;
		
		Assert.assertFalse(policyImpl.getRuleClassifier().isHidden(rule4, rule_list1));

		GenericRule[] rule_list2 = new GenericRule[3];
		rule_list2[0] = rule1;
		rule_list2[1] = rule2;
		rule_list2[2] = rule3;
		
		Assert.assertTrue(policyImpl.getRuleClassifier().isHidden(rule4, rule_list2));
	}
	
	
//	@Test
//	public void isHiddenFull() throws IncompatibleExternalDataException, DuplicateExternalDataException, InvalidRangeException, UnsupportedSelectorException, NoExternalDataException, DuplicatedRuleException, UnmanagedRuleException {
//		PolicyImpl policyImpl = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test");
//		
//
//
//		// --------------------Rule1
//		LinkedHashMap<String, Selector> ss1 = new LinkedHashMap<String, Selector>();
//		PortSelector ps11 = new PortSelector();
//		ss1.put("PS1", ps11);
//		ps11.addRange(0, 9);
//		PortSelector ps12 = new PortSelector();
//		ss1.put("PS2", ps12);
//		ps12.addRange(0, 9);
//		ConditionClause cc1 = new ConditionClause(ss1);
//		GenericRule rule1 = new GenericRule(FilteringAction.ALLOW, cc1, "R1");
//		policyImpl.insertRule(rule1, 1);
//
//		// --------------------Rule2
//		LinkedHashMap<String, Selector> ss2 = new LinkedHashMap<String, Selector>();
//		PortSelector ps21 = new PortSelector();
//		ps21.addRange(10, 19);
//		ss2.put("PS1", ps21);
//		PortSelector ps22 = new PortSelector();
//		ps22.addRange(10, 19);
//		ss2.put("PS2", ps22);
//		ConditionClause cc2 = new ConditionClause(ss2);
//		GenericRule rule2 = new GenericRule(FilteringAction.ALLOW, cc2, "R2");
//		policyImpl.insertRule(rule2, 2);
//
//		// --------------------Rule3
//		LinkedHashMap<String, Selector> ss3 = new LinkedHashMap<String, Selector>();
//		PortSelector ps31 = new PortSelector();
//		ps31.addRange(20, 65535);
//		ss3.put("PS1", ps31);
//		PortSelector ps32 = new PortSelector();
//		ps32.addRange(20, 65535);
//		ss3.put("PS2", ps32);
//		ConditionClause cc3 = new ConditionClause(ss3);
//		GenericRule rule3 = new GenericRule(FilteringAction.ALLOW, cc3, "R3");
//		policyImpl.insertRule(rule3, 3);
//		
//		// --------------------Rule4
//		LinkedHashMap<String, Selector> ss4 = new LinkedHashMap<String, Selector>();
//		PortSelector ps41 = new PortSelector();
//		ps41.full();
//		ss4.put("PS1", ps41);
//		PortSelector ps42 = new PortSelector();
//		ps42.full();
//		ss4.put("PS2", ps42);
//		ConditionClause cc4 = new ConditionClause(ss4);
//		GenericRule rule4 = new GenericRule(FilteringAction.ALLOW, cc4, "R4");
//		policyImpl.insertRule(rule4, 4);
//
//		GenericRule[] rule_list1 = new GenericRule[2];
//		
//		rule_list1[0] = rule2;
//		rule_list1[1] = rule3;
//		
//		//Assert.assertFalse(policyImpl.getRuleClassifier().isHidden(rule4, rule_list1));
//
//		GenericRule[] rule_list2 = new GenericRule[3];
//		rule_list2[0] = rule1;
//		rule_list2[1] = rule2;
//		rule_list2[2] = rule3;
//		
//		Assert.assertTrue(policyImpl.getRuleClassifier().isHidden(rule4, rule_list2));
//	}
	
}
