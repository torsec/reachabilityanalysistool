package it.polito.policytoollib.test.generator;

import static org.junit.Assert.*;
import it.polito.policytoollib.generator.policy.PolicyGenerator;
import it.polito.policytoollib.generator.util.PolicyStatistics;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.ProtocolIDSelector;

import org.junit.Test;

public class PolicyGeneratorTest {

	@Test
	public void test() throws Exception {

		for (int maxPortRanges = 1; maxPortRanges < 10; maxPortRanges++) {
			System.out.println("PORTRANGE" + maxPortRanges);
			for (int PortRangeWidth = 10; PortRangeWidth < 200; PortRangeWidth += 20) {
				for (int maxIPRanges = 1; maxIPRanges < 20; maxIPRanges++) {
					for (int IPRangeWidth = 10; IPRangeWidth < 100; IPRangeWidth += 10) {
						for (int maxProtocolID = 1; maxProtocolID < 2; maxProtocolID++) {
							PolicyGenerator generator = new PolicyGenerator(
									maxPortRanges, PortRangeWidth, maxIPRanges,
									IPRangeWidth, maxProtocolID);
							generator.setSourceStartIP("10.0.0.0");
							generator.setSourceEndIP("10.0.0.255");

							generator.setDestinationStartIP("10.0.1.0");
							generator.setDestinationEndIP("10.0.1.255");

							SelectorTypes selectorTypes = new SelectorTypes();
							selectorTypes.addSelectorType("Source Address",
									new IpSelector());
							selectorTypes.addSelectorType(
									"Destination Address", new IpSelector());
							selectorTypes.addSelectorType("Source Port",
									new PortSelector());
							selectorTypes.addSelectorType("Destination Port",
									new PortSelector());
							// selectorTypes.addSelectorType("Protocol", new
							// ProtocolIDSelector());

							Policy policy = generator.getPolicy(1000,
									selectorTypes,
									selectorTypes.getSelectorNames(),
									FilteringAction.DENY, "testPolicy");

							PolicyStatistics stat = new PolicyStatistics(policy);

							if (stat.getMAX() > 2 && stat.getMAX() < 6
									&& stat.getIntersectingNoPercent() > 15
									&& stat.getIntersectingNoPercent() < 25) {
								System.out.println(maxPortRanges);
								System.out.println(PortRangeWidth);
								System.out.println(maxIPRanges);
								System.out.println(IPRangeWidth);
								System.out.println(maxProtocolID);

							}

						}
					}
				}
			}
		}
	}

	@Test
	public void test2500_20() throws Exception {
		PolicyGenerator generator = new PolicyGenerator(3, 190, 18, 50, 1);
		generator.setSourceStartIP("10.0.0.0");
		generator.setSourceEndIP("10.0.0.255");

		generator.setDestinationStartIP("10.0.1.0");
		generator.setDestinationEndIP("10.0.1.255");

		SelectorTypes selectorTypes = new SelectorTypes();
		selectorTypes.addSelectorType("Source Address", new IpSelector());
		selectorTypes.addSelectorType("Destination Address", new IpSelector());
		selectorTypes.addSelectorType("Source Port", new PortSelector());
		selectorTypes.addSelectorType("Destination Port", new PortSelector());
		// selectorTypes.addSelectorType("Protocol", new
		// ProtocolIDSelector());

		Policy policy = generator.getPolicy(2500, selectorTypes,
				selectorTypes.getSelectorNames(), FilteringAction.DENY,
				"testPolicy");

		PolicyStatistics stat = new PolicyStatistics(policy);

		stat.printStatistics();

	}
	
	@Test
	public void test200_20() throws Exception {
		PolicyGenerator generator = new PolicyGenerator(3, 190, 18, 50, 1);
		generator.setSourceStartIP("10.0.0.0");
		generator.setSourceEndIP("10.0.0.255");

		generator.setDestinationStartIP("10.0.1.0");
		generator.setDestinationEndIP("10.0.1.255");

		SelectorTypes selectorTypes = new SelectorTypes();
		selectorTypes.addSelectorType("Source Address", new IpSelector());
		selectorTypes.addSelectorType("Destination Address", new IpSelector());
		selectorTypes.addSelectorType("Source Port", new PortSelector());
		selectorTypes.addSelectorType("Destination Port", new PortSelector());
		// selectorTypes.addSelectorType("Protocol", new
		// ProtocolIDSelector());

		Policy policy = generator.getPolicy(2500, selectorTypes,
				selectorTypes.getSelectorNames(), FilteringAction.DENY,
				"testPolicy");

		PolicyStatistics stat = new PolicyStatistics(policy);

		stat.printStatistics();
	}

}
