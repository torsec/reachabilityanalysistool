package it.polito.policytoollib.generator.util;

import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.GenericRule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PolicyStatistics {

	Policy policy;
	HashMap<GenericRule, List<GenericRule>> intersecting;	
	List<GenericRule> byIntersection;
	public HashMap<Integer,List<GenericRule>> byIntersectingNo;
	int max;
	int average,dev;
	int nonIntersectingNo;
	int intersectingNo;
	
	public PolicyStatistics(Policy policy){
		this.policy = policy;
		fillIntersecting();
		calculateStatistics();
	}
	
	private void fillIntersecting(){
		intersecting = new HashMap<GenericRule, List<GenericRule>>();
		byIntersectingNo = new HashMap<Integer,List<GenericRule>>();
//		System.out.println("=====================================\n\n");
		GenericRule [] rules = (GenericRule []) this.policy.getRuleSet().toArray(new GenericRule[0]);
		int rulesetsize=this.policy.getRuleSet().size();
		for(int i=0; i<rulesetsize;i++){
//			System.out.println("Rule: "+rules[i]);
			for(int j=i+1;j<rulesetsize;j++){
//				System.out.println("\tRule: "+j);
				if(rules[i].isIntersecting(rules[j])){
//					System.out.println("Rule "+i+","+j+"=intersecting");
					if(!intersecting.containsKey(rules[i]))
						intersecting.put(rules[i], new LinkedList<GenericRule>());
					if(!intersecting.containsKey(rules[j]))
						intersecting.put(rules[j], new LinkedList<GenericRule>());
					intersecting.get(rules[i]).add(rules[j]);
					intersecting.get(rules[j]).add(rules[i]);
				}
				
			}
		}
		byIntersection = new LinkedList<GenericRule>(intersecting.keySet());
		
		
		for(GenericRule r: intersecting.keySet()){
//			System.out.println("FI:"+r.getLabel());
			if(!byIntersectingNo.containsKey(intersecting.get(r).size()))
				byIntersectingNo.put(intersecting.get(r).size(),new LinkedList<GenericRule>());
			byIntersectingNo.get(intersecting.get(r).size()).add(r);
		}
		Collections.sort(byIntersection, new IntersectionComparator());
//		System.out.println(byIntersection);
	}
	
	private void calculateStatistics(){
		intersectingNo = intersecting.keySet().size();
		nonIntersectingNo = policy.getRuleSet().size()-intersectingNo;
//		System.out.println("CHECK: int="+intersectingNo+" nonint="+nonIntersectingNo+" Total="+policy.getRuleSet().size());
		max = 0;average=0;
		for(GenericRule r: intersecting.keySet()){
			average+=intersecting.get(r).size();
			if(intersecting.get(r).size() > max){
				max=intersecting.get(r).size();
			}
		}
		for(GenericRule r: intersecting.keySet()){
			dev+=(intersecting.get(r).size()-average)*(intersecting.get(r).size()-average);
		}
		
	}
	
	public int getMAX(){
		return max;
	}
	
	public int getIntersectingNo(){
		return intersectingNo;
	}
	
	public double getIntersectingNoPercent(){
		return (double)intersectingNo/policy.getRuleSet().size()*100;
	}
	
	public int getAverage(){
		return average;
	}
	
	public void printStatistics() throws Exception{
		System.out.println("Rule No="+policy.size());
		System.out.println("Intersecting No="+intersectingNo);
		System.out.println("Non Intersecting No="+nonIntersectingNo);
		System.out.println("Intersecting Percent="+((double)intersectingNo/policy.getRuleSet().size()*100)+"%");	
		System.out.println("Max="+max);
		System.out.println("Average intersecting="+((double)average/intersectingNo));
		System.out.println("Average global="+((double)average/policy.getRuleSet().size()));
		System.out.println("Variance intersecting="+(Math.sqrt((double)dev/intersectingNo)));
		System.out.println("Variance global="+(Math.sqrt((double)dev/policy.getRuleSet().size())));
	}

	public class IntersectionComparator implements Comparator<GenericRule>{
	    @Override
	    public int compare(GenericRule r1, GenericRule r2) {
	    	int r1int=0,r2int=0;
	        if(intersecting.containsKey(r1))
	        	r1int=intersecting.get(r1).size();
	        if(intersecting.containsKey(r2))
	        	r2int=intersecting.get(r2).size();
	        return (r1int>r2int ? -1 : (r1int==r2int ? 0 : 1));
	    }
	}
	
	
	public void printIntersecting() {
		
//		for(GenericRule r:intersecting.keySet()){
//			System.out.println("BASE RULE: "+r.getLabel());
//			System.out.println("INTERSECTING LIST");
//			for(GenericRule ri:intersecting.get(r)){
//				System.out.println(ri.getLabel());
//			}
//		}
//		System.out.println("\n\n");
		List<Integer> li = new LinkedList<Integer>();
		li.addAll(byIntersectingNo.keySet());
		Collections.sort(li);
		
		//print t
		for(Integer n:li){
			System.out.print(""+n+":");
			for(GenericRule r:byIntersectingNo.get(n)){
				System.out.print(r.getName()+" ");
			}
			System.out.println("");
		}
		
		
	}
	
}
