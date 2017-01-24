package it.polito.policytoollib.policy.tools;


import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.DuplicatedRuleException;
import it.polito.policytoollib.exception.policy.EmptySelectorException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.NotInSemiLatticeException;
import it.polito.policytoollib.exception.policy.ResolutionErrorException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.anomaly.PolicyAnomaly;
import it.polito.policytoollib.policy.anomaly.PolicyConflictResult;
import it.polito.policytoollib.policy.anomaly.RuleAnomalyAnalyzer;
import it.polito.policytoollib.policy.anomaly.utils.ConflictType;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.policy.impl.EquivalenPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.ResolutionComparison;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.semilattice.Semilattice;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.DummyAction;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.action.IPSecAction;
import it.polito.policytoollib.rule.action.IPSecActionSet;
import it.polito.policytoollib.rule.action.IPSecActionType;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.impl.NATRule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
	



// TODO: Auto-generated Javadoc
/**
 * The class Analyzer analyzes composed policies and returns the anomalies. 
 * The class uses the singleton pattern and has three private member variables: 
 * analyzer
 * graph
 * policyLabel.   
 */
public class AnomalyAnalyser {
	
	private SelectorTypes selectorTypes;
	
	
	public AnomalyAnalyser(SelectorTypes selectorTypes){
		this.selectorTypes = selectorTypes;
	}


	public Set<PolicyAnomaly> getIntraPolicyAnomalies(Policy policy){
		Set<PolicyAnomaly> anomaly_list = new HashSet<PolicyAnomaly>();
		
		RuleAnomalyAnalyzer analyzer = new RuleAnomalyAnalyzer(policy, selectorTypes);	
		
		LinkedList<Policy> policy_list = new LinkedList<Policy>();
		
		policy_list.add(policy);
		
		for(GenericRule r:policy.getRuleSet()){
			
			HashMap<GenericRule,ConflictType> conflict_map = analyzer.getAllAnomaliesExternal(r);
			
			for(GenericRule rr:conflict_map.keySet()){
				
				if(conflict_map.get(rr)!=ConflictType.NON_CONFLICTING 
						&& conflict_map.get(rr)!=ConflictType.NON_INTERSECTING
						&& conflict_map.get(rr)!=ConflictType.INTERSECTING_but_NOT_CONFLICTING
						&& conflict_map.get(rr)!=ConflictType.LESS_but_NOT_CONFLICTING
						&& conflict_map.get(rr)!=ConflictType.GREATER_but_NOT_CONFLICTING
						&& conflict_map.get(rr)!=ConflictType.IDENTICAL){
					GenericRule[] rule_set = new GenericRule[2];
					rule_set[0]=r;
					rule_set[1]=rr;
					PolicyAnomaly anomaly = new PolicyAnomaly(policy_list, rule_set, conflict_map.get(rr));
					
					anomaly_list.add(anomaly);
				}
			}
		}
		
		return anomaly_list;
	}


	public Set<PolicyAnomaly> getInterPolicyAnomalies(EquivalenPolicy equivalenPolicy) throws UnsupportedSelectorException, UnmanagedRuleException, ResolutionErrorException, NoExternalDataException, DuplicateExternalDataException, IncompatibleExternalDataException, InvalidActionException, EmptySelectorException, NotInSemiLatticeException, Exception{
		ComposedPolicy policy = equivalenPolicy.getPolicy();
		CanonicalForm can = equivalenPolicy.getCan();
		Semilattice<GenericRule> sl = equivalenPolicy.getSl();
		
		Set<PolicyAnomaly> anomaly_list = new HashSet<PolicyAnomaly>();
		
		if(can!=null){
			for(GenericRule rule:can.getRuleSet()){
				
				anomaly_list.addAll(getRuleAnomalies(can.getOriginalRules(rule), policy.getOriginalPolicy(), policy.getPolicyList(), can));
				
				if(rule.getAction().equals(FilteringAction.INCONSISTENT)){
					
					HashSet<GenericRule> rule_collection = new HashSet<GenericRule>();
	
					for(GenericRule r:sl.getOutgoingAdjacentVertices(rule)){
						if(!r.equals(sl.getTop()))
							rule_collection.add(r);
					}
					
					if(!can.getRuleClassifier().isHidden(rule,(GenericRule[])rule_collection.toArray()))
						anomaly_list.add(new PolicyAnomaly(can.getOriginalRules(rule), ConflictType.CONFLICTING));
					
				}
			}
		}
		return anomaly_list;
	}
	
