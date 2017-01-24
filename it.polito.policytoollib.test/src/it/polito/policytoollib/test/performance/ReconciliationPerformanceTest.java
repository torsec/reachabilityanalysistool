package it.polito.policytoollib.test.performance;



import java.util.LinkedList;
import java.util.List;

import it.polito.policytoollib.generator.policy.SquidPolicyGenerator;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.MultiTypeResolutionStrategy;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalFormGenerator;
import it.polito.policytoollib.policy.translation.morphisms.FMRMorphism;
import it.polito.policytoollib.policy.translation.semilattice.SemiLatticeGenerator;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.wrapper.XMLTranslator;

public class ReconciliationPerformanceTest {
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("START");
		//System.out.println(Integer.parseInt(args[0]) + " "+ Integer.parseInt(args[1]) + " "+ Integer.parseInt(args[2]));
		if(args.length == 3)
			run(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		else
			run(1, 50, 10);
	}
	
	
	
	public static void run(int type, int numRule, int numPolicy) throws Exception {
		SquidPolicyGenerator gen = new SquidPolicyGenerator();
		
		Policy policy = null;
		
		long startTime, stopTime, elapsedTime;
		
		if (type == 1)
			policy = gen.getPolicy_4_20(numRule);
		else if (type == 2)
			policy = gen.getPolicy_8_40(numRule);
		else
			policy = (new XMLTranslator(gen.getSelectorTypes())).readPolicy("fw2500.xml");
		
		System.out.print(type);
		System.out.print(",");
		System.out.print(numRule);
		System.out.print(",");
		System.out.print(numPolicy);
		System.out.print(",");
		
		System.out.print(policy.getRuleSet().size());
		System.out.print(",");
		
		if(numPolicy!=1){
			int i=0;
			
			LinkedList<Policy> policy_list = new LinkedList<Policy>();
			Policy p = null;
			for(GenericRule rule:policy.getRuleSet()){
				if(i==0){
					p = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Policy");
					policy_list.add(p);
				}
				i++;
				p.insertRule(rule, i);
				if(i>=numRule/numPolicy){
					i=0;				
				}
			}
			
			MultiTypeResolutionStrategy multi_RS =  new MultiTypeResolutionStrategy(policy_list, FilteringAction.DENY);
			p = new PolicyImpl(multi_RS, FilteringAction.DENY, PolicyType.FILTERING, "MultiPolicy");
			
			for(GenericRule rule:policy.getRuleSet()){
				p.insertRule(rule);
			}
			
			
			policy = p;
		}
		
		CanonicalFormGenerator can_gen = CanonicalFormGenerator.getInstance(policy, gen.getSelectorTypes());
		
		startTime = System.currentTimeMillis();
		can_gen.generateClosure();
		stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    
		CanonicalForm can = can_gen.getCanonicalForm();
		
		System.out.print(can.getRuleSet().size());
		System.out.print(",");
		
		
		System.out.print(elapsedTime);
		System.out.print(",");
		SemiLatticeGenerator slgen = new SemiLatticeGenerator();
		
		
		startTime = System.currentTimeMillis();
		slgen.generateSemilattice(can);
		stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    System.out.print(elapsedTime);
	    System.out.print(",");
		FMRMorphism fmr = new FMRMorphism(can);
		
		
		
		startTime = System.currentTimeMillis();
		List<GenericRule> rules = fmr.exportRules();
		stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime);
	}
	

}
