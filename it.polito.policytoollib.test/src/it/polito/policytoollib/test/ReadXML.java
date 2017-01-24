package it.polito.policytoollib.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.HTTPMethodSelector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.ProtocolIDSelector;
import it.polito.policytoollib.rule.selector.impl.StandardRegExpSelector;
import it.polito.policytoollib.rule.selector.impl.UriSelector;

import org.junit.Test;

public class ReadXML {
	
	private PolicyAnalysisModel model;
	private LinkedHashMap<String, Selector> selectorTypes;
	
	public ReadXML() throws Exception{
		model = new PolicyAnalysisModel("TestCases/ReadXML.zip", "readXML");
		selectorTypes = model.getSelectorTypes().getAllSelectorTypes();
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//																													//
//													Test Policy														//
//																													//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	@Test
	public void PolicyNumber() throws Exception {
		assertEquals("Result", 11, model.getPolicyList().size());	
	}
	
	@Test
	public void PolicyName() throws Exception {
		assertEquals("Result", "fwA1", model.getPolicy("fwA1").getName());
		assertEquals("Result", "fwA2", model.getPolicy("fwA2").getName());
		assertEquals("Result", "fwA3", model.getPolicy("fwA3").getName());
		assertEquals("Result", "fwA4", model.getPolicy("fwA4").getName());
		assertEquals("Result", "fwB1", model.getPolicy("fwB1").getName());
		assertEquals("Result", "fwB2", model.getPolicy("fwB2").getName());
		assertEquals("Result", "fwB3", model.getPolicy("fwB3").getName());
		assertEquals("Result", "fwC1", model.getPolicy("fwC1").getName());
		assertEquals("Result", "fwC2", model.getPolicy("fwC2").getName());
		assertEquals("Result", "fwC3", model.getPolicy("fwC3").getName());
		assertEquals("Result", "rA", model.getPolicy("rA").getName());
	}
	
	@Test
	public void PolicyDefaultAction() throws Exception {
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwA1").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwA2").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwA3").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwA4").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwB1").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwB2").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwB3").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwC1").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwC2").getDefaultAction());
		assertEquals("Result", FilteringAction.DENY, model.getPolicy("fwC3").getDefaultAction());
		assertEquals("Result", FilteringAction.ALLOW, model.getPolicy("rA").getDefaultAction());
	}
	
	@Test
	public void PolicySize() throws Exception {
		assertEquals("Result", 3, model.getPolicy("fwA1").getRuleSet().size());
		assertEquals("Result", 1, model.getPolicy("fwA2").getRuleSet().size());
		assertEquals("Result", 1, model.getPolicy("fwA3").getRuleSet().size());
		assertEquals("Result", 1, model.getPolicy("fwA4").getRuleSet().size());
		assertEquals("Result", 3, model.getPolicy("fwB1").getRuleSet().size());
		assertEquals("Result", 1, model.getPolicy("fwB2").getRuleSet().size());
		assertEquals("Result", 1, model.getPolicy("fwB3").getRuleSet().size());
		assertEquals("Result", 2, model.getPolicy("fwC1").getRuleSet().size());
		assertEquals("Result", 0, model.getPolicy("fwC2").getRuleSet().size());
		assertEquals("Result", 1, model.getPolicy("fwC3").getRuleSet().size());
		assertEquals("Result", 0, model.getPolicy("rA").getRuleSet().size());
	}

//	@Test
//	public void Policy_fwA1() throws Exception {
//		for(GenericRule rule:model.getPolicy("fwA1").getRuleSet()){
//			if(rule.getLabel().equals("R1")){
//				assertEquals("Result", "3", rule.getRuleType());
//				assertEquals("Result", FilteringAction.ALLOW, rule.getAction());
//				rule.getSelectors()
//				
//				assertEquals("Result", , );
//			}
//		}
//	}
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//																													//
//                                                    Test RulesType												//
//																													//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Test
	public void selectorTypes() {
		assertEquals("RulesType_1_Size", 8, selectorTypes.size());
		assertEquals("RulesType_1", true, selectorTypes.get("Source Address") instanceof IpSelector);
		assertEquals("RulesType_1", true, selectorTypes.get("Destination Address") instanceof IpSelector);
		assertEquals("RulesType_1", true, selectorTypes.get("Source Port") instanceof PortSelector);
		assertEquals("RulesType_1", true, selectorTypes.get("Destination Port") instanceof PortSelector);
		assertEquals("RulesType_1", true, selectorTypes.get("Protocol Type") instanceof ProtocolIDSelector);
		assertEquals("RulesType_1", true, selectorTypes.get("L4Protocol") instanceof ProtocolIDSelector);
		assertEquals("RulesType_1", true, selectorTypes.get("L7Protocol") instanceof UriSelector);
		assertEquals("RulesType_1", true, selectorTypes.get("Http Method") instanceof HTTPMethodSelector);
	}

	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//																													//
