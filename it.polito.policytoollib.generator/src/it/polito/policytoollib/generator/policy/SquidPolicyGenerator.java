package it.polito.policytoollib.generator.policy;

import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.ProtocolIDSelector;
import it.polito.policytoollib.rule.selector.impl.StandardRegExpSelector;

public class SquidPolicyGenerator {
	
	private SelectorTypes selectorTypes;
	
	public SquidPolicyGenerator(){
		selectorTypes = new SelectorTypes();
		selectorTypes.addSelectorType("Source Address", new IpSelector());
		selectorTypes.addSelectorType("Destination Address", new IpSelector());
		selectorTypes.addSelectorType("Source Port", new PortSelector());
		selectorTypes.addSelectorType("Destination Port", new PortSelector());
		selectorTypes.addSelectorType("Protocol", new ProtocolIDSelector());
		selectorTypes.addSelectorType("Squid URL", new StandardRegExpSelector());
		selectorTypes.addSelectorType("Squid Browser", new StandardRegExpSelector());
		selectorTypes.addSelectorType("Squid Referer", new StandardRegExpSelector());
		selectorTypes.addSelectorType("Squid Ident", new StandardRegExpSelector());
		selectorTypes.addSelectorType("Squid ProxyAuth", new StandardRegExpSelector());
		selectorTypes.addSelectorType("Squid ReqMIMEType", new StandardRegExpSelector());
		selectorTypes.addSelectorType("Squid RepMIMEType", new StandardRegExpSelector());
		selectorTypes.addSelectorType("Squid ReqHeader", new StandardRegExpSelector());
		selectorTypes.addSelectorType("Squid RepHeader", new StandardRegExpSelector());
	}

	public SelectorTypes getSelectorTypes(){
		return selectorTypes;
	}
	
	public Policy getPolicy_4_20(int num) throws Exception {
		PolicyGenerator generator = new PolicyGenerator(3, 190, 18, 50, 100);
		
		switch (num) {
		case 500:
			generator = new PolicyGenerator(3, 450, 20, 50, 100);
			break;
		case 1000:
			generator = new PolicyGenerator(3, 310, 19, 50, 100);
			break;
		case 1500:
			generator = new PolicyGenerator(3, 250, 18, 50, 100);
			break;
		case 2000:
			generator = new PolicyGenerator(3, 210, 18, 50, 100);
			break;
		case 2500:
			generator = new PolicyGenerator(3, 190, 18, 50, 100);
			break;
		case 3000:
			generator = new PolicyGenerator(3, 180, 19, 50, 100);
			break;
		case 3500:
			generator = new PolicyGenerator(3, 170, 18, 50, 100);
			break;
		case 4000:
			generator = new PolicyGenerator(3, 160, 18, 50, 100);
			break;
		case 4500:
			generator = new PolicyGenerator(3, 150, 18, 50, 100);
			break;
		case 5000:
			generator = new PolicyGenerator(3, 140, 18, 50, 100);
			break;

		}


		generator.setSourceStartIP("10.0.0.0");
		generator.setSourceEndIP("10.0.0.255");

		generator.setDestinationStartIP("10.0.255.0");
		generator.setDestinationEndIP("10.0.255.255");

		

		Policy policy = generator.getPolicy(num, selectorTypes, selectorTypes.getSelectorNames(), FilteringAction.DENY, "testPolicy");

		

		return policy;
	}
	
	public Policy getPolicy_8_40(int num) throws Exception {
		PolicyGenerator generator = new PolicyGenerator(3, 190, 18, 50, 100);
		
		switch (num) {
		case 500:
			generator = new PolicyGenerator(3, 830, 23, 50, 100);
			break;
		case 1000:
			generator = new PolicyGenerator(3, 550, 22, 50, 100);
			break;
		case 1500:
			generator = new PolicyGenerator(3, 440, 19, 50, 100);
			break;
		case 2000:
			generator = new PolicyGenerator(3, 380, 21, 50, 100);
			break;
		case 2500:
			generator = new PolicyGenerator(3, 320, 19, 50, 100);
			break;
		case 3000:
			generator = new PolicyGenerator(3, 300, 19, 50, 100);
			break;
		case 3500:
			generator = new PolicyGenerator(3, 280, 18, 50, 100);
			break;
		case 4000:
			generator = new PolicyGenerator(3, 280, 18, 50, 100);
			break;
		case 4500:
			generator = new PolicyGenerator(3, 260, 18, 50, 100);
			break;
		case 5000:
			generator = new PolicyGenerator(3, 250, 18, 50, 100);
			break;

		}
		
		


		generator.setSourceStartIP("10.0.0.0");
		generator.setSourceEndIP("10.0.0.255");

		generator.setDestinationStartIP("10.0.255.0");
		generator.setDestinationEndIP("10.0.255.255");

		

		Policy policy = generator.getPolicy(num, selectorTypes, selectorTypes.getSelectorNames(), FilteringAction.DENY, "testPolicy");

		

		return policy;
	}
	
	
	

}
