package it.polito.policytoollib.landscape;

public class InterfaceToFirewall
{
	String itf;
	SecurityControl fw;
	
	public InterfaceToFirewall(String itf, SecurityControl fw) {
		super();
		this.itf = itf;
		this.fw = fw;
	}

	@Override
	public String toString() {
		return itf + " " + fw.getName();
	}
}
