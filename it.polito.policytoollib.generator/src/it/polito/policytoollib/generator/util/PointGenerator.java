package it.polito.policytoollib.generator.util;

import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.ProtocolIDSelector;
import it.polito.policytoollib.util.RealBitSet;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class PointGenerator {
public HashSet<ConditionClause> getPoints(int maxRules, GenericRule rule, LinkedHashMap<String, Selector> selectors) throws Exception{
			
		
		Random rand=new Random(System.currentTimeMillis());
		
		HashSet<ConditionClause> points = new HashSet<ConditionClause>();
		
		for(int i=0;i<maxRules;i++){
			ConditionClause point = new ConditionClause(selectors);
			for(String key : selectors.keySet()){
//				System.out.println(key);
				Selector sel = selectors.get(key);
		
				if (sel instanceof ProtocolIDSelector){
					
					RealBitSet bitset = ((ProtocolIDSelector)rule.getConditionClause().get(key)).getPointSet();
					
					int range = rand.nextInt(ProtocolIDSelector.getMAX_VALUE());
					
					while(!bitset.get(range))
						range = rand.nextInt(ProtocolIDSelector.getMAX_VALUE());
						
					ProtocolIDSelector pids = new ProtocolIDSelector();
					pids.addRange(range);
					point.setSelector(key, pids);
				}
				
				if (sel instanceof PortSelector){
					
					BigInteger[] ranges =  ((PortSelector)rule.getConditionClause().get(key)).getRanges();
					
//					System.out.println(ranges);
					
					int ii = rand.nextInt(ranges.length-1);
					
					if(ii%2==1)
						ii--;
					
					while(ranges[ii].compareTo(BigInteger.valueOf(-2))==0){
						ii-=2;
					}
					
					PortSelector ps = new PortSelector();
					int range;
					if(ranges[ii+1].compareTo(ranges[ii])<=0)
						range=ranges[ii].intValue();
					else
						range = (ranges[ii].intValue())+(rand.nextInt(ranges[ii+1].intValue()-ranges[ii].intValue()));
//					System.out.println(ranges.get(ii+1).intValue()-ranges.get(ii).intValue());
//					System.out.println(ranges.get(ii+1).intValue());
//					System.out.println(ranges.get(ii).intValue());
					ps.addRange(range);
					point.setSelector(key, ps);
				}
				
				if (sel instanceof IpSelector){
					BigInteger[] ranges =  ((IpSelector)rule.getConditionClause().get(key)).getRanges();
					int ii = rand.nextInt(ranges.length-1);
					
					if(ii%2==1)
						ii--;
					
					while(ranges[ii].compareTo(BigInteger.valueOf(-2))==0){
						ii-=2;
					}
					
					IpSelector ips = new IpSelector();
					BigInteger range;
					if(ranges[ii+1].compareTo(ranges[ii])==0)
						range=ranges[ii];
					else
						range = ranges[ii].add((BigInteger.valueOf(rand.nextLong()).remainder((ranges[ii+1].subtract(ranges[ii]))))) ;
					ips.addRange(range, range);
					point.setSelector(key, ips);
				}
		
			}
			
			if(!point.isPoint(selectors.keySet())){
				for(String key : selectors.keySet()){
					if(point.get(key).isEmpty()){
						Selector sel = selectors.get(key);
						
						if (sel instanceof ProtocolIDSelector){
							ProtocolIDSelector pips = new ProtocolIDSelector();
							pips.addRange(rand.nextInt(ProtocolIDSelector.getMAX_VALUE()));
							point.setSelector(key, pips);
						}
						
						if (sel instanceof PortSelector){
							PortSelector ps = new PortSelector();
							ps.addRange(rand.nextInt(PortSelector.getMaxPort()));
							point.setSelector(key, ps);
						}
						
						if (sel instanceof IpSelector){
							IpSelector ips = new IpSelector();
							long range = rand.nextLong()%IpSelector.getMaxLong();
							ips.addRange(range,range);
							point.setSelector(key, ips);
						}
					}
				}
			}
			points.add(point);
		}
		
				
		return points;	
	}
}
