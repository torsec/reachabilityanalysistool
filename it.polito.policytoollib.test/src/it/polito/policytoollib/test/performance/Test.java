package it.polito.policytoollib.test.performance;
import java.util.Set;

import it.polito.policytoollib.generator.policy.FilteringPolicyGenerator;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.wrapper.XMLTranslator;


public class Test {

	public static void main(String[] args) throws Exception {
		for(int i=0; i<1; i++)
		{
			System.out.println(i);
			FilteringPolicyGenerator gen = new FilteringPolicyGenerator();
			Policy p = gen.getPolicy_4_20(50, 0, "fw50");
						
			XMLTranslator xml = new XMLTranslator(gen.getSelectorTypes());
			
			xml.writeOriginalRuleSetFromPolicy(p, "TestCases/Reachability/fw50.xml");
		}
	}

}
