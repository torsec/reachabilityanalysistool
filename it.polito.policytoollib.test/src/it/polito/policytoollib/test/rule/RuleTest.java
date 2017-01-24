package it.polito.policytoollib.test.rule;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

import org.junit.Test;

public class RuleTest {

	@Test
	public void rule() throws InvalidIpAddressException, InvalidRangeException{
		LinkedHashMap<String, Selector> hashMap = new LinkedHashMap<String, Selector>();
		
		
		
		ConditionClause cc = new ConditionClause(hashMap);
		GenericRule rule = new GenericRule(FilteringAction.ALLOW, cc, "TEST");
		
		assertTrue(rule.isConditionEquivalent(cc));
		assertTrue(rule.isEmpty());
		
		IpSelector ip = new IpSelector();
		ip.addRange("1.1.1.1");
		hashMap.put("IP", ip);
		assertFalse(rule.isEmpty());
		assertTrue(rule.isConditionEquivalent(cc));
	}
}
