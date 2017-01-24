package it.polito.policytool.ui.wizard.Landscape;

import java.util.HashMap;
import java.util.List;

import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

public class Interface {

	SecurityControl f;
	String name;
	IpSelector ip;
	HashMap<SecurityControl,String> otherFwWithItf;
	String fzName;
	IpSelector fzSubnet;
	List<Host> fzHosts;
	
	public Interface(SecurityControl f) {
		this.f = f;
	}
	
	public Interface(SecurityControl f, String itf) {
		this.f = f;
		name = itf;
		ip = f.getInterfacesIp().get(itf);
		if(f.getFirewallsByInterface(itf)==null)
		{
			otherFwWithItf = new HashMap<SecurityControl, String>();
			for(SecurityControl f2 : f.getConnection_fw().get(itf))
			{
				String itf2 = f2.getInterfaceByFirewall(f);
				if(itf2 != null)
					otherFwWithItf.put(f2, itf2);
			}
		}
		else
		{
			FilteringZone fz = f.getFilteringZonesWithInterfaces().get(name); 
			fzName = fz.getName();
			fzSubnet = fz.getIPSubnet();
			fzHosts = fz.getHostList();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IpSelector getIp() {
		return ip;
	}

	public void setIp(IpSelector ip) {
		this.ip = ip;
	}

	public HashMap<SecurityControl, String> getOtherFwWithItf() {
		return otherFwWithItf;
	}

	public void setOtherFwWithItf(HashMap<SecurityControl, String> otherFwWithItf) {
		this.otherFwWithItf = otherFwWithItf;
	}

	public String getFzName() {
		return fzName;
	}

	public void setFzName(String fzName) {
		this.fzName = fzName;
	}

	public IpSelector getFzSubnet() {
		return fzSubnet;
	}

	public void setFzSubnet(IpSelector fzSubnet) {
		this.fzSubnet = fzSubnet;
	}

	public List<Host> getFzHosts() {
		return fzHosts;
	}

	public void setFzHosts(List<Host> fzHosts) {
		this.fzHosts = fzHosts;
	}
	
	
}
