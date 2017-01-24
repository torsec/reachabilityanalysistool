package it.polito.policytoollib.generator.util;


import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.generator.policy.FilteringPolicyGenerator;
import it.polito.policytoollib.generator.policy.SquidPolicyGenerator;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.landscape.Landscape;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.policy.impl.EquivalenPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.tools.AnomalyAnalyser;
import it.polito.policytoollib.policy.tools.ReachabilityAnalyser;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalFormGenerator;
import it.polito.policytoollib.policy.translation.semilattice.SemiLatticeGenerator;
import it.polito.policytoollib.policy.translation.semilattice.Semilattice;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.action.NATAction;
import it.polito.policytoollib.rule.action.NATActionType;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;



public class RandomFilteringPolicyModel extends PolicyAnalysisModel{
	

	

	
	public RandomFilteringPolicyModel(int maxFW, int maxNAT, int maxClient, int maxServer, int ruleNum, int stat) throws Exception{
		super();
		
		
		createRandomPolicy(maxFW, maxNAT, maxClient, maxServer, ruleNum, stat);
		
		
	}


	
	
	public void createRandomPolicy(int maxFW, int maxNAT, int maxClient, int maxServer, int ruleNum, int stat) throws Exception {

				
		FilteringPolicyGenerator generator = new FilteringPolicyGenerator();
		
		
		
//		XMLTranslator xml = new XMLTranslator(generator.getSelectorTypes());
//		xml.writeOriginalRuleSetFromPolicy(policy, "fw"+ruleNum+".xml");
		
		String policyName = "FW";
		
		
		Policy policy = null;
		switch(stat){
		case 0 : 
			policy = generator.getPolicy_0_0(ruleNum, 0, policyName);
			break;
		case 1 : 
			policy = generator.getPolicy_1_5(ruleNum, 0, policyName);
			break;
		case 2 : 
			policy = generator.getPolicy_2_10(ruleNum, 0, policyName);
			break;
		case 4 : 
			policy = generator.getPolicy_4_20(ruleNum, 0, policyName);
			break;
		case 8 : 
			policy = generator.getPolicy_8_40(ruleNum, 0, policyName);
			break;
		default : 
			throw new Exception("unsuported stat value");
		}
		
		int ext = ruleNum;
		LinkedList<GenericRule> remove = new LinkedList<GenericRule>();
		for(GenericRule rule:policy.getRuleSet()){
			if(ext>=ruleNum/10){
				ext--;
				remove.add(rule);
				
			}
		}
		
		
		for(GenericRule rule:remove){
			policy.removeRule(rule);
		}
		
		
		Policy policy2 = null;
		switch(stat){
		case 0 : 
			policy2 = generator.getPolicy_0_0(ruleNum, 1, policyName);
			break;
		case 1 : 
			policy2 = generator.getPolicy_1_5(ruleNum, 1, policyName);
			break;
		case 2 : 
			policy2 = generator.getPolicy_2_10(ruleNum, 1, policyName);
			break;
		case 4 : 
			policy2 = generator.getPolicy_4_20(ruleNum, 1, policyName);
			break;
		case 8 : 
			policy2 = generator.getPolicy_8_40(ruleNum, 1, policyName);
			break;
		default : 
			throw new Exception("unsuported stat value");
		}
		
		for(GenericRule rule:policy2.getRuleSet()){
			ext++;
			if(ext<ruleNum)
				policy.insertRule(rule, ext);
		}
		
		maxFW++;
		
		for(int i=1; i<maxFW; i++){
			policyName = "FW"+i;
			
			//policy = xml.readPolicy("fw"+ruleNum+".xml");
			
			LinkedHashMap<String, Selector> selectors = new LinkedHashMap<String, Selector>();
			IpSelector sip = new IpSelector();
			sip.addRange("10.0.0.1","10.0.0.1");
			IpSelector dip = new IpSelector();
			dip.addRange("10.0.255.1","10.0.255.1");
			selectors.put("Source Address", sip);
			selectors.put("Destination Address", dip);
			ConditionClause conditionClause = new ConditionClause(selectors);
			GenericRule rule = new GenericRule(FilteringAction.ALLOW, conditionClause, "R0");
			
			policy.insertRule(rule, 0);
			HashMap<String, IpSelector> interface_list = new HashMap<String, IpSelector>();
			interface_list.put(policyName+".eth0",null);
			interface_list.put(policyName+".eth1",null);
			SecurityControl firewall = new SecurityControl(interface_list, interface_list, policyName);
			if(i>1){
				SecurityControl fw = super.getLandscape().getFirewallList().get("FW"+(i-1));
				firewall.addFW(policyName+".eth1", fw);
				fw.addFW("FW"+(i-1)+".eth0", firewall);
			}
			
			super.getLandscape().getFirewallList().put(policyName, firewall);
			
			super.addPolicy(policy);
			firewall.setPolicy(policy);
			
			
		}
		
		
		String natPolicyName = "FW1";
		
		Policy nat_policy = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.NAT, natPolicyName);
		maxNAT++;
		for(int i=1; i<maxNAT; i++){
			insertNat(nat_policy, maxFW, i);
		}
		super.addNATPolicy(nat_policy);
		super.getLandscape().getFirewallList().get("FW1").setNAT(nat_policy);
		
