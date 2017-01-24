package it.polito.policytoollib.policy.translation.morphisms;

import it.polito.policytoollib.rule.impl.GenericRule;

import java.util.List;


public interface GenericMorphism {
	public List<GenericRule> exportRules() throws Exception;
}
