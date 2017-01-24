package it.polito.policytoollib.test.policy;

import static org.junit.Assert.assertEquals;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

import java.util.LinkedHashMap;

import org.junit.Test;

public class IPSecTest {
private PolicyAnalysisModel model;
	
	public IPSecTest() throws Exception{
		model = new PolicyAnalysisModel("TestCases/VPN.zip", "VPN");
	}

	
	@Test
	public void read() throws Exception{
		
		assertEquals("fw1PolicySize", 1, model.getPolicy("fw1").size());
		assertEquals("fw2PolicySize", 1, model.getPolicy("fw2").size());
		assertEquals("fw3PolicySize", 1, model.getPolicy("fw3").size());
		
		assertEquals("fw1VPNSize", 1, model.getVPN("fw1").size());
		assertEquals("fw3VPNSize", 1, model.getVPN("fw3").size());
	}
	
	
	@Test
	public void match() throws Exception{
		ComposedPolicy policy12 = model.getEquivalentFW("Zone1", "Zone2");
		
		assertEquals("p12-10", FilteringAction.DENY, policy12.evalAction(getPoint(10,"10.0.1.10",10,"10.0.2.10")));
		assertEquals("p12-11", FilteringAction.ALLOW, policy12.evalAction(getPoint(11,"10.0.1.11",11,"10.0.2.11")));
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
