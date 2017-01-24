package it.polito.policytoollib.test.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.ResolutionErrorException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.MultiTypeResolutionStrategy;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalFormGenerator;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.junit.Test;

public class ReconciliatonTest {

	
	@Test
	public void canonicalForm() throws InvalidRangeException, IncompatibleExternalDataException, DuplicateExternalDataException, UnsupportedSelectorException, UnmanagedRuleException, ResolutionErrorException, NoExternalDataException, InvalidActionException{
		
		

		PolicyImpl policy1 = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test1");
		
		LinkedHashMap<String, Selector> selectors11 = new LinkedHashMap<String, Selector>();
		PortSelector ps11 = new PortSelector();
		ps11.addRange("5","7");
		selectors11.put("PS", ps11);
		ConditionClause cc11 = new ConditionClause(selectors11);
		GenericRule rule11 = new GenericRule(FilteringAction.ALLOW, cc11, "R11");
		
		LinkedHashMap<String, Selector> selectors12 = new LinkedHashMap<String, Selector>();
		PortSelector ps12 = new PortSelector();
		ps12.addRange("5","9");
		selectors12.put("PS", ps12);
		ConditionClause cc12 = new ConditionClause(selectors12);
		GenericRule rule12 = new GenericRule(FilteringAction.ALLOW, cc12, "R12");
		
		LinkedHashMap<String, Selector> selectors13 = new LinkedHashMap<String, Selector>();
		PortSelector ps13 = new PortSelector();
		ps13.addRange("3","5");
		selectors13.put("PS", ps13);
		ConditionClause cc13 = new ConditionClause(selectors13);
		GenericRule rule13 = new GenericRule(FilteringAction.DENY, cc13, "R13");
		
		LinkedHashMap<String, Selector> selectors14 = new LinkedHashMap<String, Selector>();
		PortSelector ps14 = new PortSelector();
		ps14.addRange("1","10");
		selectors14.put("PS", ps14);
		ConditionClause cc14 = new ConditionClause(selectors14);
		GenericRule rule14 = new GenericRule(FilteringAction.DENY, cc14, "R14");
		
		policy1.insertRule(rule11, 1);
		policy1.insertRule(rule12, 2);
		policy1.insertRule(rule13, 3);
		policy1.insertRule(rule14, 4);

		
//========================================================================================================
		PolicyImpl policy2 = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test2");
		
		LinkedHashMap<String, Selector> selectors21 = new LinkedHashMap<String, Selector>();
		PortSelector ps21 = new PortSelector();
		ps21.addRange("5","7");
		selectors21.put("PS", ps21);
		ConditionClause cc21 = new ConditionClause(selectors21);
		GenericRule rule21 = new GenericRule(FilteringAction.DENY, cc21, "R21");
		
		LinkedHashMap<String, Selector> selectors22 = new LinkedHashMap<String, Selector>();
		PortSelector ps22 = new PortSelector();
		ps22.addRange("5","9");
		selectors22.put("PS", ps22);
		ConditionClause cc22 = new ConditionClause(selectors22);
		GenericRule rule22 = new GenericRule(FilteringAction.DENY, cc22, "R22");
		
		LinkedHashMap<String, Selector> selectors23 = new LinkedHashMap<String, Selector>();
		PortSelector ps23 = new PortSelector();
		ps23.addRange("3","5");
		selectors23.put("PS", ps23);
		ConditionClause cc23 = new ConditionClause(selectors23);
		GenericRule rule23 = new GenericRule(FilteringAction.ALLOW, cc23, "R23");
		
		LinkedHashMap<String, Selector> selectors24 = new LinkedHashMap<String, Selector>();
		PortSelector ps24 = new PortSelector();
		ps24.addRange("1","10");
		selectors24.put("PS", ps24);
		ConditionClause cc24 = new ConditionClause(selectors24);
		GenericRule rule24 = new GenericRule(FilteringAction.ALLOW, cc24, "R24");
		
		policy2.insertRule(rule21, 1);
		policy2.insertRule(rule22, 2);
		policy2.insertRule(rule23, 3);
		policy2.insertRule(rule24, 4);
	
//========================================================================================================
		PolicyImpl policy3 = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test3");
		
		LinkedHashMap<String, Selector> selectors31 = new LinkedHashMap<String, Selector>();
		PortSelector ps31 = new PortSelector();
		ps31.addRange("5","7");
		selectors31.put("PS", ps31);
		ConditionClause cc31 = new ConditionClause(selectors31);
		GenericRule rule31 = new GenericRule(FilteringAction.DENY, cc31, "R31");
		
		LinkedHashMap<String, Selector> selectors32 = new LinkedHashMap<String, Selector>();
		PortSelector ps32 = new PortSelector();
		ps32.addRange("5","9");
		selectors32.put("PS", ps32);
		ConditionClause cc32 = new ConditionClause(selectors32);
		GenericRule rule32 = new GenericRule(FilteringAction.DENY, cc32, "R32");
		
		LinkedHashMap<String, Selector> selectors33 = new LinkedHashMap<String, Selector>();
		PortSelector ps33 = new PortSelector();
		ps33.addRange("3","5");
		selectors33.put("PS", ps33);
		ConditionClause cc33 = new ConditionClause(selectors33);
		GenericRule rule33 = new GenericRule(FilteringAction.ALLOW, cc33, "R33");
		
		LinkedHashMap<String, Selector> selectors34 = new LinkedHashMap<String, Selector>();
		PortSelector ps34 = new PortSelector();
		ps34.addRange("1","10");
		selectors34.put("PS", ps34);
		ConditionClause cc34 = new ConditionClause(selectors34);
		GenericRule rule34 = new GenericRule(FilteringAction.ALLOW, cc34, "R34");
		
		policy3.insertRule(rule31, 1);
		policy3.insertRule(rule32, 2);
		policy3.insertRule(rule33, 3);
		policy3.insertRule(rule34, 4);
		
//===============================================================================S========================
		
		
		SelectorTypes selectorTypes = new SelectorTypes();
		selectorTypes.addSelectorType("PS", new PortSelector());
	
		
		
		LinkedList<Policy> policy_list = new LinkedList<Policy>();
		
		policy_list.add(policy1);
		policy_list.add(policy2);
		policy_list.add(policy3);
		
		MultiTypeResolutionStrategy multi_RS =  new MultiTypeResolutionStrategy(policy_list, FilteringAction.DENY);
		
		PolicyImpl multi_policy = new PolicyImpl(multi_RS, FilteringAction.DENY, PolicyType.FILTERING, "MultiPolicy");
		
		for (GenericRule rule:policy1.getRuleSet())
			multi_policy.insertRule(rule);
		for (GenericRule rule:policy2.getRuleSet())
			multi_policy.insertRule(rule);
		for (GenericRule rule:policy3.getRuleSet())
			multi_policy.insertRule(rule);
		
		
		CanonicalFormGenerator.getInstance(multi_policy, selectorTypes).generateClosure();
		
		CanonicalForm can = CanonicalFormGenerator.getInstance(multi_policy, selectorTypes).getCanonicalForm();
		
		
		HashMap<String, GenericRule> rule_map = new HashMap<String, GenericRule>();
		for(GenericRule r:can.getRuleSet()){
			System.out.println(r);
			rule_map.put(r.getName(), r);
		}
		
		assertTrue(rule_map.containsKey("R14_R24_R34"));
		assertTrue(rule_map.containsKey("R12_R14_R22_R24_R32_R34"));
		assertTrue(rule_map.containsKey("R13_R14_R23_R24_R33_R34"));
		assertTrue(rule_map.containsKey("R11_R12_R14_R21_R22_R24_R31_R32_R34"));
		assertTrue(rule_map.containsKey("R11_R12_R13_R14_R21_R22_R23_R24_R31_R32_R33_R34"));
		
		
		assertEquals("[1 - 10] ", rule_map.get("R14_R24_R34").getConditionClause().get("PS").toString());
		assertEquals("[5 - 9] ", rule_map.get("R12_R14_R22_R24_R32_R34").getConditionClause().get("PS").toString());
		assertEquals("[3 - 5] ", rule_map.get("R13_R14_R23_R24_R33_R34").getConditionClause().get("PS").toString());
		assertEquals("[5 - 7] ", rule_map.get("R11_R12_R14_R21_R22_R24_R31_R32_R34").getConditionClause().get("PS").toString());
		assertEquals("[5 - 5] ", rule_map.get("R11_R12_R13_R14_R21_R22_R23_R24_R31_R32_R33_R34").getConditionClause().get("PS").toString());
		
		assertEquals(FilteringAction.DENY, rule_map.get("R14_R24_R34").getAction());
		assertEquals(FilteringAction.ALLOW, rule_map.get("R12_R14_R22_R24_R32_R34").getAction());
		assertEquals(FilteringAction.DENY, rule_map.get("R13_R14_R23_R24_R33_R34").getAction());
		assertEquals(FilteringAction.ALLOW, rule_map.get("R11_R12_R14_R21_R22_R24_R31_R32_R34").getAction());
		assertEquals(FilteringAction.ALLOW, rule_map.get("R11_R12_R13_R14_R21_R22_R23_R24_R31_R32_R33_R34").getAction());
	}
}
