package it.polito.policytoollib.test.rule;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedHashMap;

import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidNetException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import org.junit.Test;

public class ConditionClauseTest {

	@Test
	public void read() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException {
		LinkedHashMap<String, Selector> hashMap = new LinkedHashMap<String, Selector>();

		IpSelector ips = new IpSelector();
		ips.addRange("1.1.1.1");
		hashMap.put("SIP", ips);

		ConditionClause cc = new ConditionClause(hashMap);


	}

	@Test
	public void isPoint() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException {
		LinkedHashMap<String, Selector> hashMap = new LinkedHashMap<String, Selector>();

		IpSelector ips = new IpSelector();
		ips.addRange("1.1.1.1");
		hashMap.put("SIP", ips);

		ConditionClause cc = new ConditionClause(hashMap);

		HashSet<String> selctorsName = new HashSet<String>();
		selctorsName.add("SIP");

		assertTrue(cc.isPoint(selctorsName));

		selctorsName.add("SP");
		assertFalse(cc.isPoint(selctorsName));
	}

	@Test
	public void isEmpty() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException {
		ConditionClause cc = new ConditionClause(null);
		assertTrue(cc.isEmpty());

		LinkedHashMap<String, Selector> hashMap = new LinkedHashMap<String, Selector>();
		cc = new ConditionClause(hashMap);
		assertFalse(cc.isEmpty());

		IpSelector ips = new IpSelector();
		hashMap.put("SIP", ips);
		assertTrue(cc.isEmpty());

		ips.addRange("1.1.1.1");
		assertFalse(cc.isEmpty());
	}

	@Test(expected = IncompatibleSelectorException.class)
	public void setSelector() throws InvalidIpAddressException, InvalidRangeException, IncompatibleSelectorException, InvalidNetException {
		LinkedHashMap<String, Selector> hashMap = new LinkedHashMap<String, Selector>();

		IpSelector ips = new IpSelector();
		ips.addRange("1.1.1.1");
		hashMap.put("SIP", ips);

		ConditionClause cc = new ConditionClause(hashMap);

		cc.setSelector("SP", new PortSelector());

		cc.setSelector("SP", new IpSelector());
	}

	@Test
	public void isConditionEquivalent() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException {
		LinkedHashMap<String, Selector> hashMap1 = new LinkedHashMap<String, Selector>();

		IpSelector ips1 = new IpSelector();
		ips1.addRange("1.1.1.1");
		hashMap1.put("SIP", ips1);

		ConditionClause cc1 = new ConditionClause(hashMap1);

		LinkedHashMap<String, Selector> hashMap2 = new LinkedHashMap<String, Selector>();

		IpSelector ips2 = new IpSelector();
		ips2.addRange("1.1.1.1");
		hashMap2.put("SIP", ips2);

		ConditionClause cc2 = new ConditionClause(hashMap2);

		assertTrue(cc1.isConditionEquivalent(cc2));

		ips2.addRange("1.1.1.2");

		assertFalse(cc1.isConditionEquivalent(cc2));
	}

	@Test
	public void isConditionSubsetOrEquivalent() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException {
		LinkedHashMap<String, Selector> hashMap1 = new LinkedHashMap<String, Selector>();

		IpSelector ips1 = new IpSelector();
		ips1.addRange("1.1.1.1");
		hashMap1.put("SIP", ips1);

		ConditionClause cc1 = new ConditionClause(hashMap1);

		LinkedHashMap<String, Selector> hashMap2 = new LinkedHashMap<String, Selector>();

		IpSelector ips2 = new IpSelector();
		ips2.addRange("1.1.1.1");
		hashMap2.put("SIP", ips2);

		ConditionClause cc2 = new ConditionClause(hashMap2);

		assertTrue(cc1.isConditionSubsetOrEquivalent(cc2));

		ips1.addRange("1.1.1.2");

		assertFalse(cc1.isConditionSubsetOrEquivalent(cc2));
	}

	@Test
	public void isCorrelated() throws InvalidIpAddressException, InvalidRangeException, InvalidNetException {
		LinkedHashMap<String, Selector> hashMap1 = new LinkedHashMap<String, Selector>();

		IpSelector ips1 = new IpSelector();
		ips1.addRange("1.1.1.2", "1.1.1.3");
		hashMap1.put("SIP", ips1);
		PortSelector ps1 = new PortSelector();
		ps1.addRange("2", "3");
		hashMap1.put("SP", ps1);

		ConditionClause cc1 = new ConditionClause(hashMap1);

		LinkedHashMap<String, Selector> hashMap2 = new LinkedHashMap<String, Selector>();

		IpSelector ips2 = new IpSelector();
		ips2.addRange("1.1.1.1", "1.1.1.2");
		hashMap2.put("SIP", ips2);
		PortSelector ps2 = new PortSelector();
		ps2.addRange("1", "2");
		hashMap2.put("SP", ps2);

		ConditionClause cc2 = new ConditionClause(hashMap2);
		

		assertTrue(cc1.isCorrelated(cc2));

		ips2.addRange("1.1.1.2");

		assertFalse(cc1.isCorrelated(cc2));
	}

	@Test
	public void isIntersecting() throws InvalidIpAddressException, InvalidRangeException, IncompatibleSelectorException, InvalidNetException {
		LinkedHashMap<String, Selector> hashMap1 = new LinkedHashMap<String, Selector>();

		IpSelector ips1 = new IpSelector();
		ips1.addRange("1.1.1.1", "1.1.1.2");
		hashMap1.put("SIP", ips1);
		PortSelector ps1 = new PortSelector();
		ps1.addRange("1");
		hashMap1.put("SP", ps1);

		ConditionClause cc1 = new ConditionClause(hashMap1);

		LinkedHashMap<String, Selector> hashMap2 = new LinkedHashMap<String, Selector>();

		IpSelector ips2 = new IpSelector();
		ips2.addRange("1.1.1.1");
		hashMap2.put("SIP", ips2);
		PortSelector ps2 = new PortSelector();
		ps2.addRange("1", "2");
		hashMap2.put("SP", ps2);

		ConditionClause cc2 = new ConditionClause(hashMap2);

		assertTrue(cc1.isIntersecting(cc2));

		cc2.setSelector("SP", new PortSelector());

		assertFalse(cc1.isIntersecting(cc2));
	}
	
	@Test
	public void intersection() throws InvalidIpAddressException, InvalidRangeException, IncompatibleSelectorException, InvalidNetException {
		LinkedHashMap<String, Selector> hashMap1 = new LinkedHashMap<String, Selector>();

		IpSelector ips1 = new IpSelector();
		ips1.addRange("1.1.1.1", "1.1.1.2");
		hashMap1.put("SIP", ips1);
		PortSelector ps1 = new PortSelector();
		ps1.addRange("1");
		hashMap1.put("SP", ps1);

		ConditionClause cc1 = new ConditionClause(hashMap1);

		LinkedHashMap<String, Selector> hashMap2 = new LinkedHashMap<String, Selector>();

		IpSelector ips2 = new IpSelector();
		ips2.addRange("1.1.1.1");
		hashMap2.put("SIP", ips2);
		PortSelector ps2 = new PortSelector();
		ps2.addRange("1", "2");
		hashMap2.put("SP", ps2);

		ConditionClause cc2 = new ConditionClause(hashMap2);

		cc1.intersection(cc2);
		
		assertEquals("[1.1.1.1 - 1.1.1.1] ", cc1.get("SIP").toString());
		assertEquals("[1 - 1] ", cc1.get("SP").toString());
	}
}
