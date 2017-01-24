package it.polito.policytoollib.generator.policy;

import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.ProtocolIDSelector;

public class FilteringPolicyGenerator {
	
	private SelectorTypes selectorTypes;
	
	public FilteringPolicyGenerator(){
		selectorTypes = new SelectorTypes();
		selectorTypes.addSelectorType("Source Address", new IpSelector());
		selectorTypes.addSelectorType("Destination Address", new IpSelector());
		selectorTypes.addSelectorType("Source Port", new PortSelector());
		selectorTypes.addSelectorType("Destination Port", new PortSelector());
		selectorTypes.addSelectorType("Protocol", new ProtocolIDSelector());
	}

	public SelectorTypes getSelectorTypes(){
		return selectorTypes;
	}
	
	public Policy getPolicy_0_0(int num, int type, String name) throws Exception {
		PolicyGenerator generator = new PolicyGenerator(3, 190, 18, 50, 100);
		
		switch (num) {
		case 50:
			generator = new PolicyGenerator(3, 1000, 100, 50, 100);
			break;
		case 100:
			generator = new PolicyGenerator(3, 800, 50, 50, 100);
			break;
		case 250:
			generator = new PolicyGenerator(3, 700, 30, 50, 100);
			break;
		case 500:
			generator = new PolicyGenerator(3, 450, 20, 50, 100);
			break;
		case 750:
			generator = new PolicyGenerator(3, 400, 20, 50, 100);
			break;
		case 1000:
			generator = new PolicyGenerator(3, 10, 18, 50, 100);
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


		if(type==0){
			generator.setSourceStartIP("10.0.0.0");
			generator.setSourceEndIP("10.0.0.255");
	
			generator.setDestinationStartIP("10.0.255.0");
			generator.setDestinationEndIP("10.0.255.255");
		} else {
			generator.setSourceStartIP("10.1.0.0");
			generator.setSourceEndIP("10.1.0.255");

			generator.setDestinationStartIP("10.1.255.0");
			generator.setDestinationEndIP("10.1.255.255");
		}

		

		Policy policy = generator.getPolicy(num, selectorTypes, selectorTypes.getSelectorNames(), FilteringAction.DENY, name);

		

		return policy;
	}
	
	public Policy getPolicy_1_5(int num, int type, String name) throws Exception {
		PolicyGenerator generator = new PolicyGenerator(3, 190, 18, 50, 100);
		
		switch (num) {
		case 50:
			generator = new PolicyGenerator(3, 1000, 100, 50, 100);
			break;
		case 100:
			generator = new PolicyGenerator(3, 800, 50, 50, 100);
			break;
		case 250:
			generator = new PolicyGenerator(3, 700, 30, 50, 100);
			break;
		case 500:
			generator = new PolicyGenerator(3, 450, 20, 50, 100);
			break;
		case 750:
			generator = new PolicyGenerator(3, 400, 20, 50, 100);
			break;
		case 1000:
			generator = new PolicyGenerator(3, 120, 18, 50, 100);
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


		if(type==0){
			generator.setSourceStartIP("10.0.0.0");
			generator.setSourceEndIP("10.0.0.255");
	
			generator.setDestinationStartIP("10.0.255.0");
			generator.setDestinationEndIP("10.0.255.255");
		} else {
			generator.setSourceStartIP("10.1.0.0");
			generator.setSourceEndIP("10.1.0.255");

			generator.setDestinationStartIP("10.1.255.0");
			generator.setDestinationEndIP("10.1.255.255");
		}

		

		Policy policy = generator.getPolicy(num, selectorTypes, selectorTypes.getSelectorNames(), FilteringAction.DENY, name);

		

		return policy;
	}
	
	public Policy getPolicy_2_10(int num, int type, String name) throws Exception {
		PolicyGenerator generator = new PolicyGenerator(3, 190, 18, 50, 100);
		
		switch (num) {
		case 50:
			generator = new PolicyGenerator(3, 1000, 100, 50, 100);
			break;
		case 100:
			generator = new PolicyGenerator(3, 800, 50, 50, 100);
			break;
		case 250:
			generator = new PolicyGenerator(3, 700, 30, 50, 100);
			break;
		case 500:
			generator = new PolicyGenerator(3, 450, 20, 50, 100);
			break;
		case 750:
			generator = new PolicyGenerator(3, 400, 20, 50, 100);
			break;
		case 1000:
			generator = new PolicyGenerator(3, 210, 18, 50, 100);
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


		if(type==0){
			generator.setSourceStartIP("10.0.0.0");
			generator.setSourceEndIP("10.0.0.255");
	
			generator.setDestinationStartIP("10.0.255.0");
			generator.setDestinationEndIP("10.0.255.255");
		} else {
			generator.setSourceStartIP("10.1.0.0");
			generator.setSourceEndIP("10.1.0.255");

			generator.setDestinationStartIP("10.1.255.0");
			generator.setDestinationEndIP("10.1.255.255");
		}

		

		Policy policy = generator.getPolicy(num, selectorTypes, selectorTypes.getSelectorNames(), FilteringAction.DENY, name);

		

		return policy;
	}
	
	public Policy getPolicy_4_20(int num, int type, String name) throws Exception {
		PolicyGenerator generator = new PolicyGenerator(3, 190, 18, 50, 100);
		
		switch (num) {
		case 50:
			generator = new PolicyGenerator(3, 1000, 100, 50, 100);
			break;
		case 100:
			generator = new PolicyGenerator(3, 800, 50, 50, 100);
			break;
		case 150:
			generator = new PolicyGenerator(3, 750, 50, 50, 100);
			break;
		case 200:
			generator = new PolicyGenerator(3, 750, 30, 50, 100);
			break;
		case 250:
			generator = new PolicyGenerator(3, 700, 30, 50, 100);
			break;
		case 300:
			generator = new PolicyGenerator(3, 650, 30, 50, 100);
			break;
		case 350:
			generator = new PolicyGenerator(3, 600, 30, 50, 100);
			break;
		case 400:
			generator = new PolicyGenerator(3, 550, 40, 50, 100);
			break;
		case 450:
			generator = new PolicyGenerator(3, 500, 20, 50, 100);
			break;
		case 500:
			generator = new PolicyGenerator(3, 450, 30, 50, 100);
			break;
		case 750:
			generator = new PolicyGenerator(3, 400, 20, 50, 100);
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

		if(type==0){
			generator.setSourceStartIP("10.0.0.0");
			generator.setSourceEndIP("10.0.0.255");
	
			generator.setDestinationStartIP("10.0.255.0");
			generator.setDestinationEndIP("10.0.255.255");
		} else {
			generator.setSourceStartIP("10.1.0.0");
			generator.setSourceEndIP("10.1.0.255");

			generator.setDestinationStartIP("10.1.255.0");
			generator.setDestinationEndIP("10.1.255.255");
		}
		

		Policy policy = generator.getPolicy(num, selectorTypes, selectorTypes.getSelectorNames(), FilteringAction.DENY, name);

		

		return policy;
	}
	
	
	
	public Policy getPolicy_8_40(int num, int type, String name) throws Exception {
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
		
		


		if(type==0){
			generator.setSourceStartIP("10.0.0.0");
			generator.setSourceEndIP("10.0.0.255");
	
			generator.setDestinationStartIP("10.0.255.0");
			generator.setDestinationEndIP("10.0.255.255");
		} else {
			generator.setSourceStartIP("10.1.0.0");
			generator.setSourceEndIP("10.1.0.255");

			generator.setDestinationStartIP("10.1.255.0");
			generator.setDestinationEndIP("10.1.255.255");
		}

		

		Policy policy = generator.getPolicy(num, selectorTypes, selectorTypes.getSelectorNames(), FilteringAction.DENY, name);

		

		return policy;
	}
	
	
	

}
