package it.polito.policytoollib.test.policy;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.ResolutionErrorException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalFormGenerator;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import org.junit.Assert;
import org.junit.Test;

public class CanonicalFormTest {
	
	
	@Test
	public void canonicalForm() throws InvalidRangeException, IncompatibleExternalDataException, DuplicateExternalDataException, UnsupportedSelectorException, UnmanagedRuleException, ResolutionErrorException, NoExternalDataException, InvalidActionException{
		
		PolicyImpl policy = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test");
		
		
		
		LinkedHashMap<String, Selector> selectors1 = new LinkedHashMap<String, Selector>();
		PortSelector ps1 = new PortSelector();
		ps1.addRange("1","10");
		selectors1.put("PS", ps1);
		ConditionClause cc1 = new ConditionClause(selectors1);
		GenericRule rule1 = new GenericRule(FilteringAction.ALLOW, cc1, "R1");
		
		LinkedHashMap<String, Selector> selectors2 = new LinkedHashMap<String, Selector>();
		PortSelector ps2 = new PortSelector();
		ps2.addRange("5","9");
		selectors2.put("PS", ps2);
		ConditionClause cc2 = new ConditionClause(selectors2);
		GenericRule rule2 = new GenericRule(FilteringAction.ALLOW, cc2, "R2");
		
		LinkedHashMap<String, Selector> selectors3 = new LinkedHashMap<String, Selector>();
		PortSelector ps3 = new PortSelector();
		ps3.addRange("3","5");
		selectors3.put("PS", ps3);
		ConditionClause cc3 = new ConditionClause(selectors3);
		GenericRule rule3 = new GenericRule(FilteringAction.ALLOW, cc3, "R3");
		
		LinkedHashMap<String, Selector> selectors4 = new LinkedHashMap<String, Selector>();
		PortSelector ps4 = new PortSelector();
		ps4.addRange("5","7");
		selectors4.put("PS", ps4);
		ConditionClause cc4 = new ConditionClause(selectors4);
		GenericRule rule4 = new GenericRule(FilteringAction.ALLOW, cc4, "R4");
		
		policy.insertRule(rule1, 1);
		policy.insertRule(rule2, 1);
		policy.insertRule(rule3, 1);
		policy.insertRule(rule4, 1);
		
		
		SelectorTypes selectorTypes = new SelectorTypes();
		selectorTypes.addSelectorType("PS", new PortSelector());
		
		
		
		CanonicalFormGenerator.getInstance(policy, selectorTypes).generateClosure();
		
		CanonicalForm can = CanonicalFormGenerator.getInstance(policy, selectorTypes).getCanonicalForm();
		
		assertEquals(5, can.getRuleSet().size());
		
		
		HashMap<String, GenericRule> rule_map = new HashMap<String, GenericRule>();
		for(GenericRule r:can.getRuleSet()){
			rule_map.put(r.getName(), r);
		}
		
		assertTrue(rule_map.containsKey("R1"));
		assertTrue(rule_map.containsKey("R1_R2"));
		assertTrue(rule_map.containsKey("R1_R3"));
		assertTrue(rule_map.containsKey("R1_R2_R4"));
		assertTrue(rule_map.containsKey("R1_R2_R3_R4"));
		
		
		assertEquals("[1 - 10] ", rule_map.get("R1").getConditionClause().get("PS").toString());
		assertEquals("[5 - 9] ", rule_map.get("R1_R2").getConditionClause().get("PS").toString());
		assertEquals("[3 - 5] ", rule_map.get("R1_R3").getConditionClause().get("PS").toString());
		assertEquals("[5 - 7] ", rule_map.get("R1_R2_R4").getConditionClause().get("PS").toString());
		assertEquals("[5 - 5] ", rule_map.get("R1_R2_R3_R4").getConditionClause().get("PS").toString());
		
		
	}
}
