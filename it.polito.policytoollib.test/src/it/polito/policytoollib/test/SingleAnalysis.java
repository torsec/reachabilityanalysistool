package it.polito.policytoollib.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;

public class SingleAnalysis {

	private PolicyAnalysisModel model;
	
	public SingleAnalysis() throws Exception{
		model = new PolicyAnalysisModel("TestCases/Single.zip", "readXML");
	}

	
	@Test
	public void noConflict() throws Exception{
		assertEquals("noConflictPolicySize", 8, model.getPolicy("noConflict").size());
		
		assertEquals("noConflictSize", 0, model.getSingleAnomalies("noConflict").size());
	}
	
	@Test
	public void allConflict() throws Exception{ 
		assertEquals("allConflictPolicySize", 8, model.getPolicy("allConflict").size());
		
		Set<PolicyAnomaly> conflicts = model.getSingleAnomalies("allConflict");
		assertEquals("allConflictSize", 8, conflicts.size());
		HashSet<String> anomalySet = new HashSet<String>();
		for(PolicyAnomaly pa:conflicts){
			assertEquals("allConflictAnomalySize", 2, pa.getRule_set().length);
			anomalySet.add(pa.getRule_set()[0].getName()+"-"+pa.getRule_set()[1].getName()+"="+pa.getConflict());
		}
		assertEquals("R5-R4=REDUNDANT", true, anomalySet.contains("R5-R4=REDUNDANT"));
		assertEquals("R1-R2=SHADOWS", true, anomalySet.contains("R1-R2=SHADOWS"));
		assertEquals("R9-R8=LESS", true, anomalySet.contains("R9-R8=LESS"));
		assertEquals("R8-R9=GREATER", true, anomalySet.contains("R8-R9=GREATER"));
		assertEquals("R6-R7=CONFLICTING", true, anomalySet.contains("R6-R7=CONFLICTING"));
		assertEquals("R7-R6=CONFLICTING", true, anomalySet.contains("R7-R6=CONFLICTING"));
		assertEquals("R4-R5=MAKES_REDUNDANT", true, anomalySet.contains("R4-R5=MAKES_REDUNDANT"));
		assertEquals("R2-R1=SHADOWED", true, anomalySet.contains("R2-R1=SHADOWED"));
		
		
	}
}
