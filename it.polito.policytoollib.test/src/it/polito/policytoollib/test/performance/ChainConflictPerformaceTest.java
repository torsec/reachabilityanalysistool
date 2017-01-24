package it.polito.policytoollib.test.performance;


import java.util.LinkedList;

import it.polito.policytoollib.generator.util.RandomFilteringPolicyModel;
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



public class ChainConflictPerformaceTest {
	
	public static void main(String[] args) throws Exception{
		
		if(args.length==5){
			int maxFW = Integer.parseInt(args[0]);
			int maxNAT = Integer.parseInt(args[1]);
			int maxClient = Integer.parseInt(args[2]);
			int maxServer = Integer.parseInt(args[3]);
			int ruleNum = Integer.parseInt(args[4]);
			
			singleRun(maxFW, maxNAT, maxClient, maxServer, ruleNum, 4);
		} else {
			if(args.length==1){
				int maxFW = 5;
				int maxNAT = 25;
				int maxClient = 100;
				int maxServer = 100;
				int ruleNum = 1000;
				int stat = Integer.parseInt(args[0]);
				
				singleRun(maxFW, maxNAT, maxClient, maxServer, ruleNum, stat);
			} else {
				System.out.println("ERROR");
				singleRun(5, 25, 100, 100, 50, 4);
			}
		}
	}
	
	
	private static void singleRun(int maxFW, int maxNAT, int maxClient, int maxServer, int ruleNum, int stat) throws Exception{
		
		RandomFilteringPolicyModel model = new RandomFilteringPolicyModel(maxFW, maxNAT, maxClient, maxServer, ruleNum, stat);
		System.out.print("maxFW: ");
		System.out.print(maxFW);
		System.out.print(";");
		System.out.print("maxNAT: ");
		System.out.print(maxNAT);
		System.out.print("; ");
		System.out.print("maxRule: ");
		System.out.print(ruleNum);
		System.out.print("; ");
		

		long startTime2 = System.currentTimeMillis();
		model.getEquivalentFW_list();
		long stopTime2 = System.currentTimeMillis();
		System.out.print("EquFW: ");
		System.out.print(stopTime2 - startTime2);
		System.out.print(";");
		System.out.print(model.getEquivalentFW_list().get(model.getLandscape().getZoneList().get("Zone1")).get(model.getLandscape().getZoneList().get("Zone2")).getRuleSet().size());
		System.out.print("; ");
		
		ComposedPolicy policy1 = model.getEquivalentFW_list().get(model.getLandscape().getZoneList().get("Zone1")).get(model.getLandscape().getZoneList().get("Zone2"));
		
		
		System.out.println("FMR="+policy1.getFMRPolicy().getRuleSet().size());

		
	}
	

}
