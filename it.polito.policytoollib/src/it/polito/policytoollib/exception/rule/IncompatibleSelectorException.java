package it.polito.policytoollib.exception.rule;

public class IncompatibleSelectorException extends Exception{

	static final long serialVersionUID = 12L;
	
	public IncompatibleSelectorException(){}

	public IncompatibleSelectorException(String msg)
	{
		super(msg);
	}

}

