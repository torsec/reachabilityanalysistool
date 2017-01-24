package it.polito.policytoollib.test.selector;

import static org.junit.Assert.*;

import java.math.BigInteger;

import junit.framework.Assert;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import org.junit.Test;

public class PortSelectorTest {

	@Test
	public void addRange() throws InvalidIpAddressException, InvalidRangeException{
		
		PortSelector ips = new PortSelector();
		
		ips.addRange("1","5");
		assertEquals("[1 - 5] ", ips.toString());
	
		
		ips.addRange("7","10");
		assertEquals("[1 - 5] [7 - 10] ", ips.toString());
	
		
		ips.addRange("6");
		assertEquals("[1 - 10] ", ips.toString());
	}
	
	@Test(expected=InvalidRangeException.class)
	public void invalidRange() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips = new PortSelector();		
		ips.addRange("1","300000");
	}
	
	@Test
	public void intersection1() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips1 = new PortSelector();
		PortSelector ips2 = new PortSelector();
		
		ips1.addRange("1","5");
		ips2.addRange("5");
		
		ips1.intersection(ips2);
		assertEquals("[5 - 5] ", ips1.toString());
	}
	
	@Test
	public void intersection2() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips1 = new PortSelector();
		PortSelector ips2 = new PortSelector();
		
		ips1.addRange("1","5");
		ips2.addRange("5","9");
		
		ips1.intersection(ips2);
		assertEquals("[5 - 5] ", ips1.toString());
	}
	
	@Test
	public void intersection3() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips1 = new PortSelector();
		PortSelector ips2 = new PortSelector();
		
		ips1.addRange("1","5");
		ips2.addRange("4","9");
		
		ips1.intersection(ips2);
		assertEquals("[4 - 5] ", ips1.toString());
	}
	
	@Test
	public void complement1() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips = new PortSelector();
		
		ips.addRange("1","5");
		
		ips.complement();
		assertEquals("[0 - 0] [6 - 65535] ", ips.toString());
		
		ips.complement();
		assertEquals("[1 - 5] ", ips.toString());
	}
	
	@Test
	public void complement2() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips = new PortSelector();
		
		ips.addRange("1","1");
		
		ips.complement();
		assertEquals("[0 - 0] [2 - 65535] ", ips.toString());
		
		ips.complement();
		assertEquals("[1 - 1] ", ips.toString());
	}
	
	@Test
	public void complement3() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips = new PortSelector();
		
		ips.addRange("1","5");
		
		ips.addRange("7","8");
		
		ips.complement();
		for(BigInteger l:ips.getRanges())
			System.out.println(l);
		System.out.println("-----------------------------------------");
		assertEquals("[0 - 0] [6 - 6] [9 - 65535] ", ips.toString());
		
		
		ips.complement();
		for(BigInteger l:ips.getRanges())
			System.out.println(l);
		System.out.println(ips);
		System.out.println("-----------------------------------------");
		assertEquals("[1 - 5] [7 - 8] ", ips.toString());
	}
	
	@Test
	public void full_empty() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips = new PortSelector();
		ips.complement();
		assertTrue(ips.isFull());
		assertEquals("full",ips.toString());
		ips.empty();
		assertTrue(ips.isEmpty());
		assertEquals("empty",ips.toString());
	}
	
	@Test
	public void isEquivalent() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips1 = new PortSelector();
		PortSelector ips2 = new PortSelector();
		
		ips1.addRange("1","5");
		ips2.addRange("1","5");
		
		assertTrue(ips1.isEquivalent(ips2));
	}
	
	@Test
	public void isPoint() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips = new PortSelector();
		
		ips.addRange("1");
		assertTrue(ips.isPoint());
		
		ips.addRange("2");
		assertFalse(ips.isPoint());
	}
	
	@Test
	public void isSubset1() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips1 = new PortSelector();
		PortSelector ips2 = new PortSelector();
		
		ips1.addRange("1","4");
		ips2.addRange("1","5");
		
		assertTrue(ips1.isSubset(ips2));
	}
	
	@Test
	public void isSubset2() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips1 = new PortSelector();
		PortSelector ips2 = new PortSelector();
		
		ips1.addRange("0","4");
		ips2.addRange("1","5");
		
		assertFalse(ips1.isSubset(ips2));
	}
	
	@Test
	public void isSubset3() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips1 = new PortSelector();
		PortSelector ips2 = new PortSelector();
		
		ips1.addRange("1","5");
		ips2.addRange("1","5");
		
		assertFalse(ips1.isSubset(ips2));
	}
	
	@Test
	public void isSubsetOrEquivalent() throws InvalidIpAddressException, InvalidRangeException{
		PortSelector ips1 = new PortSelector();
		PortSelector ips2 = new PortSelector();
		
		ips1.addRange("1","5");
		ips2.addRange("1","5");
		
		assertTrue(ips1.isSubsetOrEquivalent(ips2));
	}
}
