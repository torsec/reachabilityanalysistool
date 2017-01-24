package it.polito.policytoollib.landscape;

import java.util.Collection;
import java.util.LinkedList;

public class InterfaceToFirewallList
{
	public LinkedList<InterfaceToFirewall> list;
	
	public InterfaceToFirewallList() {
		list = new LinkedList<InterfaceToFirewall>();
	}

	public String toString() {
		String ret = "";
		for(InterfaceToFirewall l : list)
			ret += l.toString() + "\n";
		return ret;
	}
	
	public void addToList(InterfaceToFirewall e)
	{
		if(e.fw==null || e.itf==null) return;
		for(InterfaceToFirewall l : list)
			if(l.fw==e.fw && l.itf.compareTo(e.itf)==0)
				return;
		list.add(e);
	}
	
	public Collection<SecurityControl> getFirewallsFromInterface(String itf)
	{
		LinkedList<SecurityControl> fws = new LinkedList<SecurityControl>();
		for(InterfaceToFirewall l : list)
			if(l.itf.compareTo(itf)==0)
				fws.add(l.fw);
		return fws;
	}
}