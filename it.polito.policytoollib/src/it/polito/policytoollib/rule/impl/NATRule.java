package it.polito.policytoollib.rule.impl;

import it.polito.policytoollib.rule.action.Action;

import java.util.HashSet;
import java.util.LinkedList;


public class NATRule extends GenericRule{

	private HashSet<GenericRule> rules;
	private ConditionClause natCondition;
	
	public NATRule(Action action, ConditionClause conditionClause, String name){
		super(action, conditionClause, name);
		rules = new HashSet<GenericRule>();
	}
	
	public void setNATRule(ConditionClause natCondition){
		this.natCondition = natCondition;
	}
	
	public ConditionClause getNATRule() {
		return natCondition;
	}

	public HashSet<GenericRule> getOriginalRules(){
		return rules;
	}
	
	public void addOriginalRule(GenericRule rule){
		if(rule instanceof NATRule)
			addAllOriginalRules(((NATRule) rule).getOriginalRules());
		else
			rules.add(rule);
	}
	
	public void deletOriginalRule(GenericRule rule){
		rules.remove(rule);
	}
	
	public void addAllOriginalRules(LinkedList<GenericRule> rules){
		for(GenericRule rule:rules){
			if(rule instanceof NATRule){
				NATRule nr=(NATRule)rule;
				addAllOriginalRules(nr.getOriginalRules());
			} else
				addOriginalRule(rule);
		}
		
		
	}
	
	private void addAllOriginalRules(HashSet<GenericRule> rules){
		this.rules.addAll(rules);
	}
	
	@Override
	public String toString() {
		String str = "";

		if (name != null)
			if (!name.equalsIgnoreCase(""))
				str = name + "\n";
		

		str += conditionClause.toString();

		str += ("Action: " + action+"\n");
		
		str += natCondition.toString();
		
		if(name.substring(0,5).compareTo("NATDA")!=0) for(GenericRule r: rules)
		{
			str += r.toString();
		}
		return str;
	}
	
	public GenericRule ruleClone(){
		NATRule rule = new NATRule(action.actionClone(), conditionClause.conditionClauseClone(), name);
		rule.setNATRule(natCondition.conditionClauseClone());
		rule.addAllOriginalRules(rules);
		return  rule;
	}
}
