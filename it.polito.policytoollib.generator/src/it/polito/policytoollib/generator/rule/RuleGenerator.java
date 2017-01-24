package it.polito.policytoollib.generator.rule;

import java.util.LinkedHashMap;

import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.generator.selector.SelectorGenerator;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.RegExpSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.ProtocolIDSelector;
import it.polito.policytoollib.rule.utils.IpAddressManagement;

public class RuleGenerator {
	
	private int maxPortRanges;
	private int PortRangeWidth;
	private int maxIPRanges;
	private int IPRangeWidth;
	private int maxProtocolID;
	private long sourceStartIP;
	private long sourceEndIP;
	private long destinationStartIP;
	private long destinationEndIP;
	
	public RuleGenerator(int maxPortRanges, int PortRangeWidth, int maxIPRanges, int IPRangeWidth, int maxProtocolID){
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
	
	public void setSourceStartIP(long startIP){
		this.sourceStartIP = startIP;
	}
	
	public void setSourceEndIP(long endIP){
		this.sourceEndIP = endIP;
	}
	
	public void setDestinationStartIP(long startIP){
		this.destinationStartIP = startIP;
	}
	
	public void setDestinationEndIP(long endIP){
		this.destinationEndIP = endIP;
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
	
	public GenericRule getGenericRule(FilteringAction action, String name, SelectorTypes selectorTypes, String[] selectorNames){
		
		
		
		LinkedHashMap<String, Selector> selectors = new LinkedHashMap<String, Selector>();
		
		for(String s:selectorNames){
			
			if(selectorTypes.getSelectorType(s) instanceof IpSelector){
				if(s.equals("Source Address"))
					selectors.put(s, SelectorGenerator.getIPSelectorList(sourceStartIP, sourceEndIP, maxIPRanges, IPRangeWidth));
				if(s.equals("Destination Address"))
					selectors.put(s, SelectorGenerator.getIPSelectorList(destinationStartIP, destinationEndIP, maxIPRanges, IPRangeWidth));
			}
			
			if(selectorTypes.getSelectorType(s) instanceof PortSelector){
				selectors.put(s, SelectorGenerator.getPortSelectorList(maxPortRanges, PortRangeWidth));
			}
			
			if(selectorTypes.getSelectorType(s) instanceof ProtocolIDSelector){
				selectors.put(s, SelectorGenerator.getProtocolIDList(maxProtocolID));
			}
			
			if(selectorTypes.getSelectorType(s) instanceof RegExpSelector){
				selectors.put(s, SelectorGenerator.getRegexSelelectorList(1));
			}
		}
		
		
		ConditionClause conditionClause = new ConditionClause(selectors);
		
		
		return new GenericRule(action, conditionClause, name);
		
	}

}
