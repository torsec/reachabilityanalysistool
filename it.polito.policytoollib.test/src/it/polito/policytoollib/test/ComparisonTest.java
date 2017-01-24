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
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.tools.ReachabilityAnalyser;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.test.selector.IPSelectorTest;

import org.junit.Test;

public class ComparisonTest {

	private PolicyAnalysisModel model;
	
	public ComparisonTest() throws Exception{
		Logger.getLogger(PolicyAnalysisModel.class.getName()+".executeQuerry()").setLevel(Level.INFO);

		model = new PolicyAnalysisModel("TestCases/Distributed.zip", "Compare");
	}
	
	@Test
	public void verifyModel() throws Exception{	
		assertEquals("fw1PolicySize", 8, model.getPolicy("fw1").size());
		assertEquals("fw2PolicySize", 8, model.getPolicy("fw2").size());
		
		assertEquals("zones", 2, model.getLandscape().getZoneList().size());
	}
	
	
	@Test
	public void compareSingel1() throws Exception{	
		Policy policy = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "Test");
		assertEquals("differencesEmptyPolicy", 6, model.compare(policy).size());
	}
	
	@Test
	public void compareSingel2() throws Exception{	
		assertEquals("differencesFW1", 1, model.compare( model.getPolicy("fw1")).size());
	}
	
	@Test
	public void compareSingel3() throws Exception{	
		assertEquals("differencesFW2", 2, model.compare( model.getPolicy("fw2")).size());
	}
	
	@Test
	public void compareMulti() throws Exception{	
		PolicyAnalysisModel model1 = new PolicyAnalysisModel("TestCases/Distributed.zip", "Compare");
		
		Policy mergedPolicy = new PolicyImpl(new FMRResolutionStrategy(), FilteringAction.DENY, PolicyType.FILTERING, "");
		int i=0;
		for(FilteringZone z1 : model1.getEquivalentFW_list().keySet()){
			for(FilteringZone z2 : model1.getEquivalentFW_list().keySet()){
				Policy p =model1.getEquivalentFW_list().get(z1).get(z2).getFMRPolicy();
				for(GenericRule rule:p.getRuleSet()){
					int ii = ((FMRResolutionStrategy)p.getResolutionStrategy()).getExternalData(rule);
					mergedPolicy.insertRule(rule, i+ii);
				}
				i=i+p.getRuleSet().size()+1;
			}
		}
		
		assertEquals("fw1PolicySize", 0, model.compare(mergedPolicy).size());
	}
	
}
