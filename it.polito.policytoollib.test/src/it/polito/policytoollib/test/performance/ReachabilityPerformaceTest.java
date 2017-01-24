package it.polito.policytoollib.test.performance;


import java.util.LinkedHashMap;
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
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;



public class ReachabilityPerformaceTest {
	
	public static void main(String[] args) throws Exception{
		
		if(args.length==5){
			int maxFW = Integer.parseInt(args[0]);
			int maxNAT = Integer.parseInt(args[1]);
			int maxClient = Integer.parseInt(args[2]);
			int maxServer = Integer.parseInt(args[3]);
			int ruleNum = Integer.parseInt(args[4]);
			
			singleRun(maxFW, maxNAT, maxClient, maxServer, ruleNum, 4);
		} else {
			System.out.println("ERROR");
			singleRun(5, 25, 100, 100, 50, 4);
		}
	}
	
	
	private static void singleRun(int maxFW, int maxNAT, int maxClient, int maxServer, int ruleNum, int stat) throws Exception{
		
		
		
		RandomFilteringPolicyModel model1 = singleRun1(maxFW, maxNAT, maxClient, maxServer, ruleNum, stat);
		Policy policy1 = model1.getEquivalentFW_list().get(model1.getLandscape().getZoneList().get("Zone1")).get(model1.getLandscape().getZoneList().get("Zone2")).getFMRPolicy();
		
		
		
		RandomFilteringPolicyModel model2 = singleRun1(maxFW, maxNAT, maxClient, maxServer, ruleNum, stat);
		Policy policy2 = model2.getEquivalentFW_list().get(model2.getLandscape().getZoneList().get("Zone1")).get(model2.getLandscape().getZoneList().get("Zone2")).getFMRPolicy();
		
		
		LinkedList<LinkedList<Policy>> policy_list = new LinkedList<LinkedList<Policy>>();
		LinkedList<Policy> policy_l1 = new LinkedList<Policy>();
		policy_l1.add(policy1);
		policy_list.add(policy_l1 );
		LinkedList<Policy> policy_l2 = new LinkedList<Policy>();
		policy_l2.add(policy2);
		policy_list.add(policy_l2);
		ComposedPolicy policy = new ComposedPolicy(policy_list , PolicyType.FILTERING, "test");
		long startTime6 = System.currentTimeMillis();
		CanonicalFormGenerator.getInstance(policy, model1.getSelectorTypes()).generateClosure();
		CanonicalForm can = CanonicalFormGenerator.getInstance(policy, model1.getSelectorTypes()).getCanonicalForm();
		long stopTime6 = System.currentTimeMillis();
		System.out.print("CAN: ");
		System.out.print(stopTime6 - startTime6);
		System.out.print(";");
		System.out.print(can.getRuleSet().size());
		System.out.println(";");
		
		
		
//		policy_list = new LinkedList<LinkedList<Policy>>();
//		policy_l1 = new LinkedList<Policy>();
//		policy_l1.add(model1.getEquivalentFW_list().get(model1.getLandscape().getZoneList().get("Zone1")).get(model1.getLandscape().getZoneList().get("Zone2")));
//		policy_list.add(policy_l1 );
//		policy_l2 = new LinkedList<Policy>();
//		policy_l2.add(model2.getEquivalentFW_list().get(model2.getLandscape().getZoneList().get("Zone1")).get(model2.getLandscape().getZoneList().get("Zone2")));
//		policy_list.add(policy_l2);
//		policy = new ComposedPolicy(policy_list , PolicyType.FILTERING, "test");
//		long startTime9 = System.currentTimeMillis();
//		CanonicalFormGenerator.getInstance(policy, model1.getSelectorTypes()).generateClosure();
//		can = CanonicalFormGenerator.getInstance(policy, model1.getSelectorTypes()).getCanonicalForm();
//		long stopTime9 = System.currentTimeMillis();
//		System.out.print("CAN: ");
//		System.out.print(stopTime9 - startTime9);
//		System.out.print(";");
//		System.out.print(can.getRuleSet().size());
//		System.out.println(";");
	
		
		
	}
	
	private static RandomFilteringPolicyModel singleRun1(int maxFW, int maxNAT, int maxClient, int maxServer, int ruleNum, int stat) throws Exception{
		System.out.print("maxFW: ");
		System.out.print(maxFW);
		System.out.print(";");
		RandomFilteringPolicyModel model = new RandomFilteringPolicyModel(maxFW, maxNAT, maxClient, maxServer, ruleNum, stat);
		System.out.print("maxNAT: ");
		System.out.print(maxNAT);
		System.out.print("; ");
		
		

		long startTime2 = System.currentTimeMillis();
		model.getEquivalentFW_list();
		long stopTime2 = System.currentTimeMillis();
		System.out.print("EquFW: ");
		System.out.print(stopTime2 - startTime2);
		System.out.print(";");
		System.out.print(model.getEquivalentFW_list().get(model.getLandscape().getZoneList().get("Zone1")).get(model.getLandscape().getZoneList().get("Zone2")).getRuleSet().size());
		System.out.print("; ");

		
		
		
		
		long startTime3 = System.currentTimeMillis();
		int Sresult = model.setClientQuerry("10.0.0.1").size();
		long stopTime3 = System.currentTimeMillis();
		System.out.print("Squerry: ");
		System.out.print(stopTime3 - startTime3);
		System.out.print(";");
		System.out.print(Sresult);
		System.out.print("; ");
		
		long startTime4 = System.currentTimeMillis();
		LinkedHashMap<String, Selector> selHash = new LinkedHashMap<String, Selector>();
		IpSelector dip = new IpSelector();
		dip.addRange("10.0."+(maxFW+1)+".1");
		selHash.put("Destination Address", dip);
		IpSelector sip = new IpSelector();
		sip.addRange("10.0.0.1");
		selHash.put("Source Address", sip);
		ConditionClause zone_rule = new ConditionClause(selHash);
		int Presult = model.executeQuerry(zone_rule).size();
		long stopTime4 = System.currentTimeMillis();
		System.out.print("Pquerry: ");
		System.out.print(stopTime4 - startTime4);
		System.out.print(";");
		System.out.print(Presult);
		System.out.print("; ");
		
		long startTime5 = System.currentTimeMillis();
		int Qresult = model.setReachabilityQuerry().size();
		long stopTime5 = System.currentTimeMillis();
		System.out.print("Qquerry: ");
		System.out.print(stopTime5 - startTime5);
		System.out.print(";");
		System.out.print(Qresult);
		System.out.print("; ");
		
		
		return model;	
	}
	


}
