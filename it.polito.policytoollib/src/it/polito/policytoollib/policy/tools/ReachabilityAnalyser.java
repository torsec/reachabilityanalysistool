package it.polito.policytoollib.policy.tools;

import it.polito.policytoollib.exception.policy.EmptySelectorException;
import it.polito.policytoollib.exception.rule.IllegalParamException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.landscape.Landscape;
import it.polito.policytoollib.landscape.Service;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.utils.Point;
import it.polito.policytoollib.policy.utils.PointList;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class ReachabilityAnalyser {
	
	private Landscape ld;
	private HashMap<FilteringZone, HashMap<FilteringZone, ComposedPolicy>> equivalentFW_list;
	
	public ReachabilityAnalyser(Landscape ld, HashMap<FilteringZone, HashMap<FilteringZone, ComposedPolicy>> equivalentFW_list){
		this.equivalentFW_list = equivalentFW_list;
		this.ld = ld;
	}
	
	
	public HashSet<GenericRule> executeQuerry(ConditionClause zone_rule) throws Exception{


		HashSet<GenericRule> rule_set = null;
		HashSet<GenericRule> result_rule_set = new HashSet<GenericRule>();
		
		Collection<FilteringZone> zoneList = ld.getZoneList().values();
		
		if(zone_rule.get("Source Address") == null)
			zone_rule.setSelector("Source Address", new IpSelector());
		if(zone_rule.get("Destination Address") == null)
			zone_rule.setSelector("Destination Address", new IpSelector());
		
		for(FilteringZone zone1:zoneList){
			HashMap<FilteringZone, ComposedPolicy> FW_list = equivalentFW_list.get(zone1);
			IpSelector sip = new IpSelector();
			HashSet<IpSelector> sip_set = new HashSet<IpSelector>();
			if(zone_rule.get("Source Address").isEmpty()){
				for(Host ele:zone1.getHostList()){
					sip_set.add(ele.getIPAddress());
					sip.union(ele.getIPAddress());
				}
				if(!sip.isEmpty())
					zone_rule.setSelector("Source Address", sip);
				else
					continue;
			} else {
				IpSelector zip = (IpSelector)zone1.getIPSubnet().selectorClone();
				zip.intersection(zone_rule.get("Source Address"));
				if(zip.isEmpty())
					continue;
				sip_set.add((IpSelector)zone_rule.get("Source Address"));
			}
			
			
			for(FilteringZone zone2:zoneList){
				if(zone1!=zone2){
					
					//System.out.println(zone_rule);
					
					rule_set = new HashSet<GenericRule>();
					IpSelector dip = new IpSelector();
					//HashSet<IpSelector> dip_set = new HashSet<IpSelector>();

					if(zone_rule.get("Destination Address").isEmpty()){
						for(Host ele:zone2.getHostList()){
							for(Service serv: ele.getServices().values()){
									IpSelector inter = serv.getAddress();
									IpSelector n_dip = (IpSelector)inter.selectorClone();
									//dip_set.add(n_dip);
									dip.union(inter);
								}
						}
						if(!dip.isEmpty())
							zone_rule.setSelector("Destination Address", dip);
						else
							continue;
					} else {
						IpSelector zip = (IpSelector)zone2.getIPSubnet().selectorClone();
						zip.intersection(zone_rule.get("Destination Address"));
						if(zip.isEmpty())
							continue;
						//dip_set.add((IpSelector)zone_rule.get("Destination Address"));
					}
					
					
					for(GenericRule r:FW_list.get(zone2).getRuleSet()){
						if(r.isIntersecting(zone_rule)){
							result_rule_set.add(r);
//							if(r.getConditionClause().get("Destination Address").isEmpty())
//								System.out.println(r);
						}
					}

//					int count=0;
//					int rule_count=0;
//					//GenericRule new_zone_rule;// = new GenericRule(action, conditionClause, name).ruleClone();
//
//					ConditionClause new_zone_rule = zone_rule.conditionClauseClone();
//					
//					PortSelector n_dp = new PortSelector();
//					n_dp.addRange(80);
//					new_zone_rule.setSelector("Destination Port", n_dp);
//					
//					for(IpSelector n_sip:sip_set){
//						new_zone_rule.setSelector("Source Address", n_sip);
//						for(IpSelector n_dip:dip_set){
//							new_zone_rule.setSelector("Destination Address", n_dip);
//							
//							HashSet<GenericRule> r_set = new HashSet<GenericRule>();
//							
//							
//							for(GenericRule r:rule_set){
//								if(r.isIntersecting(new_zone_rule))
//									r_set.add(r);
//							}
//							
////							for(Policy p:FW_list.get(zone2).getOriginalPolicy()){
////								for(GenericRule r:p.getRuleSet()){
////									if(r.isIntersecting(new_zone_rule))
////										if(r.getName().contains("R0"))
////											System.out.println(r);
////								}
////							}
//								
//							
//							Action a = FW_list.get(zone2).getResolutionStrategy().composeActions(r_set);
//							count++;
//							rule_count+=r_set.size();
//							if(a==FilteringAction.ALLOW){
//								ConditionClause new_cc = new_zone_rule.conditionClauseClone();
//								for(GenericRule r:r_set)
//									new_cc.intersection(r.getConditionClause());
//								result_rule_set.add(new GenericRule(a, new_cc, ""));
//								
//							}
//						}
//
//					}
					
					if(!dip.isEmpty())
						zone_rule.setSelector("Destination Address", new IpSelector());
				}
			}
			
			if(!sip.isEmpty())
				zone_rule.setSelector("Source Address", new IpSelector());
		}
		
		
		return result_rule_set;
		
	}


	private HashSet<GenericRule> getAllow(HashSet<GenericRule> rule_set, ComposedPolicy policy, SelectorTypes selectorTypes) throws IllegalParamException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, EmptySelectorException, UnsupportedSelectorException {
		
		
		HashSet<GenericRule> result = new HashSet<GenericRule>();
		
		if(rule_set.size()==0)
			return result;
		
		HashMap<String, PointList> hashpl = new HashMap<String, PointList>();
		
		hashpl = new HashMap<String, PointList>();
  
    	GenericRule r = rule_set.iterator().next();
        HashSet<String> selLab = policy.getSelectorNames();
        
        for(String keylabel: selLab){
        	Selector s = selectorTypes.getSelectorType(keylabel);
                
            hashpl.put(keylabel, new PointList(s,keylabel));
        }
        
        
      
        int index=0;
        for(GenericRule rule:rule_set){
            for(String keylabel: selLab){
            	Selector s = rule.getConditionClause().get(keylabel).selectorClone();
       
            	PointList pl = hashpl.get(keylabel);
                pl.insert(s, index);
            }
            index++;
        }
        
        PointList[] point_list = new PointList[hashpl.values().size()];
        int i =0;
        for(PointList pl: hashpl.values()){
                point_list[i++] = pl;
        }
        
        Point start_point = null;

        
        
        //TODO:
//        for(Point p: (List<Point>)point_list[0].getPointList()){
//            if(p.isStart()){
//            	start_point = p;
//                continue;
//            }
//            Selector s = GenericRuleFactory.getInstance().getSelectorFactory(test_rule, "").createEmptySelector();
//            s.
//            test_rule.setSelector(label, s);
//            recursive_verification(1, point_list, result);
//        }
        
        
        
        //TODO:
        result.addAll(rule_set);
		
		return result;
	}
	
//	private boolean recursive_verification1(int n, PointList[] point_list, HashSet<GenericRule> result)
//    {
//            if(n == point_list.length-1){
//                    return verifyAllPoints(point_list[n]);
//            }
//            try{
//	            for(Point p: (List<Point>)point_list[n].getPointList())
//	            {
//                    if(p.isStart())
//                            continue;
//
//                    recursive_verification(n+1, point_list, result);
//	
//	            }
//            }catch(Exception e){
////                    System.err.println(rule);
////                    System.err.println(hiders);
//                    e.printStackTrace();
//            }
//            return true;
//            
//
//    }       
//	
//	
//	private boolean verifyAllPoints(PointList list)
//    {
//        for(Block p:(List<Block>)list.getPointList())
//        {
//            if(p.isStart())
//                    continue;
//
//
//                
//        }
//        return true;
//            
//    }
	
}