		FilteringZone fwZone1 = new FilteringZone("Zone1", "10.0.0.0/24", super.getLandscape().getFirewallList().get("FW1"));
		for(int i=1; i<maxClient; i++){
			IpSelector ip = new IpSelector();
			ip.addRange("10.0.0."+i,"10.0.0."+i);
			Host host = new Host("C"+i, new HashMap<String,IpSelector>(), fwZone1);
			host.addInterface("eth", ip);
			fwZone1.addHost(host);
		}
		super.getLandscape().getZoneList().put("Zone1", fwZone1);
		
		
		FilteringZone fwZone2 = new FilteringZone("Zone2", "10.0."+maxFW+".0/24", super.getLandscape().getFirewallList().get("FW"+(maxFW-1)));
		for(int i=1; i<maxServer; i++){
			IpSelector ip = new IpSelector();
//			System.out.println("10.0."+maxFW+"."+i+"  10.0."+maxFW+"."+i);
			ip.addRange("10.0."+maxFW+"."+i,"10.0."+maxFW+"."+i);
			Host host = new Host("S"+i, new HashMap<String,IpSelector>(), fwZone2);
			host.addInterface("eth", ip);
			PortSelector port = new PortSelector();
			port.addRange(80);
			host.addService("service", ip, port );
			fwZone2.addHost(host);
		}
		super.getLandscape().getZoneList().put("Zone2", fwZone2);
	}
	
	private void insertNat(Policy nat_policy, int max_fw, int ip) throws Exception{
		String natRuleName = "NAT"+ip;
		
		LinkedHashMap<String, Selector> nat_selectors = new LinkedHashMap<String, Selector>();
		IpSelector ndip = new IpSelector();
		ndip.addRange("10.0."+max_fw+"."+ip,"10.0."+max_fw+"."+ip);
		nat_selectors.put("Destination Address", ndip);
		ConditionClause conditionClause = new ConditionClause(nat_selectors);
		
		LinkedHashMap<String, Selector> trans_selectors = new LinkedHashMap<String, Selector>();
		IpSelector tdip = new IpSelector();
		tdip.addRange("10.0.255."+ip,"10.0.255."+ip);
		trans_selectors.put("Destination Address", tdip);
		ConditionClause transformation = new ConditionClause(trans_selectors);
		
		NATAction nat_action = new NATAction(NATActionType.PRENAT, transformation);
		GenericRule nat_rule = new GenericRule(nat_action, conditionClause, natRuleName);
		nat_policy.insertRule(nat_rule, ip);
		
	}

}
