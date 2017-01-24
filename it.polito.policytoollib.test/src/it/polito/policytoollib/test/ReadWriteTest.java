package it.polito.policytoollib.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.wrapper.XMLTranslator;

public class ReadWriteTest {
	
	public static void main(String[] args) throws Exception {
//		
//		
//		

//		
		
		if(args.length != 2){
			System.out.println("java -jar ReadWriteTest.jar <inputFile> <outputFile>");
			return;
		}
		
		System.out.println();
		System.out.println("==========================================================");
		
		System.out.println("SelectorTypes File : selectorTypes.xml");
		System.out.println("Input File : " + args[0]);
		System.out.println("Output File : " + args[1]);
		
		
		System.out.println("==========================================================");
		
		System.out.println("Reading SelectorTypes File");
		SelectorTypes selectorTypes = new SelectorTypes();
		
		InputStream input = new FileInputStream("selectorTypes.conf");
		
		selectorTypes.addSelectorTypes(input);
		
		System.out.println("Complete");
		
		System.out.println("==========================================================");
		
		System.out.println("Reading Input File");
		
		XMLTranslator xml = new XMLTranslator(selectorTypes);
		Policy policy = xml.readPolicy(args[0]);		
		
		System.out.println("Complete");
		
		System.out.println("==========================================================");
		
		System.out.println("Writing Output File");
		
		xml.writeOriginalRuleSetFromPolicy(policy, args[1]);
		
		System.out.println("Complete");
		
	}

}
