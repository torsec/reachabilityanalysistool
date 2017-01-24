package it.polito.policytoollib.exception.rule;

public class UnsupportedSelectorException extends Exception{

	static final long serialVersionUID = 12L;
	
	public UnsupportedSelectorException(){}

	public UnsupportedSelectorException(String msg)
	{
		super(msg);
	}

}
