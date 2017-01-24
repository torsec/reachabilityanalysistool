package it.polito.policytoollib.policy.anomaly;

import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.MultiPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.impl.NATRule;

import java.util.HashSet;
import java.util.LinkedList;


public class NATAnalyzer {
	
	private CanonicalForm can;
	private SelectorTypes selectorTypes;

	public NATAnalyzer(CanonicalForm can, SelectorTypes selectorTypes){
		this.can = can;
		this.selectorTypes = selectorTypes;
	}
	
	
	public void getNATAnomalies() throws Exception{
		
		PolicyImpl test_p = new PolicyImpl(new FMRResolutionStrategy(), can.getDefaultAction(), PolicyType.FILTERING, "");
		LinkedList<Policy> test_policy_list = new LinkedList<Policy>();
		test_policy_list.add(test_p);
		test_policy_list.add(can.getOriginalPolicy());
		
		MultiPolicy test_policy = new MultiPolicy(test_policy_list, can.getDefaultAction(),PolicyType.FILTERING, "");
		
		System.err.println(can.getResolutionStrategy().toString());
		
		for(GenericRule rule:can.getOriginalPolicy().getRuleSet()){
			if(rule instanceof NATRule){
				ConditionClause natCondition = ((NATRule)rule).getNATRule();
				if(natCondition!=null){
					System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRr");
					System.out.println(natCondition);	
					
					for(GenericRule r:((NATRule)rule).getOriginalRules()){
	
					
						
						HashSet<GenericRule> ruleSet = new HashSet<GenericRule>();
						for(GenericRule rr : can.getOriginalPolicy().getRuleSet()){
							if(rr.isIntersecting(r)){
								ruleSet.add(rr);
							}
						}
						
						GenericRule testRule = new GenericRule(can.getResolutionStrategy().composeActions(ruleSet), natCondition.conditionClauseClone(), "NAT_"+rule.getName());
	
						
						test_p.insertRule(testRule,0);
						
						
						
						
						System.out.println("::::::::::::::::::::::::::::::::::::::::::::");
						if(!test_policy.getRuleClassifier().isUnnecessary(testRule, ruleSet.toArray(new GenericRule[ruleSet.size()]))){
							test_p.removeRule(testRule);
							System.out.println(r);
							System.out.println("........................................");
						}
							
						
					}
				}
			}
		}
			
	}	
}
