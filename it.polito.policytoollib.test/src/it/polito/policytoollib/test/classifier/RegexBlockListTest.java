package it.polito.policytoollib.test.classifier;

import static org.junit.Assert.*;
import org.junit.Test;

import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.policy.utils.RegexBlockList;
import it.polito.policytoollib.rule.selector.impl.StandardRegExpSelector;

public class RegexBlockListTest {
	
	@Test
	public void regexBlockListTest(){
		StandardRegExpSelector s = new StandardRegExpSelector("prov.*"); 		
		StandardRegExpSelector s1 = new StandardRegExpSelector("prova.*");
		StandardRegExpSelector s2 = new StandardRegExpSelector("provab.*"); 		
		StandardRegExpSelector s3 = new StandardRegExpSelector(".*ova"); 		
		
		RegexBlockList l = new RegexBlockList(s);
		try {
			l.insert(s1, 1);
			l.insert(s2, 2);
			l.insert(s3, 3);	
		} catch (UnsupportedSelectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		l.printBlocks();
	}
}
