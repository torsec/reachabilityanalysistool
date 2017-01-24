package it.polito.policytoollib.test;

import java.util.HashSet;

import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.GenericRule;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistributedAnalysis {
	
	private PolicyAnalysisModel model;
	
	public DistributedAnalysis() throws Exception{
		model = new PolicyAnalysisModel("TestCases/Distributed.zip", "DistributedAnzlysis");
	}

	
	@Test
	public void noConflict() throws Exception{
		assertEquals("fw1PolicySize", 8, model.getPolicy("fw1").size());
		assertEquals("fw2PolicySize", 8, model.getPolicy("fw2").size());
		
//		System.out.println(model.getDistributedConflicts("Zone1", "Zone2"));
		
		assertEquals("allConflictSize", 12, model.getDistributedConflicts("Zone1", "Zone2").size());
		
		HashSet<String> anomalis = new HashSet<String>();
		for(PolicyAnomaly pa:model.getDistributedConflicts("Zone1", "Zone2")){
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
		
		
		
		
		assertEquals("-fw2.R8-fw1.R9:>fw1>fw2:SHADOWED", true, anomalis.contains("-fw2.R8-fw1.R9:>fw1>fw2:SHADOWED"));
		assertEquals("-fw2.R4-fw1.R5-fw1.R4:>fw1>fw2:REDUNDANT", true, anomalis.contains("-fw2.R4-fw1.R5-fw1.R4:>fw1>fw2:REDUNDANT") || anomalis.contains("-fw2.R4-fw1.R4-fw1.R5:>fw1>fw2:REDUNDANT"));
		assertEquals("-fw1.R5-fw1.R4:>fw1>fw2:REDUNDANT", true, anomalis.contains("-fw1.R5-fw1.R4:>fw1>fw2:REDUNDANT") || anomalis.contains("-fw1.R4-fw1.R5:>fw1>fw2:REDUNDANT"));
		assertEquals("-fw1.R2-fw1.R1:>fw1>fw2:SPURIOUS", true, anomalis.contains("-fw1.R2-fw1.R1:>fw1>fw2:SPURIOUS") || anomalis.contains("-fw1.R1-fw1.R2:>fw1>fw2:SPURIOUS"));
		assertEquals("-fw2.R7-fw1.R7:>fw1>fw2:REDUNDANT", true, anomalis.contains("-fw2.R7-fw1.R7:>fw1>fw2:REDUNDANT"));
	}
}
