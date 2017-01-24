package it.polito.policytoollib.test;

import static org.junit.Assert.assertEquals;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.test.logger.LoggerTest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

public class InterDistributedAnalysisTest {
private PolicyAnalysisModel model;
	
	public InterDistributedAnalysisTest() throws Exception{
		
		Logger.getLogger(PolicyAnalysisModel.class.getName()+".executeQuerry()").setLevel(Level.INFO);
	}

	
	@Test
	public void noConflict() throws Exception{
		model = new PolicyAnalysisModel("TestCases/InterDistributed.zip", "DistributedAnzlysis");
		
		assertEquals("fw1PolicySize", 2, model.getPolicy("fw1").size());
		assertEquals("fw2PolicySize", 4, model.getPolicy("fw2").size());
		
		assertEquals("fw1NATSize", 1, model.getNAT("fw1").size());
		assertEquals("fw2NATSize", 0, model.getNAT("fw2").size());
		
		assertEquals("zones", 2, model.getLandscape().getZoneList().size());
		
		//System.out.println(model.getDistributedConflicts("Zone1", "Zone2"));
		Set<PolicyAnomaly> pa_set = model.getDistributedConflicts("Zone1", "Zone2");
		
		assertEquals("allConflictSize", 3, pa_set.size());
		
		HashSet<String> anomalis = new HashSet<String>();
		for(PolicyAnomaly pa:pa_set){
			String pas = "";
			for(GenericRule r:pa.getRule_set())
				pas = pas +"-"+ r.getName();
			pas = pas + ":";
			for(Policy p:pa.getPolicyList())
				pas = pas +">"+ p.getName();
			pas = pas + ":";
			pas = pas + pa.getConflict();
			anomalis.add(pas);
		}
		
		
		
		assertEquals("-fw2.R2:>fw1>fw2:INTER_TECH_CONFLICT", true, anomalis.contains("-fw2.R2:>fw1>fw2:INTER_TECH_CONFLICT"));
		assertEquals("-fw2.R4:>fw1>fw2:INTER_TECH_CONFLICT", true, anomalis.contains("-fw2.R4:>fw1>fw2:INTER_TECH_CONFLICT"));
		assertEquals("-fw2.R4-fw2.R3:>fw1>fw2:INTER_TECH_CONFLICT", true, anomalis.contains("-fw2.R4-fw2.R3:>fw1>fw2:INTER_TECH_CONFLICT"));
	}
}
