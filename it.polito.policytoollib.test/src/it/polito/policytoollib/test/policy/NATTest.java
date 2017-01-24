package it.polito.policytoollib.test.policy;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.tools.ReachabilityAnalyser;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import org.junit.Test;

public class NATTest {

	private PolicyAnalysisModel model;
	
	public NATTest() throws Exception{
		model = new PolicyAnalysisModel("TestCases/NAT.zip", "NAT");
	}

	
	@Test
	public void read() throws Exception{
		assertEquals("fw1PolicySize", 1, model.getPolicy("fw1").size());
		assertEquals("fw2PolicySize", 1, model.getPolicy("fw2").size());
		
		assertEquals("fw1NATSize", 1, model.getNAT("fw1").size());
		assertEquals("fw2NATSize", 1, model.getNAT("fw2").size());
	}
	
	
	@Test
	public void match() throws Exception{
		ComposedPolicy policy13 = model.getEquivalentFW("Zone1", "Zone3");
		ComposedPolicy policy12 = model.getEquivalentFW("Zone1", "Zone2");
		
//		for(GenericRule r:policy12.getRuleSet()){
//			System.out.println(r);
//		}
//		
//		System.out.println("-----------------------------");
//		
//		for(GenericRule r:policy12.getCan().getRuleSet()){
//			System.out.println(r);
//		}
//		
//		System.out.println("-----------------------------");
//		
//		for(GenericRule r:policy12.getFMRPolicy().getRuleSet()){
//			System.out.println(r);
//		}

		
		assertEquals("p13-10", FilteringAction.DENY, policy13.evalAction(getPoint(10,"10.0.1.10",10,"10.0.3.10")));
		assertEquals("p13-11", FilteringAction.DENY, policy13.evalAction(getPoint(11,"10.0.1.11",10,"10.0.3.10")));
		assertEquals("p13-12", FilteringAction.DENY, policy13.evalAction(getPoint(12,"10.0.1.12",10,"10.0.3.10")));
		
		assertEquals("p12-10", FilteringAction.ALLOW, policy12.evalAction(getPoint(10,"10.0.1.10",10,"10.0.2.10")));
		assertEquals("p12-11", FilteringAction.ALLOW, policy12.evalAction(getPoint(11,"10.0.1.11",11,"10.0.2.11")));
		assertEquals("p12-12", FilteringAction.DENY, policy12.evalAction(getPoint(12,"10.0.1.12",10,"10.0.2.12")));
	}
	

	private ConditionClause getPoint(int sport, String sip, int dport, String dip) throws Exception{
		LinkedHashMap<String, Selector> hashMap = new LinkedHashMap<String, Selector>();
		
		IpSelector sips = new IpSelector();
		sips.addRange(sip);
		hashMap.put("Source Address", sips);
		IpSelector dips = new IpSelector();
		dips.addRange(dip);
		hashMap.put("Destination Address", dips);
		PortSelector sp = new PortSelector();
		sp.addRange(sport);
		hashMap.put("Source Port", sp);
		PortSelector dp = new PortSelector();
		dp.addRange(dport);
		hashMap.put("Destination Port", dp);
		
		
		ConditionClause point = new ConditionClause(hashMap);
		return point;
	}
}