	/**
	 * The private function getRuleAnomalies() searches the anomalies for a given array of rules. 
	 * First for every list in the policy_list the function calls the function getIPSecSerialConflict() 
	 * and than searches for serial conflicts, after that it calls the function getIPSecParallelConflict(). 
	 * All conflicts found in the process are saved in the locale variable anomalie_set, which is return 
	 * at the end.
	 *
	 * @param rules the rules
	 * @param org_policy_list the org_policy_list
	 * @param policy_list the policy_list
	 * @param can the can
	 * @return the rule anomalies
	 * @throws NoExternalDataException the no external data exception
	 * @throws InvalidActionException the invalid action exception
	 * @throws UnmanagedRuleException 
	 * @throws DuplicatedRuleException 
	 */
	private HashSet<PolicyAnomaly> getRuleAnomalies(GenericRule[] rules, List<Policy> org_policy_list, LinkedList<LinkedList<Policy>> policy_list, CanonicalForm can) throws NoExternalDataException, InvalidActionException, DuplicatedRuleException, UnmanagedRuleException{
		HashSet<PolicyAnomaly> anomalie_set =  new HashSet<PolicyAnomaly>();
		
		HashMap<Policy, HashSet<GenericRule>> rp = new HashMap<Policy, HashSet<GenericRule>>();
		for(Policy p:org_policy_list){
			HashSet<GenericRule> hs = new HashSet<GenericRule>();
			rp.put(p, hs);
		}
		
		HashSet<GenericRule> rs;
		for(GenericRule r: rules){
			boolean found=false;
			for(Policy p:org_policy_list){
				rs=rp.get(p);
				if(p.containsRule(r)){
					rs.add(r);
					found=true;
				}
			}
			if(!found){
				System.err.println("E:"+r.getName()+" : "+r.hashCode());
				throw new NoExternalDataException();
			}
		}
		
	
		
		int[] secLevel_list = new int[policy_list.size()];
		int i=0;
		for(LinkedList<Policy> p_list:policy_list){
			Action[] a_list = new Action[p_list.size()];
			int ii=0;
			int count=0;
			for(Policy p:p_list){
				if(rp.get(p).isEmpty()){
					a_list[ii++]=p.getDefaultAction();
				}else{ 
					count+=rp.get(p).size();
					for(GenericRule r1:rp.get(p)){
						if(r1.getAction()==DummyAction.DUMMY){
							for(GenericRule r2:rp.get(p)){
								if(	p.getResolutionStrategy().compare(r1, r2)==ResolutionComparison.DIFFERENT_SET_LESS){
									GenericRule[] c_rules = new GenericRule[((NATRule)r1).getOriginalRules().size()+1];
									int j=0;
									for(GenericRule r:((NATRule)r1).getOriginalRules()){
										c_rules[j++]=r;
									}
									c_rules[j] = r2;
									anomalie_set.add(new PolicyAnomaly(p_list, c_rules, ConflictType.INTER_TECH_CONFLICT));
								}
							}
						}
							
					}
					a_list[ii++]=p.getResolutionStrategy().composeActions(rp.get(p));
				}
			}
			
			PolicyConflictResult policy_conflict = getIPSecSerialConflict(a_list);
			
			secLevel_list[i++]=policy_conflict.getSecLevel();
			
			ConflictType conflict = policy_conflict.getConflict();		
			if(conflict!=ConflictType.NON_CONFLICTING)
				anomalie_set.add(new PolicyAnomaly(p_list, rules, conflict));

			
			
			
			if(count>0 && a_list.length>1){
				GenericRule[] c_rules = new GenericRule[count];
				count--;
				for(Policy p:p_list)
					for(GenericRule r:rp.get(p))
						c_rules[count--]=r;
				if(a_list[0]==FilteringAction.DENY){
					boolean found=false;
					for(Action a:a_list){
						if(a==FilteringAction.ALLOW){
							anomalie_set.add(new PolicyAnomaly(p_list, c_rules, ConflictType.SHADOWED));
							found=true;
							break;
						} else if(a!=FilteringAction.DENY){
							anomalie_set.add(new PolicyAnomaly(p_list, c_rules, ConflictType.INTER_TECH_CONFLICT));
							found=true;
							break;
						}
					}
					if(!found)
						anomalie_set.add(new PolicyAnomaly(p_list, c_rules, ConflictType.REDUNDANT));
				} else if(can.getResolutionStrategy().composeActions(rules)==FilteringAction.DENY)
					anomalie_set.add(new PolicyAnomaly(p_list, c_rules, ConflictType.SPURIOUS));
			}
			
		}
		
		
		ConflictType conflict = getIPSecParallelConflict(secLevel_list);		
		if(conflict!=ConflictType.NON_CONFLICTING)
			anomalie_set.add(new PolicyAnomaly(rules, conflict));
		
		return anomalie_set;
	}
	