//														Test Landscape												//
//																													//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	@Test
	public void LandscapeSize() {
		assertEquals("FilteringZoneNumber" ,17, model.getLandscape().getZoneList().size());
		assertEquals("FirewallNumber" , 11, model.getLandscape().getFirewallList().size());
		assertEquals("HostsNumber_A1", 1, model.getLandscape().getZoneList().get("ZA1").getHostList().size());
		assertEquals("HostsNumber_A2", 0, model.getLandscape().getZoneList().get("ZA2").getHostList().size());
		assertEquals("HostsNumber_A3", 0, model.getLandscape().getZoneList().get("ZA3").getHostList().size());
		assertEquals("HostsNumber_A4", 0, model.getLandscape().getZoneList().get("ZA4").getHostList().size());
		assertEquals("HostsNumber_A5", 1, model.getLandscape().getZoneList().get("ZA5").getHostList().size());
		assertEquals("HostsNumber_A6", 0, model.getLandscape().getZoneList().get("ZA6").getHostList().size());
		assertEquals("HostsNumber_B1", 0, model.getLandscape().getZoneList().get("ZB1").getHostList().size());
		assertEquals("HostsNumber_B2", 1, model.getLandscape().getZoneList().get("ZB2").getHostList().size());
		assertEquals("HostsNumber_B3", 0, model.getLandscape().getZoneList().get("ZB3").getHostList().size());
		assertEquals("HostsNumber_B4", 0, model.getLandscape().getZoneList().get("ZB4").getHostList().size());
		assertEquals("HostsNumber_B5", 0, model.getLandscape().getZoneList().get("ZB5").getHostList().size());
		assertEquals("HostsNumber_B6", 0, model.getLandscape().getZoneList().get("ZA6").getHostList().size());
		assertEquals("HostsNumber_B7", 0, model.getLandscape().getZoneList().get("ZB7").getHostList().size());
		assertEquals("HostsNumber_C1", 0, model.getLandscape().getZoneList().get("ZC1").getHostList().size());
		assertEquals("HostsNumber_C2", 0, model.getLandscape().getZoneList().get("ZC2").getHostList().size());
		assertEquals("HostsNumber_C3", 1, model.getLandscape().getZoneList().get("ZC3").getHostList().size());
		assertEquals("HostsNumber_C4", 0, model.getLandscape().getZoneList().get("ZC4").getHostList().size());
	}
	
	@Test
	public void FilteringZoneConnection() {
		assertEquals("ZA1_fwA1", "fwA1", model.getLandscape().getZoneList().get("ZA1").getFirewall().getName());
		assertEquals("ZA2_fwA1", "fwA1", model.getLandscape().getZoneList().get("ZA2").getFirewall().getName());
		assertEquals("ZA3_fwA3", "fwA3", model.getLandscape().getZoneList().get("ZA3").getFirewall().getName());
		assertEquals("ZA4_fwA3", "fwA3", model.getLandscape().getZoneList().get("ZA4").getFirewall().getName());
		assertEquals("ZA5_fwA4", "fwA4", model.getLandscape().getZoneList().get("ZA5").getFirewall().getName());
		assertEquals("ZA6_fwA4", "fwA4", model.getLandscape().getZoneList().get("ZA6").getFirewall().getName());
		assertEquals("ZB1_fwB3", "fwB3", model.getLandscape().getZoneList().get("ZB1").getFirewall().getName());
		assertEquals("ZB2_fwB3", "fwB3", model.getLandscape().getZoneList().get("ZB2").getFirewall().getName());
		assertEquals("ZB3_fwB1", "fwB1", model.getLandscape().getZoneList().get("ZB3").getFirewall().getName());
		assertEquals("ZB4_fwB2", "fwB2", model.getLandscape().getZoneList().get("ZB4").getFirewall().getName());
		assertEquals("ZB5_fwB2", "fwB2", model.getLandscape().getZoneList().get("ZB5").getFirewall().getName());
		assertEquals("ZB6_fwB2", "fwB2", model.getLandscape().getZoneList().get("ZB6").getFirewall().getName());
		assertEquals("ZB7_fwB2", "fwB2", model.getLandscape().getZoneList().get("ZB7").getFirewall().getName());
		assertEquals("ZC1_fwC2", "fwC2", model.getLandscape().getZoneList().get("ZC1").getFirewall().getName());
		assertEquals("ZC2_fwC2", "fwC2", model.getLandscape().getZoneList().get("ZC2").getFirewall().getName());
		assertEquals("ZC3_fwC3", "fwC3", model.getLandscape().getZoneList().get("ZC3").getFirewall().getName());
	}
	
	@Test
	public void FirewallFilteringZoneConnection() {
		assertEquals("rA", 0, model.getLandscape().getFirewallList().get("rA").getFilteringZones().size());
		
		assertEquals("fwA1", 2, model.getLandscape().getFirewallList().get("fwA1").getFilteringZones().size());
		assertEquals("fwA1_ZA1", "ZA1", model.getLandscape().getFirewallList().get("fwA1").getFilteringZone("fwA1.eth0").getName());
		assertEquals("fwA1_ZA2", "ZA2", model.getLandscape().getFirewallList().get("fwA1").getFilteringZone("fwA1.eth1").getName());
		assertEquals("fwA2", 0, model.getLandscape().getFirewallList().get("fwA2").getFilteringZones().size());
		assertEquals("fwA3", 2, model.getLandscape().getFirewallList().get("fwA3").getFilteringZones().size());
		assertEquals("fwA3_ZA3", "ZA3", model.getLandscape().getFirewallList().get("fwA3").getFilteringZone("fwA3.eth1").getName());
		assertEquals("fwA3_ZA4", "ZA4", model.getLandscape().getFirewallList().get("fwA3").getFilteringZone("fwA3.eth2").getName());
		assertEquals("fwA4", 2, model.getLandscape().getFirewallList().get("fwA4").getFilteringZones().size());
		assertEquals("fwA4_ZA5", "ZA5", model.getLandscape().getFirewallList().get("fwA4").getFilteringZone("fwA4.eth0").getName());
		assertEquals("fwA4_ZA6", "ZA6", model.getLandscape().getFirewallList().get("fwA4").getFilteringZone("fwA4.eth1").getName());
		
		assertEquals("fwB1", 1, model.getLandscape().getFirewallList().get("fwB1").getFilteringZones().size());
		assertEquals("fwB1_ZB3", "ZB3", model.getLandscape().getFirewallList().get("fwB1").getFilteringZone("fwB1.eth4").getName());
		assertEquals("fwB2", 4, model.getLandscape().getFirewallList().get("fwB2").getFilteringZones().size());
		assertEquals("fwB2_ZB4", "ZB4", model.getLandscape().getFirewallList().get("fwB2").getFilteringZone("fwB2.eth2").getName());
		assertEquals("fwB2_ZB5", "ZB5", model.getLandscape().getFirewallList().get("fwB2").getFilteringZone("fwB2.eth3").getName());
		assertEquals("fwB2_ZB6", "ZB6", model.getLandscape().getFirewallList().get("fwB2").getFilteringZone("fwB2.eth4").getName());
		assertEquals("fwB2_ZB7", "ZB7", model.getLandscape().getFirewallList().get("fwB2").getFilteringZone("fwB2.eth5").getName());
		assertEquals("fwB3", 2, model.getLandscape().getFirewallList().get("fwB3").getFilteringZones().size());
		assertEquals("fwB3_ZB1", "ZB1", model.getLandscape().getFirewallList().get("fwB3").getFilteringZone("fwB3.eth2").getName());
		assertEquals("fwB3_ZB2", "ZB2", model.getLandscape().getFirewallList().get("fwB3").getFilteringZone("fwB3.eth3").getName());
		
		assertEquals("fwC1", 0, model.getLandscape().getFirewallList().get("fwC1").getFilteringZones().size());
		assertEquals("fwC2", 2, model.getLandscape().getFirewallList().get("fwC2").getFilteringZones().size());
		assertEquals("fwC2_ZC1", "ZC1", model.getLandscape().getFirewallList().get("fwC2").getFilteringZone("fwC2.eth1").getName());
		assertEquals("fwC2_ZC2", "ZC2", model.getLandscape().getFirewallList().get("fwC2").getFilteringZone("fwC2.eth2").getName());
		assertEquals("fwC3", 2, model.getLandscape().getFirewallList().get("fwC3").getFilteringZones().size());
		assertEquals("fwC3_ZC3", "ZC3", model.getLandscape().getFirewallList().get("fwC3").getFilteringZone("fwC3.eth1").getName());
		assertEquals("fwC3_ZC4", "ZC4", model.getLandscape().getFirewallList().get("fwC3").getFilteringZone("fwC3.eth2").getName());
	}
	
	@Test
	public void FirewallFirewallConnection(){
		assertEquals("rA", 4, model.getLandscape().getFirewallList().get("rA").getFirewalls().size());
		assertEquals("rA", "fwA1", model.getLandscape().getFirewallList().get("rA").getFirewallsByInterface("rA.eth0").get(0).getName());
		assertEquals("rA", "fwA2", model.getLandscape().getFirewallList().get("rA").getFirewallsByInterface("rA.eth1").get(0).getName());
		assertEquals("rA", "fwB1", model.getLandscape().getFirewallList().get("rA").getFirewallsByInterface("rA.eth2").get(0).getName());
		assertEquals("rA", "fwC1", model.getLandscape().getFirewallList().get("rA").getFirewallsByInterface("rA.eth3").get(0).getName());
		
		assertEquals("fwA1", 1, model.getLandscape().getFirewallList().get("fwA1").getFirewalls().size());
		assertEquals("fwA1", "rA", model.getLandscape().getFirewallList().get("fwA1").getFirewallsByInterface("fwA1.eth2").get(0).getName());
		assertEquals("fwA2", 2, model.getLandscape().getFirewallList().get("fwA2").getFirewalls().size());
		assertEquals("fwA2", "rA", model.getLandscape().getFirewallList().get("fwA2").getFirewallsByInterface("fwA2.eth0").get(0).getName());
		assertEquals("fwA2", "fwA3", model.getLandscape().getFirewallList().get("fwA2").getFirewallsByInterface("fwA2.eth1").get(0).getName());
		assertEquals("fwA3", 2, model.getLandscape().getFirewallList().get("fwA3").getFirewalls().size());
		assertEquals("fwA3", "fwA2", model.getLandscape().getFirewallList().get("fwA3").getFirewallsByInterface("fwA3.eth0").get(0).getName());
		assertEquals("fwA3", "fwA4", model.getLandscape().getFirewallList().get("fwA3").getFirewallsByInterface("fwA3.eth3").get(0).getName());
		assertEquals("fwA4", 1, model.getLandscape().getFirewallList().get("fwA4").getFirewalls().size());
		assertEquals("fwA4", "fwA3", model.getLandscape().getFirewallList().get("fwA4").getFirewallsByInterface("fwA4.eth3").get(0).getName());
		
		assertEquals("fwB1", 4, model.getLandscape().getFirewallList().get("fwB1").getFirewalls().size());
		assertEquals("fwB2", 2, model.getLandscape().getFirewallList().get("fwB2").getFirewalls().size());
		assertEquals("fwB3", 2, model.getLandscape().getFirewallList().get("fwB3").getFirewalls().size());
		
		assertEquals("fwC1", 4, model.getLandscape().getFirewallList().get("fwC1").getFirewalls().size());
		assertEquals("fwC2", 1, model.getLandscape().getFirewallList().get("fwC2").getFirewalls().size());
		assertEquals("fwC3", 1, model.getLandscape().getFirewallList().get("fwC3").getFirewalls().size());
	}
	
	@Test
	public void FirewallInterfaces(){
		assertEquals("rA", 4, model.getLandscape().getFirewallList().get("rA").getInterfacesIp().size());
		
		assertEquals("fwA1", 3, model.getLandscape().getFirewallList().get("fwA1").getInterfacesIp().size());
		assertEquals("fwA2", 3, model.getLandscape().getFirewallList().get("fwA2").getInterfacesIp().size());
		assertEquals("fwA3", 4, model.getLandscape().getFirewallList().get("fwA3").getInterfacesIp().size());
		assertEquals("fwA4", 4, model.getLandscape().getFirewallList().get("fwA4").getInterfacesIp().size());
		
		assertEquals("fwB1", 5, model.getLandscape().getFirewallList().get("fwB1").getInterfacesIp().size());
		assertEquals("fwB2", 6, model.getLandscape().getFirewallList().get("fwB2").getInterfacesIp().size());
		assertEquals("fwB3", 4, model.getLandscape().getFirewallList().get("fwB3").getInterfacesIp().size());
		
		assertEquals("fwC1", 4, model.getLandscape().getFirewallList().get("fwC1").getInterfacesIp().size());
		assertEquals("fwC2", 3, model.getLandscape().getFirewallList().get("fwC2").getInterfacesIp().size());
		assertEquals("fwC3", 3, model.getLandscape().getFirewallList().get("fwC3").getInterfacesIp().size());
	}
	
	@Test
	public void LandscapeQuery() throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		HashSet<String> pathset = new HashSet<String>();
		assertEquals("Query_ZA1_ZA2", 1, model.getEquivalentFW("ZA1", "ZA2").getPolicyList().size());
		assertEquals("Query_ZA1_ZA3", 1, model.getEquivalentFW("ZA1", "ZA3").getPolicyList().size());
		assertEquals("Query_ZA1_ZA4", 1, model.getEquivalentFW("ZA1", "ZA4").getPolicyList().size());
		assertEquals("Query_ZA1_ZA5", 1, model.getEquivalentFW("ZA1", "ZA5").getPolicyList().size());
		assertEquals("Query_ZA1_ZA6", 1, model.getEquivalentFW("ZA1", "ZA6").getPolicyList().size());
		
		assertEquals("Query_ZA2_ZA1", 1, model.getEquivalentFW("ZA2", "ZA1").getPolicyList().size());
		assertEquals("Query_ZA2_ZA3", 1, model.getEquivalentFW("ZA2", "ZA3").getPolicyList().size());
		assertEquals("Query_ZA2_ZA4", 1, model.getEquivalentFW("ZA2", "ZA4").getPolicyList().size());
		assertEquals("Query_ZA2_ZA5", 1, model.getEquivalentFW("ZA2", "ZA5").getPolicyList().size());
		assertEquals("Query_ZA2_ZA6", 1, model.getEquivalentFW("ZA2", "ZA6").getPolicyList().size());
		
		assertEquals("Query_ZA3_ZA1", 1, model.getEquivalentFW("ZA3", "ZA1").getPolicyList().size());
		assertEquals("Query_ZA3_ZA2", 1, model.getEquivalentFW("ZA3", "ZA2").getPolicyList().size());
		assertEquals("Query_ZA3_ZA4", 1, model.getEquivalentFW("ZA3", "ZA4").getPolicyList().size());
		assertEquals("Query_ZA3_ZA5", 1, model.getEquivalentFW("ZA3", "ZA5").getPolicyList().size());
		assertEquals("Query_ZA3_ZA6", 1, model.getEquivalentFW("ZA3", "ZA6").getPolicyList().size());
		
		assertEquals("Query_ZA4_ZA1", 1, model.getEquivalentFW("ZA4", "ZA1").getPolicyList().size());
		assertEquals("Query_ZA4_ZA2", 1, model.getEquivalentFW("ZA4", "ZA2").getPolicyList().size());
		assertEquals("Query_ZA4_ZA4", 1, model.getEquivalentFW("ZA4", "ZA4").getPolicyList().size());
		assertEquals("Query_ZA4_ZA5", 1, model.getEquivalentFW("ZA4", "ZA5").getPolicyList().size());
		assertEquals("Query_ZA4_ZA6", 1, model.getEquivalentFW("ZA4", "ZA6").getPolicyList().size());
		
		assertEquals("Query_ZA5_ZA1", 1, model.getEquivalentFW("ZA5", "ZA1").getPolicyList().size());
		assertEquals("Query_ZA5_ZA2", 1, model.getEquivalentFW("ZA5", "ZA2").getPolicyList().size());
		assertEquals("Query_ZA5_ZA3", 1, model.getEquivalentFW("ZA5", "ZA3").getPolicyList().size());
		assertEquals("Query_ZA5_ZA4", 1, model.getEquivalentFW("ZA5", "ZA4").getPolicyList().size());
		assertEquals("Query_ZA5_ZA6", 1, model.getEquivalentFW("ZA5", "ZA6").getPolicyList().size());
		
		assertEquals("Query_ZA6_ZA1", 1, model.getEquivalentFW("ZA6", "ZA1").getPolicyList().size());
		assertEquals("Query_ZA6_ZA2", 1, model.getEquivalentFW("ZA6", "ZA2").getPolicyList().size());
		assertEquals("Query_ZA6_ZA3", 1, model.getEquivalentFW("ZA6", "ZA3").getPolicyList().size());
		assertEquals("Query_ZA6_ZA4", 1, model.getEquivalentFW("ZA6", "ZA4").getPolicyList().size());
		assertEquals("Query_ZA6_ZA5", 1, model.getEquivalentFW("ZA6", "ZA5").getPolicyList().size());
		
		assertEquals("Query_ZA1_ZB1", 4, model.getEquivalentFW("ZA1", "ZB1").getPolicyList().size());
		
		
		assertEquals("Query_ZA1_ZB2", 4, model.getEquivalentFW("ZA1", "ZB2").getPolicyList().size());
		
		pathset.clear();
		for(LinkedList<Policy> p_list:model.getEquivalentFW("ZA1", "ZA2").getPolicyList()){
			String path="";
			for(Policy p:p_list)
				path=path+p.getName()+">";
			pathset.add(path);
		}
		assertEquals("fwA1", true, pathset.contains("fwA1>"));
		
		pathset.clear();
		for(LinkedList<Policy> p_list:model.getEquivalentFW("ZA1", "ZA3").getPolicyList()){
			String path="";
			for(Policy p:p_list)
				path=path+p.getName()+">";
			pathset.add(path);
		}
		assertEquals("fwA1-fwA3", true, pathset.contains("fwA1>rA>fwA2>fwA3>"));
		
		pathset.clear();
		for(LinkedList<Policy> p_list:model.getEquivalentFW("ZA1", "ZA5").getPolicyList()){
			String path="";
			for(Policy p:p_list)
				path=path+p.getName()+">";
			pathset.add(path);

		}
		assertEquals("fwA1-fwA4", true, pathset.contains("fwA1>rA>fwA2>fwA3>fwA4>"));
		
		pathset.clear();
		for(LinkedList<Policy> p_list:model.getEquivalentFW("ZA3", "ZA6").getPolicyList()){
			String path="";
			for(Policy p:p_list)
				path=path+p.getName()+">";
			pathset.add(path);
		}
		assertEquals("fwA3-fwA4", true, pathset.contains("fwA3>fwA4>"));
			
		pathset.clear();
		for(LinkedList<Policy> p_list:model.getEquivalentFW("ZA1", "ZB2").getPolicyList()){
			String path="";
			for(Policy p:p_list)
				path=path+p.getName()+">";
			pathset.add(path);
		}
		assertEquals("fwA1-fwB3", true, pathset.contains("fwA1>rA>fwC1>fwB1>fwB3>"));
		assertEquals("fwA1-fwB3", true, pathset.contains("fwA1>rA>fwC1>fwB1>fwB2>fwB3>"));
		assertEquals("fwA1-fwB3", true, pathset.contains("fwA1>rA>fwB1>fwB3>"));
		assertEquals("fwA1-fwB3", true, pathset.contains("fwA1>rA>fwB1>fwB2>fwB3>"));
		
	}
	
//	@Test
//	public void Policy() throws Exception {
//		assertEquals("Result", , model.getPolicy("fwA1").get
//		assertEquals("Result", , model.getPolicy("fwA2").get
//		assertEquals("Result", , model.getPolicy("fwA3").get
//		assertEquals("Result", , model.getPolicy("fwA4").get
//		assertEquals("Result", , model.getPolicy("fwB1").get
//		assertEquals("Result", , model.getPolicy("fwB2").get
//		assertEquals("Result", , model.getPolicy("fwB3").get
//		assertEquals("Result", , model.getPolicy("fwC1").get
//		assertEquals("Result", , model.getPolicy("fwC2").get
//		assertEquals("Result", , model.getPolicy("fwC3").get
//	}
}
