package it.polito.policytoollib.policy.utils;

public enum  PolicyType {
	FILTERING, NAT, VPN, LOGGING, ROUTING;
	
	public String toString(){
		if(this == FILTERING)
			return "FILTERING";
		else if (this == NAT)
			return "NAT";
		else if (this == VPN)
			return "VPN";
		else if (this == LOGGING)
			return "LOGGING";
		else if (this == ROUTING)
			return "ROUTING";
		return "";
	}
	
}
