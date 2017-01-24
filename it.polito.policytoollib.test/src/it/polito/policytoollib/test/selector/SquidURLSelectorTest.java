package it.polito.policytoollib.test.selector;

import static org.junit.Assert.*;
import it.polito.policytoollib.rule.selector.impl.SquidURLSelector;
import it.polito.policytoollib.rule.selector.impl.SquidURLType;

import org.junit.Test;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.OtherOperations;
import dk.brics.automaton.RegExp;

public class SquidURLSelectorTest {

	
	@Test
	public void test(){
		SquidURLSelector sur = new SquidURLSelector(SquidURLType.URL_REGEX);
		
		SquidURLSelector sur2 = new SquidURLSelector(SquidURLType.DSTDOMAIN_REGEX);
		
		String s1 = "aba/xyz";
		String s2 = "abc";
		
		sur.setRegExp(s1);
		//System.out.println("SUR: "+sur);
		
		sur2.setRegExp(s2);
		//System.out.println("SUR2: "+sur2);
		
		Automaton a = (new RegExp(".*"+s1+".*")).toAutomaton();
		Automaton b = (new RegExp(".*"+s2+".*")).toAutomaton();
		
		System.out.println("AUTOMATON A: "+OtherOperations.toRegExp(a));
		System.out.println("AUTOMATON B: "+OtherOperations.toRegExp(b));
		Automaton c = BasicOperations.intersection(a, b);
		//c.determinize();
		System.out.println("AUTOMATON C: ");
		System.out.println(a);
		System.out.println(OtherOperations.toRegExp(c));
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("IsInt: "+OtherOperations.intersecting(a, b));
		System.out.println("IsEq: "+OtherOperations.equivalent(a, b));
		System.out.println("IsSub(a,b): "+OtherOperations.subsetNotEquivalent(a, b));
		System.out.println("IsSubEQ(a,b): "+BasicOperations.subsetOf(a, b));
		System.out.println("IsSub(b,a): "+OtherOperations.subsetNotEquivalent(b, a));
		System.out.println("IsSubEQ(b.a): "+BasicOperations.subsetOf(b, a));

		//System.out.println("Squid sel: "+sur);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		
		System.out.println("IsInt: "+sur.isIntersecting(sur2));
		System.out.println("IsEq: "+sur.isEquivalent(sur2));
		System.out.println("IsSub(a,b): "+sur.isSubset(sur2));
		System.out.println("IsSubEQ(a,b): "+sur.isSubsetOrEquivalent(sur2));
		System.out.println("IsSub(b,a): "+sur2.isSubset(sur));
		System.out.println("IsSubEQ(b.a): "+sur2.isSubsetOrEquivalent(sur));
		
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		
		 a = sur.getReducedAutomaton();
		 b = sur2.getReducedAutomaton();
		//a.determinize();
//		System.out.println("1sur2 isSingleton: "+b.isSingleton());
//		System.out.println("1sur runs sur2: "+ a.run(b.getSingleton()));
		System.out.println("IsInt: "+OtherOperations.intersecting(a, b));
//		System.out.println();
		
//		System.out.println("2sur2 isSingleton: "+b.isSingleton());
//		System.out.println("2sur runs sur2: "+ a.run(b.getSingleton()));
		System.out.println("IsEq: "+OtherOperations.equivalent(a, b));
//		System.out.println();
		
//		System.out.println("3sur2 isSingleton: "+b.isSingleton());
//		System.out.println("3sur runs sur2: "+ a.run(b.getSingleton()));
		System.out.println("IsSub(a,b): "+OtherOperations.subsetNotEquivalent(a, b));
//		System.out.println();
		
//		System.out.println("4sur2 isSingleton: "+b.isSingleton());
//		System.out.println("4sur runs sur2: "+ a.run(b.getSingleton()));
		System.out.println("IsSubEQ(a,b): "+BasicOperations.subsetOf(a, b));
//		System.out.println();
		
//		System.out.println("5sur2 isSingleton: "+b.isSingleton());
		//System.out.println("5sur runs sur2: "+ a.run(b.getSingleton()));
		System.out.println("IsSub(b,a): "+OtherOperations.subsetNotEquivalent(b, a));
//		System.out.println();
		
//		System.out.println("6sur2 isSingleton: "+b.isSingleton());
//		System.out.println("6sur runs sur2: "+ a.run(b.getSingleton()));
		System.out.println("IsSubEQ(b.a): "+BasicOperations.subsetOf(b, a));
		
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		
		sur.intersection(sur2);
		
		System.out.println("Squid sel intersected: "+sur);
	}
}
