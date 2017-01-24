package it.polito.policytoollib.generator.policy;


import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.generator.rule.RuleGenerator;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.ProtocolIDSelector;
import it.polito.policytoollib.rule.utils.IpAddressManagement;
import it.polito.policytoollib.util.RealBitSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class PolicyGenerator {


//	private SelectorTypes selectorTypes;
//	
//	public PolicyGenerator(SelectorTypes selectorTypes) {
//		this.selectorTypes = selectorTypes;
//	}
	
	private int maxPortRanges;
	private int PortRangeWidth;
	private int maxIPRanges;
	private int IPRangeWidth;
	private int maxProtocolID;
	private long sourceStartIP;
	private long sourceEndIP;
	private long destinationStartIP;
	private long destinationEndIP;
	
	public PolicyGenerator(int maxPortRanges, int PortRangeWidth, int maxIPRanges, int IPRangeWidth, int maxProtocolID){
		this.maxPortRanges = maxPortRanges;
		this.PortRangeWidth = PortRangeWidth;		
		this.maxIPRanges = maxIPRanges;
		this.IPRangeWidth = IPRangeWidth;
		this.maxProtocolID = maxProtocolID;
		this.sourceStartIP = 0;
		this.sourceEndIP = IpSelector.getMaxLong();
		this.destinationStartIP = 0;
		this.destinationEndIP = IpSelector.getMaxLong();
	}
	
	public void setSourceStartIP(String startIP) throws InvalidIpAddressException{
		this.sourceStartIP = IpAddressManagement.getInstance().toLong(startIP);
	}
	
	public void setSourceEndIP(String endIP) throws InvalidIpAddressException{
		this.sourceEndIP = IpAddressManagement.getInstance().toLong(endIP);
	}
	
	public void setDestinationStartIP(String startIP) throws InvalidIpAddressException{
		this.destinationStartIP = IpAddressManagement.getInstance().toLong(startIP);
	}
	
	public void setDestinationEndIP(String endIP) throws InvalidIpAddressException{
		this.destinationEndIP = IpAddressManagement.getInstance().toLong(endIP);
	}
	
	public Policy getPolicy(int ruleNum, SelectorTypes selectorTypes, String[] selectorNames, Action defaultAction, String name){
		
		PolicyImpl policy = new PolicyImpl(new FMRResolutionStrategy(), defaultAction, PolicyType.FILTERING, name);
		RuleGenerator ruleGenerator = new RuleGenerator(maxPortRanges, PortRangeWidth, maxIPRanges, IPRangeWidth, maxProtocolID);
		ruleGenerator.setSourceStartIP(sourceStartIP);
		ruleGenerator.setSourceEndIP(sourceEndIP);
		ruleGenerator.setDestinationStartIP(destinationStartIP);
		ruleGenerator.setDestinationEndIP(destinationEndIP);
		for(int i=0; i<ruleNum; i++){
			GenericRule rule = ruleGenerator.getGenericRule(FilteringAction.ALLOW, "R"+i, selectorTypes, selectorNames);
			try {
				policy.insertRule(rule, i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return policy;
	}
	

	
	
}
