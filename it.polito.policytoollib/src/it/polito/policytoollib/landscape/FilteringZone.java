package it.polito.policytoollib.landscape;

import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidNetException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

import java.util.LinkedList;
import java.util.List;

public class FilteringZone {
	
	private String name;
	
	private SecurityControl fw;
	
	private List<Host> host_list;
	
	private IpSelector IP_Subnet;

	public FilteringZone(String name, String IP_Subnet, SecurityControl fw) throws InvalidIpAddressException, InvalidRangeException, NumberFormatException, InvalidNetException {
		this.name=name;
		this.IP_Subnet = new IpSelector();
		String[] ip_net = IP_Subnet.split("/");
		this.IP_Subnet.addRange(ip_net[0], Integer.valueOf(ip_net[1]).intValue());
		this.host_list = new LinkedList<Host>();
		this.fw = fw;
	}
	
	public FilteringZone(String name, IpSelector IP_Subnet, SecurityControl fw) throws InvalidIpAddressException, InvalidRangeException, NumberFormatException, InvalidNetException {
		this.name=name;
		this.IP_Subnet = IP_Subnet;
		this.host_list = new LinkedList<Host>();
		this.fw = fw;
	}
	
	public IpSelector getIPSubnet(){
		return IP_Subnet;
	}

	public void addHost(Host host){
		host_list.add(host);
	}
	
	public String getName(){
		return name;
	}
	
	public List<Host> getHostList(){
		return host_list;
	}
	
	public SecurityControl getFirewall(){
		return fw;
	}
}
