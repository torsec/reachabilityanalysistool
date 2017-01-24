package it.polito.policytoollib.exception.rule;

public class InvalidPortNumberException extends Exception {

	static final long serialVersionUID = 16L;
	
	public InvalidPortNumberException(){}

	public InvalidPortNumberException(String msg)
	{
		super(msg);
	}
}
