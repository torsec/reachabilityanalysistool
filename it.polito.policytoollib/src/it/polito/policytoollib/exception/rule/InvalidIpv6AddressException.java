package it.polito.policytoollib.exception.rule;

public class InvalidIpv6AddressException extends Exception {

	static final long serialVersionUID = 14L;
	
	public InvalidIpv6AddressException(){}

	public InvalidIpv6AddressException(String msg)
	{
		super(msg);
	}
	
}
