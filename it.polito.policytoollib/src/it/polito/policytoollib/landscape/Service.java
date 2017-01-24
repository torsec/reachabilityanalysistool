package it.polito.policytoollib.landscape;

import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

public class Service {
	
	private IpSelector address;
	private PortSelector port;
	
	public Service() {
		this.address = new IpSelector();
		this.port = new PortSelector();
	}

	public Service(IpSelector address, PortSelector port) {
		this.address = address;
		this.port = port;
	}

	public IpSelector getAddress() {
		return address;
	}

	public void setAddress(IpSelector address) {
		this.address = address;
	}

	public PortSelector getPort() {
		return port;
	}

	public void setPort(PortSelector port) {
		this.port = port;
	}
}
