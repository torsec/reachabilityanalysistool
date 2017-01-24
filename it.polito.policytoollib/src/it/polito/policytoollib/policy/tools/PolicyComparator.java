package it.polito.policytoollib.policy.tools;



import it.polito.policytoollib.exception.policy.DuplicatedRuleException;
import it.polito.policytoollib.exception.policy.EmptySelectorException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.NotInSemiLatticeException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.ComposedPolicy;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalFormGenerator;
import it.polito.policytoollib.policy.translation.semilattice.SemiLatticeGenerator;
import it.polito.policytoollib.policy.translation.semilattice.Semilattice;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.GenericRule;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
 * The Class PolicyComparator compares two policies and checks if the two are equal or not, 
 * it has two private member variable one for each policy.
 * 
 */
public class PolicyComparator {

	
	private CanonicalForm can;
	private Semilattice<GenericRule> sl;

	/**
	 * Compare.
	 *
	 * @throws Exception the exception
	 */
	public Set<Semilattice<GenericRule>> compare(Policy policy1, Policy policy2, SelectorTypes selectorTypes) throws Exception{
		
		LinkedList<LinkedList<Policy>> policy_list = new LinkedList<LinkedList<Policy>>();
		
		LinkedList<Policy> pl1 = new LinkedList<Policy>();
		pl1.add(policy1);
		policy_list.add(pl1);
		
		LinkedList<Policy> pl2 = new LinkedList<Policy>();
		pl2.add(policy2);
		policy_list.add(pl2);
		
		ComposedPolicy policy = new ComposedPolicy(policy_list, PolicyType.FILTERING, policy1.getName()+"_||_"+policy2.getName());
		
		if(policy.getRuleSet().size()==0){
			new HashSet<Semilattice<GenericRule>>();
		}
		
		can = CanonicalFormGenerator.getInstance(policy, selectorTypes).getCanonicalForm();
		CanonicalFormGenerator.getInstance(policy, selectorTypes).generateClosure();
		SemiLatticeGenerator slgen = new SemiLatticeGenerator();
		slgen.generateSemilattice(can);
		sl = can.getSemiLattice();
		
		
		return getDifferences();
	}
	
	/**
	 * The function isEqual() compares the two policies and returns true if the two policies 
	 * are equal and false if the two policies are not equal.
	 *
	 * @return the boolean
	 * @throws Exception the exception
	 */
	private Boolean isEqual() throws Exception{
		
		for(GenericRule rule:can.getRuleSet())
			if(rule.getAction().equals(FilteringAction.INCONSISTENT)){
				
				
				Collection<GenericRule> rule_collection = new HashSet<GenericRule>();
				
				for(GenericRule r:sl.getOutgoingAdjacentVertices(rule)){
					if(!r.equals(sl.getTop()))
						rule_collection.add(r);
				}
				
				
				if(!can.getRuleClassifier().isHidden(rule,(GenericRule[])rule_collection.toArray()))
					return false;
			//	else
				//	rule.setAction(FilteringAction.HIDDEN_INCONSISTENT);
			}
			
		return true;
	}

	/**
	 * The public function getDifferences returns a set of semil-lattice, 
	 * for every rule which is different in one of the two policy one.
	 *
	 * @return the differences
	 * @throws Exception the exception
	 */
	private Set<Semilattice<GenericRule>> getDifferences() throws Exception{
		
		HashSet<Semilattice<GenericRule>> set = new HashSet<Semilattice<GenericRule>>();
		
		SemiLatticeGenerator SL_gen = new SemiLatticeGenerator();
		
		for(GenericRule rule:getMinInconsistent()){
			set.add(SL_gen.getSubSemiLattice(sl, rule));
		}
		
		return set; 
	}

	
	/**
	 * The private function getMinInconsistent() searches all differances for the to 
	 * policies using the semi-lattice calculated by the canonical form. It uses the 
	 * private function getMinInconsistentRec().
	 *
	 * @return the min inconsistent
	 * @throws Exception the exception
	 */
	private HashSet<GenericRule> getMinInconsistent()throws Exception{
		return getMinInconsistentRec(sl.getOutgoingAdjacentVertices(sl.getRoot()), sl);
	}
	
	/**
	 * The private function getMinInconsistentRec() is a recursive function which searches in 
	 * the semilattice of the canonical form for differeneces between the two policies.
	 *
	 * @param rule_list the rule_list
	 * @param sl the sl
	 * @return the min inconsistent
	 * @throws EmptySelectorException the empty selector exception
	 * @throws UnsupportedSelectorException the unsupported selector exception
	 * @throws NotInSemiLatticeException the not in semi lattice exception
	 * @throws UnmanagedRuleException 
	 * @throws DuplicatedRuleException 
	 * @throws NoExternalDataException 
	 */
	private HashSet<GenericRule> getMinInconsistentRec(List<GenericRule> rule_list, Semilattice<GenericRule> sl) throws EmptySelectorException, UnsupportedSelectorException, NotInSemiLatticeException, NoExternalDataException, DuplicatedRuleException, UnmanagedRuleException{
		HashSet<GenericRule> rule_set = new HashSet<GenericRule>();
		for(GenericRule rule:rule_list){
			if(!rule.equals(sl.getTop())){
				if(rule.getAction()==FilteringAction.INCONSISTENT){
					
					Collection<GenericRule> rule_collection = new HashSet<GenericRule>();
					
					for(GenericRule r:sl.getOutgoingAdjacentVertices(rule)){
						if(!r.equals(sl.getTop()))
							rule_collection.add(r);
					}
										
					rule_set.add(rule);
					
//					if(rule_collection.size()!=0 && !can.getRuleClassifier().isHidden(rule,rule_collection.toArray(new GenericRule[rule_collection.size()])))
//						rule_set.add(rule);
//					else{
//						//rule.setAction(FilteringAction.HIDDEN_INCONSISTENT);
//						rule_set.addAll(getMinInconsistentRec(sl.getOutgoingAdjacentVertices(rule), sl));
//					}
				} else {
					rule_set.addAll(getMinInconsistentRec(sl.getOutgoingAdjacentVertices(rule), sl));
				}
			}
		}
		return rule_set;
	}
	
}