	/**
	 * The private function getIPSecParallelConflict() checks if every path in the network applies the same IPSec options to the packets.
	 *
	 * @param secLevel_list the sec level_list
	 * @return the iP sec parallel conflict
	 */
	private ConflictType getIPSecParallelConflict(int[] secLevel_list) {
				
		if(secLevel_list.length!=0){
			int ii = secLevel_list[0];
			for(int i=1; i<secLevel_list.length; i++){
				if(ii!=secLevel_list[i])
					return ConflictType.SECLEVEL_CONFLICT;
			}
		}
		return ConflictType.NON_CONFLICTING;
	}
	
	/**
	 * The private function getIPSecSerialConflict() checks if on a path in the network exists IPSec conflicts.
	 *
	 * @param actionList the action list
	 * @return the iP sec serial conflict
	 */
	private PolicyConflictResult getIPSecSerialConflict(Action[] actionList) {
		Stack<IPSecAction> IPSecActionStack = new Stack<IPSecAction>();
		int secLevel=-1;
		ConflictType result_conflict=ConflictType.NON_CONFLICTING;
		
		for(Action action:actionList){
			if(action instanceof IPSecAction || action instanceof IPSecActionSet){
				
				LinkedList<IPSecAction> ipsecactionlist;
				if(action instanceof IPSecAction){
					ipsecactionlist= new LinkedList<IPSecAction>();
					ipsecactionlist.add((IPSecAction)action);
				}else
					ipsecactionlist = ((IPSecActionSet)action).getSecActionList();
				
				for(IPSecAction a:ipsecactionlist){
					if(a.getType()==IPSecActionType.AH || a.getType()==IPSecActionType.ESP){
						if(IPSecActionStack.size()>1)
							result_conflict=ConflictType.IPSEC_OVERLAP;
						
						if(IPSecActionStack.size()==1 && a.getType()==IPSecActionType.ESP){
							IPSecAction temp = IPSecActionStack.pop();
							if(temp.getType()==IPSecActionType.AH){
								result_conflict=ConflictType.IPSEC_OVERLAP;
							}
							IPSecActionStack.push(temp);
						}
						IPSecActionStack.push(a);
						if(secLevel==-1)
							secLevel=1;
					}
					
					if(a.getType()==IPSecActionType.INVERT_AH || a.getType()==IPSecActionType.INVERT_ESP){
						if(!IPSecActionStack.isEmpty()){
							IPSecAction temp = IPSecActionStack.pop();
							if(!temp.isInvertEqual(a))
								return new PolicyConflictResult(ConflictType.IPSEC_INCONSISTENT,secLevel);
						} else
							return new PolicyConflictResult(ConflictType.IPSEC_INCONSISTENT,secLevel);
					}
				}
				
			} else {
				if(IPSecActionStack.isEmpty())
					secLevel=0;
			}
			if(action==FilteringAction.DENY)
				return new PolicyConflictResult(result_conflict,secLevel);
		}
		
		
		if(IPSecActionStack.isEmpty())
			return new PolicyConflictResult(result_conflict, secLevel);
		else
			return new PolicyConflictResult(ConflictType.IPSEC_INCONSISTENT,secLevel);
		
		
	}

}
