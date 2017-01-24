package it.polito.policytoollib.test.performance;

import java.util.HashMap;

import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.model.PolicyAnalysisModel;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

import org.junit.Test;

public class ReachabilityPolitoNetPerformanceTest {

	private PolicyAnalysisModel model;
	
	public ReachabilityPolitoNetPerformanceTest() throws Exception{
		model = new PolicyAnalysisModel("TestCases/Reachability.zip", "ReadXML");
		
		for(String s:model.getLandscape().getZoneList().keySet()){
			for(int i=1;i<255;i++){
				IpSelector ip = new IpSelector();
				if(s.contains("p") || s.contains("WLAN") || s.contains("_1") )
					ip.addRange("10.0.1."+i);
				else if( s.contains("_2") )
					ip.addRange("10.0.2."+i);
				else if( s.contains("_3") )
					ip.addRange("10.0.3."+i);
				else if( s.contains("_4") )
					ip.addRange("10.0.4."+i);
				else if( s.contains("_5") )
					ip.addRange("10.0.5."+i);
				else if( s.contains("_6") )
					ip.addRange("10.0.6."+i);
				else if( s.contains("_7") )
					ip.addRange("10.0.7."+i);
				else if( s.contains("_8") )
					ip.addRange("10.0.8."+i);
				else if( s.contains("_9") )
					ip.addRange("10.0.9."+i);
				else if( s.contains("_10") )
					ip.addRange("10.0.10."+i);
				else
					ip.addRange("10.0.0."+i);
				Host host = new Host("H"+i, new HashMap<String,IpSelector>(), model.getLandscape().getZoneList().get(s));
				host.addInterface("itf", ip);
				model.getLandscape().getZoneList().get(s).addHost(host);
			}
		}
	}
	
	@Test
	public void reachability() throws Exception{
		
		long startTime, stopTime, elapsedTime;
		System.out.println("Start");
		startTime = System.currentTimeMillis();
		System.out.println(model.setReachabilityQuerry().size());
		stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    System.out.println("Stop");
		System.out.println(elapsedTime);
		
		
	}
	
	
	
	
}
