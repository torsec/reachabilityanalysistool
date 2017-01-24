package it.polito.policytoollib.landscape;

import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalFormGenerator;
import it.polito.policytoollib.policy.translation.resolver.RuleTransformationResolver;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.RoutingAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.InterfaceSelector;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Landscape {

	
	private HashMap<String, FilteringZone> zone_list;
	
	private HashMap<String, SecurityControl> fw_list;
	
	private HashMap<String, Host> host_list;
	
	private LinkedList<String[]> link_list;  
	
	public HashMap<String, Host> getHost_list() {
		return host_list;
	}

	public void addLink(String endpoint1, String endpoint2)
	{
		String[] link = {endpoint1, endpoint2};
		link_list.add(link);
	}
	
	public LinkedList<String[]> getLink_list() {
		return link_list;
	}

	public void setLink_list(LinkedList<String[]> link_list) {
		this.link_list = link_list;
	}

	public Landscape(HashMap<String, SecurityControl> fw_list, HashMap<String, FilteringZone> zone_list) {
		this.zone_list = zone_list;
		this.fw_list = fw_list;
		this.host_list = new HashMap<String, Host>(); 
		this.link_list = new LinkedList<String[]>();
	}

	public Landscape(HashMap<String, SecurityControl> fw_list, HashMap<String, FilteringZone> zone_list, HashMap<String,Host> host_list) {
		this.zone_list = zone_list;
		this.fw_list = fw_list;
		this.host_list = host_list; 
		this.link_list = new LinkedList<String[]>();
	}
	
	public HashMap<String, FilteringZone> getZoneList(){
		return zone_list;
	}
	
	public HashMap<String, SecurityControl> getFirewallList() {
		return this.fw_list;
	}
	
	public ComposedPolicy getComposedPolicy(String zone1, String zone2) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		return getComposedPolicy(zone_list.get(zone1), zone_list.get(zone2));
	}
	
	public ComposedPolicy getComposedPolicy(FilteringZone START, FilteringZone END) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		
		LinkedList<LinkedList<SecurityControl>> firewall_list;
		
		if(START.getFirewall()==END.getFirewall()){
			firewall_list = new LinkedList<LinkedList<SecurityControl>>();
			LinkedList<SecurityControl> fw_list = new LinkedList<SecurityControl>();
			fw_list.add(START.getFirewall());
			firewall_list.add(fw_list);			
		} else {
			LinkedList<SecurityControl> visited = new LinkedList<SecurityControl>();
			firewall_list = getPath(visited, START.getFirewall(), END.getFirewall(), START.getFirewall());
		}
		
		RuleTransformationResolver transform = new RuleTransformationResolver();
		
		LinkedList<LinkedList<Policy>> policy_list = transform.getPolicyList(firewall_list);
		ComposedPolicy policy = new ComposedPolicy(policy_list, PolicyType.FILTERING, "CP-"+START.getName()+"-"+END.getName());
		
		LinkedHashMap<String, Selector> selHash = new LinkedHashMap<String, Selector>();
		selHash.put("Source Address", START.getIPSubnet());
		selHash.put("Destination Address", END.getIPSubnet());
		
		ConditionClause zone_rule = new ConditionClause(selHash);
		
		for(Policy p:policy.getOriginalPolicy()){
			for(GenericRule r:p.getRuleSet()){
				if(!r.isIntersecting(zone_rule))
					policy.removeRule(r);
			}
		}
		
		
		
		return policy;
	}
	
	public ComposedPolicy getComposedPolicy(String host1, String host2, int routes) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		Host h1 = null, h2 = null;
		boolean found = false;
		for(FilteringZone f: zone_list.values())
		{
			for(Host h: f.getHostList())
			{
				if(h1==null && h.getName().compareTo(host1)==0) h1=h;
				if(h2==null && h.getName().compareTo(host2)==0) h2=h;
				if(h1!=null && h2!=null)
				{
					found=true;
					break;
				}
			}
			if(found) break;
		}
		if(h1==null || h2==null) return null; //TODO avvisa l'utente che uno dei due host non esiste
		return getComposedPolicy(h1, h2, routes);
	}
	
	public ComposedPolicy getComposedPolicy(Host h1, Host h2, int routes) throws UnmanagedRuleException, OperationNotPermittedException, Exception{
		
		LinkedList<LinkedList<SecurityControl>> firewall_list;
		FilteringZone START = h1.getFilteringZone();
		FilteringZone END = h2.getFilteringZone();
		if(START.getFirewall()==END.getFirewall()){
			firewall_list = new LinkedList<LinkedList<SecurityControl>>();
			LinkedList<SecurityControl> fw_list = new LinkedList<SecurityControl>();
			fw_list.add(START.getFirewall());
			firewall_list.add(fw_list);			
		} else {
			LinkedList<SecurityControl> visited = new LinkedList<SecurityControl>();
			firewall_list = getPath(visited, START.getFirewall(), END.getFirewall(), START.getFirewall(),routes,h2.getIPAddress());
		}
		
		RuleTransformationResolver transform = new RuleTransformationResolver();
		
		LinkedList<LinkedList<Policy>> policy_list = transform.getPolicyList(firewall_list);
		ComposedPolicy policy = new ComposedPolicy(policy_list, PolicyType.FILTERING, "CP-"+START.getName()+"-"+END.getName());
		
		LinkedHashMap<String, Selector> selHash = new LinkedHashMap<String, Selector>();
		selHash.put("Source Address", h1.getIPAddress());
		selHash.put("Destination Address", h2.getIPAddress());
		
		ConditionClause zone_rule = new ConditionClause(selHash);
		
		for(Policy p:policy.getOriginalPolicy()){
			for(GenericRule r:p.getRuleSet()){
				//if(!r.isIntersecting(zone_rule))
					//policy.removeRule(r);
			}
		}
		
		return policy;
	}

	private LinkedList<LinkedList<SecurityControl>> getPath(LinkedList<SecurityControl> visited, SecurityControl START, SecurityControl END, SecurityControl last) {
		 
		LinkedList<LinkedList<SecurityControl>> return_list = new LinkedList<LinkedList<SecurityControl>>();
    	
    	if(visited.isEmpty())
    		visited.add(START);
    	
    	Collection<SecurityControl> nodes = last.getFirewalls();
 
        for (SecurityControl node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(END)) {
                visited.add(node);
                return_list.add((LinkedList<SecurityControl>)visited.clone());
                visited.removeLast();
                break;
            }
        }
        // in breadth-first, recursion needs to come after visiting adjacent nodes
        for (SecurityControl node : nodes) {
            if (visited.contains(node) || node.equals(END)) {
                continue;
            }
            visited.addLast(node);
            return_list.addAll(getPath(visited, START, END, node));
            visited.removeLast();
        }
        
        return return_list;
    }

	//return paths to host h2 using at most the specified number of routes from last firewall routing table
	private LinkedList<LinkedList<SecurityControl>> getPath(LinkedList<SecurityControl> visited, SecurityControl START, SecurityControl END, SecurityControl last, int routes, IpSelector h2) {
		 
		LinkedList<LinkedList<SecurityControl>> return_list = new LinkedList<LinkedList<SecurityControl>>();
    	
    	if(visited.isEmpty())
    		visited.add(START);
    	
    	Collection<SecurityControl> nodes = new LinkedList<SecurityControl>();
    	Policy routing = last.getRouting();
    	if(routing!=null)
    	{	
	    	HashMap<Integer,Collection<SecurityControl>> nexthop_list = new HashMap<Integer,Collection<SecurityControl>>();
	    	//nexthop list deve associare metrica a gruppo di fw
	    	//poi prendere n gruppi quanti le rotte, unisco e ottengo il nodes da usare per calcolare i path
	    	for(GenericRule r: routing.getRuleSet())
	    	{
	    		Selector s = r.getConditionClause().getSelectors().get("Destination Address");
	    		if(h2.isSubsetOrEquivalent(s))
	    		{
	    			LinkedList<SecurityControl> nexthop = new LinkedList<SecurityControl>();
	    			InterfaceSelector itf = (InterfaceSelector) r.getConditionClause().getSelectors().get("Interface");
	    			IpSelector gateway = (IpSelector) r.getConditionClause().getSelectors().get("Gateway Address");
	    			if(itf!=null && gateway!=null)
	    			{
	    				//se ho entrambe cerco il fw gateway tra i fw direttamente connessi all'interfaccia
	    				//se lo trovo lo aggiungo, altrimenti con questa regola non si inoltra il pacchetto
	    				String itf_name = itf.getInterfaceName()[itf.getFirstAssignedValue()];
	    				for(SecurityControl f : last.getFirewallsByInterface(itf_name))
	    					if(gateway.isSubsetOrEquivalent(f.getFirewallIpList()))
	    						nexthop.add(f);
	    			}
	    			else if(itf!=null)
	    			{
	    				//tutti i fw sulla subnet
	    				String itf_name = itf.getInterfaceName()[itf.getFirstAssignedValue()];
	    				for(SecurityControl f : last.getFirewallsByInterface(itf_name))
	    					nexthop.add(f);
	    			}
	    			else if(gateway!=null)
	    			{
	    				for(SecurityControl f : last.getFirewalls())
	    					if(gateway.isSubsetOrEquivalent(f.getFirewallIpList()))
	    						nexthop.add(f);
	    			}
	    			nexthop_list.put(((RoutingAction)r.getAction()).getMetric(),nexthop);
	    		}
	    	}
	    	
	    	//255 � il valore massimo per una metrica in fwb
	    	int j=0;
	    	for(int i=0; i<256 && j<routes; i++)
	    	{
	    		Collection<SecurityControl> nexthop = nexthop_list.get(i);
	    		if(nexthop==null) continue;
	    		if(nexthop.isEmpty()) continue;
	    		j++;
	    		boolean found=false;
	    		for(SecurityControl f: nexthop)
	    		{
	    			for(SecurityControl f2: nodes)
	    			{
		        		if(f==f2)
		        		{
		        			found=true;
		        			break;
		        		}
		        		nodes.add(f);
		        	}
	    			if(found)
	    			{
	    				found = false;
	    				continue;
	    			}
	    		}
	    	}
    	}	
    	else nodes = last.getFirewalls();	
	    	
        for (SecurityControl node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(END)) {
                visited.add(node);
                return_list.add((LinkedList<SecurityControl>)visited.clone());
                visited.removeLast();
                break;
            }
        }
        // in breadth-first, recursion needs to come after visiting adjacent nodes
        for (SecurityControl node : nodes) {
            if (visited.contains(node) || node.equals(END)) {
                continue;
            }
            visited.addLast(node);
            return_list.addAll(getPath(visited, START, END, node));
            visited.removeLast();
        }
        
        return return_list;
    }
}
