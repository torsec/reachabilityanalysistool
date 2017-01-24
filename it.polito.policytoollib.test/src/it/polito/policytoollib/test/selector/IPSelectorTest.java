package it.polito.policytoollib.test.selector;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidNetException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import org.junit.Test;

public class IPSelectorTest {

	@Test
	public void addRange() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException{
		
		IpSelector ips = new IpSelector();
		
		ips.addRange("1.1.1.1","1.1.1.5");
		assertEquals("[1.1.1.1 - 1.1.1.5] ", ips.toString());
		
		ips.addRange("1.1.1.7","1.1.1.10");
		assertEquals("[1.1.1.1 - 1.1.1.5] [1.1.1.7 - 1.1.1.10] ", ips.toString());
		
		ips.addRange("1.1.1.6");
		assertEquals("[1.1.1.1 - 1.1.1.10] ", ips.toString());
	}
	
	@Test(expected=InvalidIpAddressException.class)
	public void invalidIpAddress1() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips = new IpSelector();		
		ips.addRange("1.1.1.300","300");
	}
	
	@Test(expected=InvalidIpAddressException.class)
	public void invalidIpAddress2() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException{
		IpSelector ips = new IpSelector();		
		ips.addRange("1.1.1.300");
	}
	
	@Test(expected=InvalidIpAddressException.class)
	public void invalidIpAddress3() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips = new IpSelector();		
		ips.addRange("1.1.1.300","1.1.1.300");
	}
	
	@Test(expected=InvalidIpAddressException.class)
	public void invalidRange() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips = new IpSelector();		
		ips.addRange("1.1.1.1","300.300.300.300");
		
	}
	
	@Test
	public void intersection1() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.5");
		ips2.addRange("1.1.1.5");
		
		ips1.intersection(ips2);
		assertEquals("[1.1.1.5 - 1.1.1.5] ", ips1.toString());
	}
	
	@Test
	public void intersection2() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.5");
		ips2.addRange("1.1.1.5","1.1.1.9");
		
		ips1.intersection(ips2);
		assertEquals("[1.1.1.5 - 1.1.1.5] ", ips1.toString());
	}
	
	@Test
	public void intersection3() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.5");
		ips2.addRange("1.1.1.4","1.1.1.9");
		
		ips1.intersection(ips2);
		assertEquals("[1.1.1.4 - 1.1.1.5] ", ips1.toString());
	}
	
	@Test
	public void complement1() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips = new IpSelector();
		
		ips.addRange("1.1.1.1","1.1.1.5");
		
		ips.complement();
		assertEquals("[0.0.0.0 - 1.1.1.0] [1.1.1.6 - 255.255.255.255] ", ips.toString());
		
		ips.complement();
		assertEquals("[1.1.1.1 - 1.1.1.5] ", ips.toString());
	}
	
	@Test
	public void complement2() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips = new IpSelector();
		
		ips.addRange("1.1.1.1","1.1.1.1");
		
		ips.complement();
		assertEquals("[0.0.0.0 - 1.1.1.0] [1.1.1.2 - 255.255.255.255] ", ips.toString());
		
		ips.complement();
		assertEquals("[1.1.1.1 - 1.1.1.1] ", ips.toString());
	}
	
	@Test
	public void complement3() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips = new IpSelector();
		
		ips.addRange("1.1.1.1","1.1.1.5");
		
		ips.addRange("1.1.1.7","1.1.1.8");
		
		ips.complement();
		assertEquals("[0.0.0.0 - 1.1.1.0] [1.1.1.6 - 1.1.1.6] [1.1.1.9 - 255.255.255.255] ", ips.toString());
		
		ips.complement();
		assertEquals("[1.1.1.1 - 1.1.1.5] [1.1.1.7 - 1.1.1.8] ", ips.toString());
	}
	
	@Test
	public void full_empty() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips = new IpSelector();
		ips.complement();
		assertTrue(ips.isFull());
		assertEquals("full",ips.toString());
		ips.empty();
		assertTrue(ips.isEmpty());
		assertEquals("empty",ips.toString());
	}
	
	@Test
	public void isEquivalent() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.5");
		ips2.addRange("1.1.1.1","1.1.1.5");
		
		assertTrue(ips1.isEquivalent(ips2));
	}
	
	@Test
	public void isPoint() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException{
		IpSelector ips = new IpSelector();
		
		ips.addRange("1.1.1.1");
		assertTrue(ips.isPoint());
		
		ips.addRange("1.1.1.2");
		assertFalse(ips.isPoint());
	}
	
	@Test
	public void isSubset1() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.1");
		ips2.addRange("1.1.1.1","1.1.1.2");
		
		assertTrue(ips1.isSubset(ips2));
	}
	
	@Test
	public void isSubset2() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.0","1.1.1.4");
		ips2.addRange("1.1.1.1","1.1.1.5");
		
		assertFalse(ips1.isSubset(ips2));
	}
	
	@Test
	public void isSubset3() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.5");
		ips2.addRange("1.1.1.1","1.1.1.5");
		
		assertFalse(ips1.isSubset(ips2));
	}
	
	@Test
	public void isSubsetOrEquivalent1() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.5");
		ips2.addRange("1.1.1.1","1.1.1.5");
		
		assertTrue(ips1.isSubsetOrEquivalent(ips2));
		assertTrue(ips2.isSubsetOrEquivalent(ips1));
	}
	
	@Test
	public void isSubsetOrEquivalent2() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.1");
		ips2.addRange("1.1.1.1","1.1.1.2");
		
		assertTrue(ips1.isSubsetOrEquivalent(ips2));
		assertFalse(ips2.isSubsetOrEquivalent(ips1));
	}
	
	@Test
	public void isSubsetOrEquivalent3() throws InvalidIpAddressException, InvalidRangeException{
		IpSelector ips1 = new IpSelector();
		IpSelector ips2 = new IpSelector();
		
		ips1.addRange("1.1.1.1","1.1.1.1");
		ips2.addRange("1.1.1.1","1.1.1.1");
		
		assertTrue(ips1.isSubsetOrEquivalent(ips2));
		assertTrue(ips2.isSubsetOrEquivalent(ips1));
	}
}
