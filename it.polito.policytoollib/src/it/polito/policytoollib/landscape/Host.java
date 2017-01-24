package it.polito.policytoollib.landscape;

import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import java.util.HashMap;

public class Host {
	
	private HashMap<String,Service> services;
		
	private HashMap<String, IpSelector> interfaces_ip;
	private IpSelector ipAddress;
	private FilteringZone zone;
	
	private String name;
	
	private String id;
	
	public Host(String name, HashMap<String, IpSelector> interfaces_ip, FilteringZone zone){
		this.name = name;
		this.zone=zone;
		this.interfaces_ip = interfaces_ip;
		this.ipAddress = new IpSelector();
		for(IpSelector s : interfaces_ip.values())
			ipAddress.union(s);
		this.services = new HashMap<String,Service>();
		
		zone.addHost(this);
	}
	
	public Host(String name, String id, HashMap<String, IpSelector> interfaces_ip, FilteringZone zone){
		this.name = name;
		this.zone=zone;
		this.id=id;
		this.interfaces_ip = interfaces_ip;
		this.ipAddress = new IpSelector();
		for(IpSelector s : interfaces_ip.values())
			ipAddress.union(s);
		this.services = new HashMap<String,Service>();
		
		zone.addHost(this);
	}
	
	public void addService(String name, IpSelector ip, PortSelector port){
		services.put(name, new Service(ip, port));
	}

	public void addServices(HashMap<String, Service> services) {
		this.services.putAll(services);
	}
	
	public HashMap<String, Service> getServices(){
		return services;
	}

	public FilteringZone getFilteringZone(){
		return zone;
	}
	
	public IpSelector getIPAddress(String itf_name){
		return interfaces_ip.get(itf_name);
	}
	
	public HashMap<String, IpSelector> getInterfaces(){
		return interfaces_ip;
	}
	
	public String getName(){
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void addInterface(String itf_name, IpSelector new_ip)
	{
		IpSelector itf_ip = interfaces_ip.get(itf_name);
		if(itf_ip!=null)
			itf_ip.union(new_ip);
		else
			interfaces_ip.put(itf_name, new_ip);
		ipAddress.union(new_ip);
	}

	public IpSelector getIPAddress() {
		return ipAddress;
	}
}
