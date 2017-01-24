package it.polito.policytoollib.test.performance;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import it.polito.policytoollib.generator.util.RandomFilteringPolicyModel;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalFormGenerator;
import it.polito.policytoollib.policy.translation.morphisms.FMRMorphism;
import it.polito.policytoollib.policy.translation.semilattice.SemiLatticeGenerator;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;



public class InterConflictPerformaceTest {
	
	public static void main(String[] args) throws Exception{
		
		if(args.length==5){
			int maxFW = Integer.parseInt(args[0]);
			int maxNAT = Integer.parseInt(args[1]);
			int ruleNum = Integer.parseInt(args[4]);
			int stat = Integer.parseInt(args[5]);
			
			singleRun(maxFW, maxNAT, ruleNum, stat);
		} else {
			System.out.println("ERROR");
			singleRun(5, 25, 50, 4);
		}
	}
	
	
	private static void singleRun(int maxFW, int maxNAT, int ruleNum, int stat) throws Exception{
		
		
		
		
		System.out.print("maxFW: ");
		System.out.print(maxFW);
		System.out.print(";");
		RandomFilteringPolicyModel model1 = new RandomFilteringPolicyModel(maxFW, maxNAT, 100, 100, ruleNum, stat);
		System.out.print("maxNAT: ");
		System.out.print(maxNAT);
		System.out.print("; ");
		
		long startTime8 = System.currentTimeMillis();
		ComposedPolicy policy1 = model1.getEquivalentFW_list().get(model1.getLandscape().getZoneList().get("Zone1")).get(model1.getLandscape().getZoneList().get("Zone2"));
		CanonicalFormGenerator.getInstance(policy1, model1.getSelectorTypes()).generateClosure();
		CanonicalForm can1 = CanonicalFormGenerator.getInstance(policy1, model1.getSelectorTypes()).getCanonicalForm();
		SemiLatticeGenerator slgen1 = new SemiLatticeGenerator();
		slgen1.generateSemilattice(can1);
//		FMRMorphism fmrMorphism1 = new FMRMorphism(can1);
//		policy1 = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test");	
//		int i1=1;
//		for (GenericRule r:fmrMorphism1.exportRules()){
//			policy1.insertRule(r, i1++);
//		}
		long stopTime8 = System.currentTimeMillis();
		System.out.print("CAN: ");
		System.out.print(stopTime8 - startTime8);
		System.out.print(";");
		System.out.print(can1.getRuleSet().size());
		System.out.print(";");
				
		long startTime9 = System.currentTimeMillis();
		Set<PolicyAnomaly> pa_set = model1.getDistributedConflicts("Zone1", "Zone2");
		long stopTime9 = System.currentTimeMillis();
		System.out.print("ANO: ");
		System.out.print(stopTime9 - startTime9);
		System.out.print(";");
		System.out.print(pa_set.size());
		System.out.println(";");
		
	}
	
	


}
