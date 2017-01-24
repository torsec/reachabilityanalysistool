package it.polito.policytoollib.generator.selector;

import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.ProtocolIDSelector;
import it.polito.policytoollib.rule.selector.impl.StandardRegExpSelector;

import java.util.LinkedList;
import java.util.Random;

public class SelectorGenerator {

	private static Random RANDOM = new Random(System.currentTimeMillis());

	public static IpSelector getIPSelectorList(long startIP, long endIP, int maxIPRanges, int IPRangeWidth) {
		int rangeNumber;

		IpSelector ipSelector = new IpSelector();

		long minIP, maxIP;

		rangeNumber = RANDOM.nextInt(maxIPRanges);

		for (int j = 0; j <= rangeNumber; j++) {
			minIP = startIP + Math.abs(RANDOM.nextLong() % ((endIP-startIP) + 1));
			maxIP = minIP + Math.abs(RANDOM.nextLong() % (IPRangeWidth));

			if (maxIP > endIP)
				maxIP = endIP;

			ipSelector.addRange(minIP, maxIP);
		}

		return ipSelector;
	}

	public static ProtocolIDSelector getProtocolIDList(int maxProtocolID) {

		int maxValue = ProtocolIDSelector.getMAX_VALUE();
		int rangeNumber = RANDOM.nextInt(maxProtocolID);
		
		ProtocolIDSelector protocolIDSelector = new ProtocolIDSelector();
		for (int j = 0; j <= rangeNumber; j++) {
			int value = RANDOM.nextInt(maxValue);
			try {
				protocolIDSelector.addRange(value);
			} catch (InvalidRangeException e) {
				j--;
				e.printStackTrace();
			}
		}

		return protocolIDSelector;
	}

	public static PortSelector getPortSelectorList(int maxPortRanges, int PortRangeWidth) {
		int maxValue, rangeNumber;
		maxValue = PortSelector.getMaxPort();

		int minPort, maxPort;

		rangeNumber = RANDOM.nextInt(maxPortRanges);

		PortSelector portSelector = new PortSelector();
		for (int j = 0; j <= rangeNumber; j++) {
			minPort = RANDOM.nextInt(maxValue);
			maxPort = minPort + RANDOM.nextInt(PortRangeWidth);
			if (maxPort > maxValue)
				maxPort = maxValue;

			try {
				portSelector.addRange(minPort, maxPort);
			} catch (InvalidRangeException e) {
				j--;
			}
		}

		return portSelector;

	}

	public static StandardRegExpSelector getRegexSelelectorList(int NumSel) {

		String[] machine = { "www[a-zA-Z]*", "ww[a-zA-Z]*", "w[a-zA-Z]*", "[a-zA-Z]*" };
		String[] domain = { "c[a-zA-Z]*", "co[a-zA-Z]*", "com[a-zA-Z]*", "[a-zA-Z]*", "[a-zA-Z]*" };
		String[] subdomain = { "polito[a-zA-Z]*", "poli[a-zA-Z]*", "po[a-zA-Z]*", "p[a-zA-Z]*", "[a-zA-Z]*" };

		StandardRegExpSelector standardRegExpSelector = new StandardRegExpSelector();

		String regex = "";

		if (RANDOM.nextInt(10) < 5) {
			regex = ".*";
		} else {
			regex += machine[RANDOM.nextInt(machine.length)] + "\\.";
			regex += subdomain[RANDOM.nextInt(subdomain.length)] + "\\.";
			regex += domain[RANDOM.nextInt(domain.length)];
		}

		standardRegExpSelector.addRange(regex);
		

		return standardRegExpSelector;
	}

}
