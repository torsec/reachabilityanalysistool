package it.polito.policytoollib.policy.impl;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.policy.UnmanagedRuleException;
import it.polito.policytoollib.exception.rule.OperationNotPermittedException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.policy.utils.RuleClassifier;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.impl.NATRule;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public interface Policy extends Cloneable{
	
	public String getName();
	
	public PolicyType getPolicyType();
	
	public void insertRule(GenericRule rule) throws NoExternalDataException, OperationNotPermittedException, UnsupportedSelectorException;
	
	public <S> void insertRule(GenericRule rule, String externalData) throws IncompatibleExternalDataException, DuplicateExternalDataException, OperationNotPermittedException, UnsupportedSelectorException;
	
	public <S> void insertRule(GenericRule rule, S externalData) throws IncompatibleExternalDataException, DuplicateExternalDataException, OperationNotPermittedException, UnsupportedSelectorException;
	
	public <S> void insertRuleNoCheck(GenericRule rule, S externalData) throws IncompatibleExternalDataException, OperationNotPermittedException, UnsupportedSelectorException;;
	
	public void insertAll(Collection<GenericRule> rules) throws NoExternalDataException, OperationNotPermittedException, UnsupportedSelectorException;
	
	public <S> void insertAll(HashMap<GenericRule,S> rules) throws NoExternalDataException, IncompatibleExternalDataException, DuplicateExternalDataException, OperationNotPermittedException, UnsupportedSelectorException;
	
	public void removeRule(GenericRule rule) throws UnmanagedRuleException, OperationNotPermittedException;
	
	public void removeAll(Collection<GenericRule> rules) throws UnmanagedRuleException, OperationNotPermittedException;
	
	public boolean containsRule(GenericRule rule);
	
	public void clearRules() throws OperationNotPermittedException;

	public Action getDefaultAction();

	public ResolutionStrategy getResolutionStrategy() ;

	public Set<GenericRule> getRuleSet();

	public int size();
	
	public HashSet<String>  getSelectorNames();
	
	@Override
	public String toString();

	public Action evalAction(ConditionClause punto) throws Exception; 

	public HashSet<GenericRule> match(ConditionClause point) throws Exception;
	
	public Policy policyClone();
	
	public RuleClassifier getRuleClassifier();

	
}