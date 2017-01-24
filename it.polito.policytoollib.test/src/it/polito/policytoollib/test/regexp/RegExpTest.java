package it.polito.policytoollib.test.regexp;

import static org.junit.Assert.*;

import java.util.Set;


import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.impl.StandardRegExpSelector;

import org.junit.Test;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.OtherOperations;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.ResolveRegexEquationSystem;

public class RegExpTest {
	
	@Test
	public void automatonToRegexp(){
		System.out.println("c: "+ (Character.MIN_VALUE));
		RegExp re2 = new RegExp(".*;\\.google\\.it");
		RegExp re = new RegExp("a.*b");
		
		Automaton a = re.toAutomaton();
		Automaton b = re2.toAutomaton();
		
		System.out.println("IsInt: "+OtherOperations.intersecting(a, b));
		System.out.println("IsEq: "+OtherOperations.equivalent(a, b));
		System.out.println("IsSub(a,b): "+OtherOperations.subsetNotEquivalent(a, b));
		System.out.println("IsSubEQ(a,b): "+BasicOperations.subsetOf(a, b));
		System.out.println("IsSub(b,a): "+OtherOperations.subsetNotEquivalent(b, a));
		System.out.println("IsSubEQ(b.a): "+BasicOperations.subsetOf(b, a));
		
		a.determinize();
		System.out.println(a);
		
		System.out.println("*****************************");
		
		System.out.println(a.run("aaaci"));

		
		System.out.println("*****************************");
		
		String st = OtherOperations.toRegExp(a);
		
		System.out.println("*****************************");
		
		System.out.println("Conv: "+st);
	}
	
	
	@Test
	public void equationSolver() {
		ResolveRegexEquationSystem res = ResolveRegexEquationSystem.getInstance();
		
		RegExp re = new RegExp("o*v");
		
		Automaton a = re.toAutomaton();
		
		System.out.println(a);
		
		res.solve(a);
	}

	@Test
	public void union() throws InvalidIpAddressException, InvalidRangeException {

		RegExp re1 = new RegExp(".*www");
		RegExp re2 = new RegExp("[d-z]*");

		RegExp reUnion = new RegExp("([a-c])|([d-z])");

		Automaton aUnion = reUnion.toAutomaton();

		Automaton a1 = re1.toAutomaton();
		Automaton a2 = re2.toAutomaton();

		Automaton res = a1.union(a2);

		// res.minimize();
		// res.determinize();
		//
		// aUnion.minimize();
		// aUnion.determinize();

		//System.out.println(res.getNumberOfStates());
		assertEquals(17,res.getNumberOfTransitions());
		assertEquals(6,res.getNumberOfStates());
		
		
		
		//System.out.println(aUnion);
		assertEquals(1,aUnion.getNumberOfTransitions());
		assertEquals(2,aUnion.getNumberOfStates());

		
		assertFalse(res.subsetOf(aUnion));
		assertFalse(OtherOperations.equivalent(res, aUnion));

		assertFalse(aUnion.subsetOf(res));
		assertFalse(OtherOperations.equivalent(aUnion, res));

		// Pattern pattern = Pattern.compile("$google.it");

		Set<String> matching = aUnion.getFiniteStrings();

		assertEquals(26, matching.size());
	}

	
	@Test
	public void regexp(){

		
		
		RegExp r1 = new RegExp("[a-d][l-p]");
		RegExp r2 = new RegExp("[c-f][n-r]");
		
		
		Automaton a1 = r1.toAutomaton();
		Automaton a2 = r2.toAutomaton();
		
		//System.out.println("*****A1*******\n"+a1.toString());
		
		//System.out.println("*****A2*******\n"+a2.toString());
		
		Automaton a3 = BasicOperations.intersection(a1, a2);
		
		Automaton a4 = BasicOperations.intersection(a2, a1);
		
		//System.out.println("*****A3*******\n"+a3.toString());
		
		System.out.println(a3.isEmpty());
		
		System.out.println(a3.subsetOf(a2));
		
		System.out.println(a3.subsetOf(a1));
		
		System.out.println(a1.subsetOf(a1));
		
		System.out.println(a2.subsetOf(a2));
		
		System.out.println("*****MODIFIED*******");
		
		System.out.println(OtherOperations.equivalent(a3, a1));
		
		System.out.println(OtherOperations.equivalent(a3, a2));
		
		System.out.println(OtherOperations.equivalent(a1.clone(), a1));
		
		System.out.println(OtherOperations.equivalent(a1, a1.clone()));
		
		System.out.println(OtherOperations.equivalent(a2.clone(), a2));
		
		System.out.println(OtherOperations.equivalent(a2, a2.clone()));
		
		System.out.println(OtherOperations.equivalent(a4, a3));
		
		System.out.println(OtherOperations.equivalent(a3, a4));
		
		System.out.println("**************************\n****************************");
		RegExp r4 = new RegExp("[a-c][1-6][l-p][k-q]");
		RegExp r5 = new RegExp("[b][5][p-z][q-u]");
		
		Automaton a10 = r4.toAutomaton();
		Automaton a11 = r5.toAutomaton();
		
		a10 = a10.union((new RegExp("[d-e]")).toAutomaton());
		a10 = a10.union((new RegExp("[i-l]")).toAutomaton());
		a10 = a10.union((new RegExp("[n-p]")).toAutomaton());
		a10 = a10.union((new RegExp("[s-z]")).toAutomaton());
		//a11 = a11.union((new RegExp("[s-z]")).toAutomaton());
		
		a11 = a11.union((new RegExp("[a-c]")).toAutomaton());
		a11 = a11.union((new RegExp("[i-l]")).toAutomaton());
		a11 = a11.union((new RegExp("[n-p]")).toAutomaton());
		a11 = a11.union((new RegExp("[s-z]")).toAutomaton());
		
		System.out.println(a10);
		
		//a11 = a11.union((new RegExp("[n-r]")).toAutomaton());
		System.out.println(a11);
		Automaton a12 = BasicOperations.intersection(a10, a11);
		System.out.println("**************************\n****************************");
		System.out.println("is: "+OtherOperations.intersecting(a10, a11));
		System.out.println("Eq: "+OtherOperations.equivalent(a10, a11));
		System.out.println(a12);
		
		System.out.println("**************************\n****************************");
		
		String [] value = new String[1];
		value[0] = "2[0-4][0-9]";
		
		StandardRegExpSelector rs1 = new StandardRegExpSelector();
		for (int k=0;k<value.length;k++)
			rs1.addRange(value[k]);
		
		value[0] = "2[0-4][0-9]";
		StandardRegExpSelector rs2 = new StandardRegExpSelector();
		for (int k=0;k<value.length;k++)
			rs2.addRange(value[k]);

		
		System.out.println("*********");
		System.out.println(rs1);
		System.out.println("*********");
		System.out.println(rs2);
		System.out.println("*********");
		boolean v;
		v = rs1.isIntersecting(rs2);
		System.out.println("int: "+v);
		v = rs1.isEquivalent(rs2);
		System.out.println("eq: "+v);
		v = rs1.isSubset(rs2);
		System.out.println("sub: "+v);
		v = rs1.isSubsetOrEquivalent(rs2);
		System.out.println("subeq: "+v);
		

	}
}
