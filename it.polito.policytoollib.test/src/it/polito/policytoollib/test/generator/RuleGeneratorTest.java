package it.polito.policytoollib.test.generator;

import static org.junit.Assert.*;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.generator.rule.RuleGenerator;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

import org.junit.Test;

public class RuleGeneratorTest {
	
	private RuleGenerator ruleGenerator = new RuleGenerator(2,100,5,25,1);

	@Test
	public void test() throws InvalidIpAddressException{
		
		ruleGenerator.setSourceStartIP("10.0.0.0");
		ruleGenerator.setSourceEndIP("10.0.0.255");
		
		ruleGenerator.setDestinationStartIP("10.0.0.0");
		ruleGenerator.setDestinationEndIP("10.0.0.255");
		
		SelectorTypes selectorTypes = new SelectorTypes();
		selectorTypes.addSelectorType("Source Address",  new IpSelector());
		selectorTypes.addSelectorType("Destination Address",  new IpSelector());
		
		GenericRule rule = ruleGenerator.getGenericRule(FilteringAction.ALLOW, "TestRule", selectorTypes, selectorTypes.getSelectorNames());
		
		System.out.println(rule);
	}
}
