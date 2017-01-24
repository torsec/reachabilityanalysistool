package it.polito.policytoollib.model;


import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.landscape.Landscape;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.policy.impl.EquivalenPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.tools.AnomalyAnalyser;
import it.polito.policytoollib.policy.tools.PolicyComparator;
import it.polito.policytoollib.policy.tools.ReachabilityAnalyser;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalFormGenerator;
import it.polito.policytoollib.policy.translation.morphisms.FMRMorphism;
import it.polito.policytoollib.policy.translation.semilattice.SemiLatticeGenerator;
import it.polito.policytoollib.policy.translation.semilattice.Semilattice;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.wrapper.XMLTranslator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PolicyAnalysisModel {
	
	private HashMap<String,Policy> policy_list;
	
	private HashMap<String,Policy> nat_list;
	
	private HashMap<String,Policy> vpn_list;
	
	private HashMap<String,Policy> routing_list;
	
	private HashMap<FilteringZone, HashMap<FilteringZone, ComposedPolicy>> equivalentFW_list;
	
	private String fileName;
	
	private String name;
	
	private SelectorTypes selectorTypes;
	
	private Landscape landscape;
	
	private AnomalyAnalyser anomalyAnalyzer;
	
	private ReachabilityAnalyser reachabilityAnalyser;
	
	private PolicyComparator policyComparator;
	
	private Boolean isModified = true;


	public PolicyAnalysisModel(String fileName, String name) throws Exception{
		this.name = name;
		this.fileName = fileName;
		this.policy_list = new HashMap<String,Policy>();
		this.nat_list = new HashMap<String,Policy>();
		this.vpn_list = new HashMap<String,Policy>();
		this.routing_list = new HashMap<String,Policy>();
		this.selectorTypes = new SelectorTypes(this);
		this.equivalentFW_list = new HashMap<FilteringZone, HashMap<FilteringZone, ComposedPolicy>>();
		readInputFile();
		this.anomalyAnalyzer = new AnomalyAnalyser(selectorTypes);
		this.reachabilityAnalyser =  new ReachabilityAnalyser(this.landscape, equivalentFW_list);
		this.policyComparator = new PolicyComparator();
	}
	
	public PolicyAnalysisModel(){
		this.name = "";
		this.fileName = "";
		this.policy_list = new HashMap<String,Policy>();
		this.nat_list = new HashMap<String,Policy>();
		this.vpn_list = new HashMap<String,Policy>();
		this.routing_list = new HashMap<String,Policy>();
		this.selectorTypes = new SelectorTypes(this);
		this.equivalentFW_list = new HashMap<FilteringZone, HashMap<FilteringZone, ComposedPolicy>>();
		this.landscape = new Landscape(new HashMap<String, SecurityControl>(), new HashMap<String, FilteringZone>());
		this.anomalyAnalyzer = new AnomalyAnalyser(selectorTypes);
		this.reachabilityAnalyser =  new ReachabilityAnalyser(landscape, equivalentFW_list);
		this.policyComparator = new PolicyComparator();
	}
//
//	public PolicyAnalysisModel(Landscape landscape){
//		this.name = "";
//		this.fileName = "";
//		this.policy_list = new HashMap<String,Policy>();
//		this.nat_list = new HashMap<String,Policy>();
//		this.vpn_list = new HashMap<String,Policy>();
//		this.routing_list = new HashMap<String,Policy>();
//		this.selectorTypes = new SelectorTypes(this);
//		this.landscape = landscape;
//		this.anomalyAnalyzer = new AnomalyAnalyser(selectorTypes);
//		this.reachabilityAnalyser =  new ReachabilityAnalyser(landscape, equivalentFW_list);
//		this.policyComparator = new PolicyComparator();
//	}
	
	public String getName(){
		return this.name;
	}
	
	public void addPolicy(Policy policy){
		if(policy==null) return;
		this.policy_list.put(policy.getName(),policy);
	}
	
	public Collection<Policy> getPolicyList(){
		return policy_list.values();
	}
	
	public Policy getPolicy(String name){
		return policy_list.get(name);
	}
	
	public void addNATPolicy(Policy policy){
		if(policy==null) return;
		this.nat_list.put(policy.getName(),policy);
	}
	
	public Collection<Policy> getNATPolicyList(){
		return nat_list.values();
	}
	
	public Policy getNAT(String name){
		return nat_list.get(name);
	}
	
	public void addVPNPolicy(Policy policy){
		if(policy==null) return;
		this.vpn_list.put(policy.getName(),policy);
	}
	
	public Collection<Policy> getVPNPolicyList(){
		return vpn_list.values();
	}
	
	public Policy getVPN(String name){
		return vpn_list.get(name);
	}
	
	public void addRoutingPolicy(Policy policy){
		if(policy==null) return;
		this.routing_list.put(policy.getName(),policy);
	}
	
	public Collection<Policy> getRoutingPolicyList(){
		return routing_list.values();
	}
	
	public Policy getRouting(String name){
		return routing_list.get(name);
	}
	
	public void removePolicy(Policy policy)
	{
		switch(policy.getPolicyType())
		{
			case FILTERING:
				policy_list.remove(policy.getName());
				break;
			case NAT:
				nat_list.remove(policy.getName());
				break;
			case ROUTING:
				break;
			case VPN:
				vpn_list.remove(policy.getName());
				break;
			default:
				break; 
		}
	}
	
	public Landscape getLandscape(){
		return landscape;
	}
	
	public SelectorTypes getSelectorTypes(){
		return selectorTypes;
	}
	
	public Set<PolicyAnomaly> getSingleAnomalies(Policy policy){
		return anomalyAnalyzer.getIntraPolicyAnomalies(policy);
	}
	
	public Set<PolicyAnomaly> getSingleAnomalies(String policy) {
		return anomalyAnalyzer.getIntraPolicyAnomalies(policy_list.get(policy));
	}
	
	public Set<PolicyAnomaly> getDistributedConflicts(String START, String END) throws Exception {
		
		ComposedPolicy policy = getEquivalentFW_list().get(getLandscape().getZoneList().get(START)).get(getLandscape().getZoneList().get(END));
		
		EquivalenPolicy equPolicy = new EquivalenPolicy(policy, policy.getCan(), policy.getSL());
		
		
		return anomalyAnalyzer.getInterPolicyAnomalies(equPolicy);
	}
	
	public ComposedPolicy getEquivalentFW(String START, String END) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		
		Policy p = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, START+"-"+END);
		ComposedPolicy policy = landscape.getComposedPolicy(START, END);
		
		
		if(policy.getRuleSet().size()!=0){
			
			
			CanonicalFormGenerator canGen = CanonicalFormGenerator.getInstance(policy, this.selectorTypes);
			canGen.generateClosure();
			
			CanonicalForm can = canGen.getCanonicalForm();
			
			SemiLatticeGenerator slgen = new SemiLatticeGenerator();
			slgen.generateSemilattice(can);
			
			FMRMorphism fmr = new FMRMorphism(can);
			
			int i=1;
			for(GenericRule r:fmr.exportRules()){
				p.insertRule(r, i++);
			}
			

			
			policy.setCan(can);
			policy.setSL(can.getSemiLattice());
		}
		
		policy.setFMRPolicy(p);
		
		return policy;
	}
	
	public ComposedPolicy getEquivalentFW(String host1, String host2, int routes) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		return landscape.getComposedPolicy(host1, host2, routes);
	}
	
	public HashSet<GenericRule> setClientQuerry(String clientIP) throws Exception{
		LinkedHashMap<String, Selector> selHash = new LinkedHashMap<String, Selector>();
		
		IpSelector sip = new IpSelector();
		sip.addRange(clientIP);
		selHash.put("Source Address", sip);
		
		ConditionClause zone_rule = new ConditionClause(selHash);
		
		return executeQuerry(zone_rule);
	}
	
	public HashMap<Host,ComposedPolicy> setClientQuerry(String clientHost, int routes) throws Exception{
		if(!landscape.getHost_list().containsKey(clientHost)) return null; //TODO avvisare l'utente che l'host non esiste
		HashMap<Host,ComposedPolicy> clientPolicies = new HashMap<Host,ComposedPolicy>();
		for(String h: landscape.getHost_list().keySet())
		{
			if(h.compareTo(clientHost)!=0)
			{
				ComposedPolicy clientToHPolicy = getEquivalentFW(clientHost,h,routes); //ottengo regole tra client e h
				Host host = landscape.getHost_list().get(h);
				clientPolicies.put(host, clientToHPolicy);
			}
		}
		return clientPolicies; //risultato da mostrare all'utente, le policy dall'host specificato a tutti gli altri host 
	}
	
	public HashSet<GenericRule> setServerQuerry(String serverIP, String serverPort) throws Exception{
		LinkedHashMap<String, Selector> selHash = new LinkedHashMap<String, Selector>();
		
		IpSelector dip = new IpSelector();
		dip.addRange(serverIP);
		selHash.put("Destination Address", dip);
		
		PortSelector dp = new PortSelector();
		dp.addRange(serverPort);
		selHash.put("Destination Port", dp);
		
		ConditionClause zone_rule = new ConditionClause(selHash);
		
		return executeQuerry(zone_rule);
		
	}

	public HashMap<Host, ComposedPolicy> setServerQuerry(String serverHost, String serverPort, int routes) throws Exception{
		if(!landscape.getHost_list().containsKey(serverHost)) return null; //TODO avvisare l'utente che l'host non esiste
		HashMap<Host,ComposedPolicy> serverPolicies = new HashMap<Host,ComposedPolicy>();
		LinkedHashMap<String, Selector> selHash = new LinkedHashMap<String, Selector>();
		PortSelector dp = new PortSelector();
		dp.addRange(serverPort);
		selHash.put("Destination Port", dp);
		ConditionClause zone_rule = new ConditionClause(selHash);
		for(String h: landscape.getHost_list().keySet())
		{
			if(h.compareTo(serverHost)!=0)
			{
				ComposedPolicy hToServerPolicy = getEquivalentFW(h,serverHost,routes); //ottengo regole tra client e h
				Host host = landscape.getHost_list().get(h);
				for(GenericRule r: hToServerPolicy.getRuleSet())
					r.getConditionClause().intersection(zone_rule);
				serverPolicies.put(host, hToServerPolicy);
			}
		}
		return serverPolicies; //risultato da mostrare all'utente, le policy dall'host specificato a tutti gli altri host
	}
	
	public HashSet<GenericRule> setReachabilityQuerry() throws Exception{
		LinkedHashMap<String, Selector> selHash = new LinkedHashMap<String, Selector>();
		
		ConditionClause zone_rule = new ConditionClause(selHash);
		
		
		for(String selctorName : this.selectorTypes.getAllSelectorTypes().keySet()){
			Selector s = this.selectorTypes.getAllSelectorTypes().get(selctorName).selectorClone();
			s.full();
			zone_rule.addSelector(selctorName, s);
		}
		
		
		return executeQuerry(zone_rule);
	}
	
	public HashSet<GenericRule> executeQuerry(ConditionClause zone_rule) throws Exception{
		getEquivalentFW_list();
		return reachabilityAnalyser.executeQuerry(zone_rule);
	}
	
	public HashMap<Host,HashMap<Host,ComposedPolicy>> setReachabilityQuerry(int routes) throws Exception{
		HashMap<Host,HashMap<Host,ComposedPolicy>> ret = new HashMap<Host, HashMap<Host,ComposedPolicy>>();
		for(Host h: landscape.getHost_list().values())
			ret.put(h,setClientQuerry(h.getName(), routes));
		return ret;
	}
	
	public Set<Semilattice<GenericRule>> compare(Policy policy) throws Exception{
		getEquivalentFW_list();
		Policy mergedPolicy = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "");
		int i=0;
		for(FilteringZone z1 : equivalentFW_list.keySet()){
			for(FilteringZone z2 : equivalentFW_list.keySet()){
				if(z1!=z2){
					Policy p = equivalentFW_list.get(z1).get(z2).getFMRPolicy();
					for(GenericRule rule:p.getRuleSet()){
						int ii = ((FMRResolutionStrategy)p.getResolutionStrategy()).getExternalData(rule);
						mergedPolicy.insertRule(rule, i+ii);
					}
					i=i+p.getRuleSet().size()+1;
				}
			}
		}
