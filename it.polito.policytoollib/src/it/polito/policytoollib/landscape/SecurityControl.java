package it.polito.policytoollib.landscape;

import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


public class SecurityControl {
	
	private HashMap<String, List<SecurityControl>> connection_fw;
	private HashMap<String, FilteringZone> connection_zone;
	
	private Policy policy;
	
	private Policy NAT;
	
	private Policy VPN;
	
	private Policy routing;
	
	private String name;
	
	private HashMap<String, IpSelector> interfaces_ip;
	private HashMap<String, IpSelector> interfaces_subnet;
	
	public SecurityControl(HashMap<String, IpSelector> interfaces_ip, HashMap<String, IpSelector> interfaces_subnet, String name) {
		this.name = name;
		this.connection_fw = new HashMap<String, List<SecurityControl>>();
		this.connection_zone = new HashMap<String, FilteringZone>();
		this.interfaces_ip = interfaces_ip;
		this.interfaces_subnet = interfaces_subnet;
	}
	
	public void addFW(String interface_name, SecurityControl fw){
		List<SecurityControl> list = connection_fw.get(interface_name);
		if(list!=null)
			list.add(fw);
		else
		{
			LinkedList<SecurityControl> fwList = new LinkedList<SecurityControl>();
			fwList.add(fw);
			connection_fw.put(interface_name, fwList);	
		}
	}

	public void addFilteringZone(String interface_name, FilteringZone zone){
		connection_zone.put(interface_name, zone);
	}
	
	public Policy getPolicy(){
		return policy;
	}
	
	public void setPolicy(Policy policy){
		this.policy = policy;
	}
	
	public Policy getNAT(){
		return NAT;
	}
	
	public void setNAT(Policy NAT){
		this.NAT = NAT;
	}
	
	public Policy getVPN(){
		return VPN;
	}
	
	public void setVPN(Policy VPN){
		this.VPN = VPN;
	}
	
	public Policy getRouting() {
		return routing;
	}

	public void setRouting(Policy routing) {
		this.routing = routing;
	}

	public String getName(){
		return name;
	}
	
	public List<SecurityControl> getFirewalls(){
		LinkedList<SecurityControl> fw_list_list = new LinkedList<SecurityControl>();
		for(List<SecurityControl> fw_list : connection_fw.values())
			fw_list_list.addAll(fw_list);
		return fw_list_list;
	}
	
	public List<SecurityControl> getFirewallsByInterface(String inter){
		return connection_fw.get(inter);
	}
	
	public String getInterfaceByFirewall(SecurityControl f)
	{
		for(Entry<String, List<SecurityControl>> e : connection_fw.entrySet())
		{
			if(e.getValue().contains(f)) return(e.getKey());
		}
		return null;
	}
	
	public Collection<FilteringZone> getFilteringZones(){
		return connection_zone.values();
	}
	
	public HashMap<String, FilteringZone> getFilteringZonesWithInterfaces(){
		return connection_zone;
	}
	
	public FilteringZone getFilteringZone(String inter){
		return connection_zone.get(inter);
	}

	public HashMap<String, IpSelector> getInterfacesIp() {
		return interfaces_ip;
	}
	
	public HashMap<String, IpSelector> getInterfacesSubnet() {
		return interfaces_subnet;
	}

	public List<String> getInterfaces() {
		LinkedList<String> list = new LinkedList<String>();
		list.addAll(interfaces_ip.keySet());
		return list;
	}
	
	public String[] getInterfacesArray() {
		String[] array = new String[interfaces_ip.size()];
		array = interfaces_ip.keySet().toArray(array);
		return array;
	}
	
	public IpSelector getFirewallIpList()
	{
		IpSelector ips = new IpSelector();
		for(IpSelector ip : interfaces_ip.values())
			ips.union(ip);
		return ips;
	}
	/*public String getInterfaceNameFromSubnet(IpSelector i)
	{
		for(IpSelector i2: interface_list.values())
			if(i.isEquivalent(i2))
				
	}*/
	
	public void setInterfaces_subnet(HashMap<String, IpSelector> interfaces_subnet) {
		this.interfaces_subnet = interfaces_subnet;
	}

	public HashMap<String, List<SecurityControl>> getConnection_fw() {
		return connection_fw;
	}

	public void setConnection_fw(HashMap<String, List<SecurityControl>> connection_fw) {
		this.connection_fw = connection_fw;
	}
}
