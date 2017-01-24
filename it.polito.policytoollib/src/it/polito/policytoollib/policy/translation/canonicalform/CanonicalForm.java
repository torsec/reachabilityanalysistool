package it.polito.policytoollib.policy.translation.canonicalform;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.NotPointException;
import it.polito.policytoollib.exception.policy.ResolutionErrorException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.translation.semilattice.Semilattice;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.policy.utils.RuleClassifier;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.util.IndexingBitSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class CanonicalForm implements Policy {

	private Policy policy;

	private Set<GenericRule> canRules;
	
	private HashMap<GenericRule, IndexingBitSet> canLabels;
	private HashMap<IndexingBitSet, GenericRule> ibsLabel;
	
	private Semilattice<GenericRule> semiLattice;
	
	private SelectorTypes selectorTypes;
	
	private PolicyType policyType;

	public CanonicalForm(Policy policy, SelectorTypes selectorTypes) {

		this.policy=policy;
		this.selectorTypes = selectorTypes;
		this.policyType = PolicyType.FILTERING;

		canRules = null;
		
		semiLattice = null;
	}
	@Override
	public void insertRule(GenericRule rule) throws NoExternalDataException, OperationNotPermittedException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
	}

	@Override
	public <S> void insertRule(GenericRule rule, S externalData)
	throws IncompatibleExternalDataException,
	DuplicateExternalDataException, OperationNotPermittedException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
	}
	
	@Override
	public <S> void insertRule(GenericRule rule, String externalData)
	throws IncompatibleExternalDataException,
	DuplicateExternalDataException, OperationNotPermittedException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
	}

	@Override
	public void insertAll(Collection<GenericRule> rules)
	throws NoExternalDataException, OperationNotPermittedException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
	}

	@Override
	public <S> void insertAll(HashMap<GenericRule, S> rules)
	throws NoExternalDataException, IncompatibleExternalDataException,
	DuplicateExternalDataException, OperationNotPermittedException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
	}

	@Override
	public void removeRule(GenericRule rule) throws UnmanagedRuleException,
	OperationNotPermittedException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
	}

	@Override
	public void removeAll(Collection<GenericRule> rules)
	throws UnmanagedRuleException, OperationNotPermittedException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
	}

	@Override
	public boolean containsRule(GenericRule rule) {
		return canRules.contains(rule);
	}

	@Override
	public void clearRules() throws OperationNotPermittedException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
	}

	@Override
	public Action getDefaultAction() {
		return policy.getDefaultAction();
	}

	@Override
	public ResolutionStrategy getResolutionStrategy() {
		return policy.getResolutionStrategy();
	}

	@Override
	public Set<GenericRule> getRuleSet() {
		return canRules; 
	}

	@Override
	public int size(){
		return canRules.size();
	}


	@Override
	public Policy clone() {
		return null;
	}
	

	
	private GenericRule findLUBRule(ConditionClause c) throws Exception {
		
		Semilattice<GenericRule> sl = semiLattice;
		
		GenericRule ret=sl.getRoot();
		
		boolean go=true;

		while (go){
			go=false;
			for(GenericRule r:sl.getOutgoingAdjacentVertices(ret)){
				if (r.isIntersecting(c)){
					ret=r;
					go=true;
					continue;
				}
			}
		}

		if (ret == sl.getRoot())
			return null;
		else return ret;
	}

	@Override
	public HashSet<GenericRule> match(ConditionClause c) throws Exception {
		
		if (!c.isPoint(policy.getSelectorNames()))
			throw new NotPointException();
		
		GenericRule lub = findLUBRule(c);

		HashSet<GenericRule> matchingRules = new HashSet<GenericRule>();
		
		if(lub!=null){
			
			matchingRules.add(lub);
			
			IndexingBitSet ibsLUB = canLabels.get(lub);
			
			for (IndexingBitSet ibs : ibsLabel.keySet())
				if (ibsLUB.hasAtLeastTheSameBitsAs(ibs))
					matchingRules.add(ibsLabel.get(ibs));
			
		}
		return matchingRules;
	}

	@Override
	public Action evalAction(ConditionClause c) throws Exception {
		if (!c.isPoint(policy.getSelectorNames()))
			throw new NotPointException();

		GenericRule lub = findLUBRule(c);
		
		if(lub!=null)
			return lub.getAction();
		else
			return this.getDefaultAction();
	}

	public GenericRule[] getOriginalRules(GenericRule rule) {
		GenericRule[] rules1 = CanonicalFormGenerator.getInstance(policy, selectorTypes).decomposeRule(rule, this);
		GenericRule[] rules = new GenericRule[rules1.length-1];
		for(int i=0; i<rules1.length-1; i++)
			rules[i]=rules1[i];
		return rules;
	}

	
	public void setSemiLattice(Semilattice<GenericRule> semiLattice){
		
		this.semiLattice = semiLattice;
	}
	
	public void setRules(Set<GenericRule> rules) {
		canRules = rules;
	}
	
	public Semilattice<GenericRule> getSemiLattice() throws Exception {
		return semiLattice;
	}

	public HashMap<GenericRule, IndexingBitSet> getLabels() throws UnsupportedSelectorException, UnmanagedRuleException, ResolutionErrorException, NoExternalDataException, DuplicateExternalDataException, IncompatibleExternalDataException, InvalidActionException {
		return canLabels;
	}
	
	public void setLabels(HashMap<GenericRule, IndexingBitSet> canLabels) {
		this.canLabels = canLabels;
	}
	
	public void setIbsLabels(HashMap<IndexingBitSet, GenericRule> ibsLabel) {
		this.ibsLabel = ibsLabel;
	}
	public Policy getOriginalPolicy() {
		return policy;
	}
	@Override
	public String getName() {
		return "CAN_"+policy.getName();
	}
	@Override
	public HashSet<String> getSelectorNames() {
		return policy.getSelectorNames();
	}
	@Override
	public Policy policyClone() {
		return null;
	}
	@Override
	public PolicyType getPolicyType() {
		return policyType;
	}
	@Override
	public RuleClassifier getRuleClassifier() {
		return null;
	}
	@Override
	public <S> void insertRuleNoCheck(GenericRule rule, S externalData)
			throws IncompatibleExternalDataException,
			OperationNotPermittedException, UnsupportedSelectorException {
		throw new OperationNotPermittedException("Not allowed for Canonical Form Instance");
		
	}

}
