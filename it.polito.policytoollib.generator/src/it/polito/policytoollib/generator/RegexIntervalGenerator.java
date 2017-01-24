package it.polito.policytoollib.generator;

public class RegexIntervalGenerator {
	
	public static RegexIntervalGenerator instance=null;
	
	private RegexIntervalGenerator(){}
	
	public static RegexIntervalGenerator getInstance(){
		if (instance==null)
			instance = new RegexIntervalGenerator();
		
		return instance;
	}
	
	public String getRegex(int value){
		
		String regex="";
		int s,e;
		
		while (value!=0){
			s = value%10;
			value=value/10;
			e = value%10;
			regex = "["+s+"-"+e+"]" + regex;
			value=value/10;
		}
		System.out.println(regex);
		return regex;
		
	}

}
