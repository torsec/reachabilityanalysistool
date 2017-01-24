package it.polito.policytoollib.test.performance;


import java.util.LinkedHashMap;
import java.util.LinkedList;

import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.generator.util.RandomFilteringPolicyModel;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.Host;
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
import it.polito.policytoollib.rule.selector.impl.PortSelector;



public class TesiPerformaceTest {
	
	public static void main(String[] args) throws Exception{
		try{
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
		} catch (Exception e){
			
		}
		System.out.println();
	}
	
	
	private static void singleRun(int maxFW, int maxNAT, int maxClient, int maxServer, int ruleNum, int stat) throws Exception{
		
		
		RandomFilteringPolicyModel model = getRandomModel(maxFW, maxNAT, maxClient, maxServer, ruleNum, stat);
		
		analysisTest(model);
		
		reachabilityTest(model, maxFW);
		
		comparisonTest1(model);
		
		comparisonTest2(model);
		
		comparisonTest3(model);
		
		
		
		
		
//		for(FilteringZone zone:model.getLandscape().getZoneList().values()){
//			System.out.println("==========================================================");
//			System.out.println(zone.getIPSubnet());
//			for(Host host:zone.getHostList()){
//				System.out.println(host.getIPAddress());
//			}
//		}
		
		
		
//		for(GenericRule rule:model.getEquivalentFW_list().get(model.getLandscape().getZoneList().get("Zone1")).get(model.getLandscape().getZoneList().get("Zone2")).getRuleSet()){
//			System.out.println(rule);
//		}
		
		
	}
	
	private static RandomFilteringPolicyModel getRandomModel(int maxFW, int maxNAT, int maxClient, int maxServer, int ruleNum, int stat) throws Exception{
		System.out.print("maxRule: ");
		System.out.print(ruleNum);
		System.out.print(";");
		System.out.print("maxFW: ");
		System.out.print(maxFW);
		System.out.print(";");
		RandomFilteringPolicyModel model = new RandomFilteringPolicyModel(maxFW, maxNAT, maxClient, maxServer, ruleNum, stat);
		System.out.print("maxNAT: ");
		System.out.print(maxNAT);
		System.out.print("; ");
		
		

		long startTime = System.currentTimeMillis();
		model.getEquivalentFW_list();
		long stopTime = System.currentTimeMillis();
		System.out.print("EquFW: ");
		System.out.print(stopTime - startTime);
		System.out.print(";");
		System.out.print(model.getEquivalentFW_list().get(model.getLandscape().getZoneList().get("Zone1")).get(model.getLandscape().getZoneList().get("Zone2")).getCan().getRuleSet().size());
		System.out.print("; ");
		
		return model;	
	}
	
	
	private static void analysisTest(RandomFilteringPolicyModel model) throws Exception{
		long startTime = System.currentTimeMillis();
		model.getDistributedConflicts("Zone1", "Zone2");
		long stopTime = System.currentTimeMillis();
		System.out.print("Intra: ");
		System.out.print(stopTime - startTime);
		System.out.print(";");
	}
	
	private static void reachabilityTest(RandomFilteringPolicyModel model, int maxFW) throws Exception{
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
	}
	
	private static void comparisonTest1(RandomFilteringPolicyModel model) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		Policy policy = model.getEquivalentFW_list().get(model.getLandscape().getZoneList().get("Zone1")).get(model.getLandscape().getZoneList().get("Zone2")).getFMRPolicy();
		long startTime = System.currentTimeMillis();
		model.compare(policy);
		long stopTime = System.currentTimeMillis();
		System.out.print("compare1: ");
		System.out.print(stopTime - startTime);
		System.out.print(";");
	}
	
	private static void comparisonTest2(RandomFilteringPolicyModel model) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		Policy policy = model.getEquivalentFW_list().get(model.getLandscape().getZoneList().get("Zone1")).get(model.getLandscape().getZoneList().get("Zone2")).getFMRPolicy();
		LinkedList<GenericRule> remove = new LinkedList<GenericRule>();
		int i=0;
		for(GenericRule r:policy.getRuleSet()){
			if(i<10){
				remove.add(r);
			} else {
				break;
			}
			i++;
		}
		policy.getRuleSet().removeAll(remove);
		long startTime = System.currentTimeMillis();
		model.compare(policy);
		long stopTime = System.currentTimeMillis();
		System.out.print("compare2: ");
		System.out.print(stopTime - startTime);
		System.out.print(";");
	}
	
	private static void comparisonTest3(RandomFilteringPolicyModel model) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		Policy policy = model.getEquivalentFW_list().get(model.getLandscape().getZoneList().get("Zone1")).get(model.getLandscape().getZoneList().get("Zone2")).getFMRPolicy();
		LinkedList<GenericRule> remove = new LinkedList<GenericRule>();
		int i=0;
		for(GenericRule r:policy.getRuleSet()){
			if(i<10){
				PortSelector dp = new PortSelector();
				dp.addRange(10);
				r.getConditionClause().addSelector("Destination Port", dp);
			} else {
				break;
			}
			i++;
		}
		policy.getRuleSet().removeAll(remove);
		long startTime = System.currentTimeMillis();
		model.compare(policy);
		long stopTime = System.currentTimeMillis();
		System.out.print("compare3: ");
		System.out.print(stopTime - startTime);
	}

}