//		System.out.println("----------------------------------------------------");
//		for(GenericRule rule:mergedPolicy.getRuleSet()){
//			System.out.println(((FMRResolutionStrategy)mergedPolicy.getResolutionStrategy()).getExternalData(rule));
//			System.out.println(rule);
//		}
				
		return policyComparator.compare(policy, mergedPolicy, selectorTypes);
	}
	
	private void readInputFile() throws Exception{
		
		File file = new File(this.fileName);
		if(!file.exists()){
			throw new FileNotFoundException();
		} else {
			ZipFile zip_file = new ZipFile(this.fileName);
			XMLTranslator xml = new XMLTranslator(this.selectorTypes);
			
			for(Enumeration e = zip_file.entries(); e.hasMoreElements();)
			{   
			    ZipEntry entry = (ZipEntry) e.nextElement();
		    	if(entry.getName().equals("selectorTypes.xml"))
		    		this.selectorTypes.addSelectorTypes(zip_file.getInputStream(entry));
		    	if(entry.getName().equals("landscape.xml"))
		    		this.landscape = xml.readLandscape(zip_file.getInputStream(entry));
			}	
			
			for(Enumeration e = zip_file.entries(); e.hasMoreElements();)
			{   
			    ZipEntry entry = (ZipEntry) e.nextElement();
			    if(entry.getName().startsWith("policy/")){
			    	if(entry.getName().split("/").length==2){
				    	String policyName = entry.getName().split("/")[1].replace(".xml", "");
				    	Policy policy = xml.readPolicy(zip_file.getInputStream(entry));
				    	this.policy_list.put(policy.getName(),policy);
				    	this.landscape.getFirewallList().get(policyName).setPolicy(policy);
			    	}
			    }
			    if(entry.getName().startsWith("nat/")){
			    	if(entry.getName().split("/").length==2){
				    	String policyName = entry.getName().split("/")[1].replace(".xml", "");
				    	Policy policy = xml.readPolicy(zip_file.getInputStream(entry));
				    	this.nat_list.put(policy.getName(),policy);
				    	this.landscape.getFirewallList().get(policyName).setNAT(policy);
			    	}
			    }
			    if(entry.getName().startsWith("secure/")){
			    	if(entry.getName().split("/").length==2){
				    	String policyName = entry.getName().split("/")[1].replace(".xml", "");
				    	Policy policy = xml.readPolicy(zip_file.getInputStream(entry));
				    	this.vpn_list.put(policy.getName(),policy);
				    	this.landscape.getFirewallList().get(policyName).setVPN(policy);
			    	}
			    }
			}		
		}
	}

	public HashMap<FilteringZone, HashMap<FilteringZone, ComposedPolicy>> getEquivalentFW_list() throws Exception{
			
		
		if(isModified){
			for(FilteringZone fz1:landscape.getZoneList().values()){
				HashMap<FilteringZone, ComposedPolicy> fzhm = new HashMap<FilteringZone, ComposedPolicy>();
				equivalentFW_list.put(fz1, fzhm);
				for(FilteringZone fz2:landscape.getZoneList().values()){
					fzhm.put(fz2, getEquivalentFW(fz1.getName(), fz2.getName()));
				}
			}
		}
		isModified = false;
		
		return equivalentFW_list;
	}

	public void setLandscape(Landscape landscape) {
		this.landscape = landscape;
	}
}
