package it.polito.policytoollib.exception.rule;

public class InvalidMacAddressException extends Exception {

	static final long serialVersionUID = 14L;
	
	public InvalidMacAddressException(){}

	public InvalidMacAddressException(String msg)
	{
		super(msg);
	}
	
}