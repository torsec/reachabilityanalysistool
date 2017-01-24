package it.polito.policytoollib.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polito.policytoollib.exception.policy.EmptySelectorException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.rule.IllegalParamException;
import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidNetException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.tools.ReachabilityAnalyser;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.test.selector.IPSelectorTest;

import org.junit.Test;

public class ReachabilityTest {

	private PolicyAnalysisModel model;
	
	public ReachabilityTest() throws Exception{
		Logger.getLogger(PolicyAnalysisModel.class.getName()+".executeQuerry()").setLevel(Level.INFO);

		model = new PolicyAnalysisModel("TestCases/NAT.zip", "VPN");
	}
	
	@Test
	public void verifyModel() throws Exception{
		
		
		assertEquals("fw1PolicySize", 1, model.getPolicy("fw1").size());
		assertEquals("fw2PolicySize", 1, model.getPolicy("fw2").size());
		assertEquals("fw1NATSize", 1, model.getNAT("fw1").size());
		assertEquals("fw2NATSize", 1, model.getNAT("fw2").size());
		
		assertEquals("zones", 3, model.getLandscape().getZoneList().size());
	}
		
	@Test
	public void executeQuerry() throws Exception{
		ConditionClause zone_rule = new ConditionClause();

		IpSelector dip = new IpSelector();
		dip.full();
		zone_rule.addSelector("Destination Address", dip);
		IpSelector sip = new IpSelector();
		sip.full();
		zone_rule.addSelector("Source Address", sip);
		
		PortSelector dps = new PortSelector();
		dps.full();
		zone_rule.addSelector("Destination Port", dps);
		PortSelector sps = new PortSelector();
		sps.full();
		zone_rule.addSelector("Source Port", sps);
		
		
		assertEquals("ServerQuerySize", 13, model.executeQuerry(zone_rule).size());
		
	}
	
	@Test
	public void setServerQuerry() throws Exception{
		assertEquals("ServerQuerySize", 1, model.setServerQuerry("10.0.2.12", "12").size());
	}
	
	
	@Test
	public void setClientQuerry() throws Exception{	
		assertEquals("ClientQuerySize", 1, model.setClientQuerry("10.0.1.10").size());
	}
	
	
}
