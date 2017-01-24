package it.polito.policytoollib.rule.selector.impl;

import it.polito.policytoollib.rule.selector.Selector;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.OtherOperations;
import dk.brics.automaton.RegExp;


public class SquidURLSelector extends RegExpSelectorImpl {
	
//	private boolean full=false;
//	private boolean empty=false;
	private  SquidURLType type;
	private boolean negated=false;
	private Automaton reduced_automaton;
	private static String selName="Squid URL";
	public Automaton getReducedAutomaton(){
		if (reduced_automaton!=null)
			return reduced_automaton.clone();
		return new Automaton();
	}
	
	@Override
	public String getName() {
		return selName;
	}
	public SquidURLSelector () {
		this.type = SquidURLType.URL_REGEX;
		empty();
	}
	
	public SquidURLSelector (SquidURLType type) {
		this.type = type;
		empty();
	}

	private Automaton convert_urlpath(){
		String s = new String(".*/");
		s = s.concat(this.regexp.toString());
		Automaton a = (new RegExp(s)).toAutomaton();
		//a.determinize();
		return a;	
	}
	
	private Automaton convert_dstdomain(){

		String s = new String(".*");
		s = s.concat(this.regexp.toString());
		s = s.concat("/.*");
		Automaton a = (new RegExp(s)).toAutomaton();
		//a.determinize();
		return a;	
		
	}
	
	private Automaton convert_dstdomain_regex(){
			String s = new String(".*");
			s = s.concat(this.regexp.toString());
			s = s.concat("/.*");
			System.err.println("CREATED STRING: "+s);
			Automaton a = (new RegExp(s)).toAutomaton();
			return a;
	}
	
	private Automaton generate_url_automaton(){
		if(type == SquidURLType.DSTDOMAIN){
			Automaton a = convert_dstdomain();
			return a;
		}
		else if(type == SquidURLType.DSTDOMAIN_REGEX){
			Automaton a = convert_dstdomain_regex();
			return a;
		}
		else if(type == SquidURLType.URLPATH_REGEX){
			Automaton a = convert_urlpath();
			return a;
		}
		else{
			return this.reduced_automaton;
		}
	}
	
//	public void setRegExp(RegExp regexp){
//		this.regexp = regexp;
//		if(regexp.toString().equals(".*")){
//			this.setFull();
//			return;
//		}
//		
//		this.empty = false;
//		this.full = false;
//		
//		if (reduced_automaton == null)
//			reduced_automaton =regexp.toAutomaton();
//		else reduced_automaton = BasicOperations.union(reduced_automaton,regexp.toAutomaton());
//		reduced_automaton.determinize();
//		
//		automata = generate_url_automaton();	
//		this.empty = false;
//	}
			
	public void setRegExp(String regexp){
		
		if(regexp.equals(".*")){
			this.setFull();
			return;
		}
			
		if (regexp.startsWith("^"))
				regexp = regexp.substring(1);
		else if(!regexp.startsWith(".*")){
			regexp = ".*" + regexp;
		}
		
		if (regexp.endsWith("$"))
			regexp = regexp.substring(0, regexp.length()-1);
		else if(! regexp.endsWith(".*"))
			regexp = regexp.concat(".*");
		
		
		this.regexp = new RegExp(regexp);
		
		if(type !=SquidURLType.URL_REGEX){
			reduced_automaton = this.regexp.toAutomaton();
			automata = generate_url_automaton();
		}
		else{
			automata = this.regexp.toAutomaton();
			reduced_automaton = automata;
		}
//		if (reduced_automaton == null)
//			reduced_automaton =this.regexp.toAutomaton();
//		else reduced_automaton = BasicOperations.union(reduced_automaton,this.regexp.toAutomaton());
//		reduced_automaton.determinize();

		this.empty = false;
		this.full = false;
	}
	
	public void setFull(){
		super.setFull();
		negated=false;
		reduced_automaton = null;	
	}
	
	public void empty(){
		super.empty();
		negated=false;
		reduced_automaton = null;
	}
	
	
	private void clone_non_empty_selector(SquidURLSelector s){
		this.full=s.full;
		this.empty=s.empty;
		this.negated=s.negated;
		this.reduced_automaton = s.reduced_automaton.clone();
		this.automata = s.automata.clone();	
		this.regexp = null;
	}
	
	private RegExp update_regex(Automaton automaton){
		String reg_str = OtherOperations.toRegExp(automaton);
		
		if(reg_str.startsWith("^")){
			reg_str = reg_str.substring(1)+".*";
		}
		
		reg_str = reg_str.replace(".*", ";");
		reg_str = reg_str.replace(".", "\\.");
		reg_str = reg_str.replace(";", ".*");
		
		return new RegExp(reg_str);
	}
	
	@Override
	public void intersection(Selector s) throws IllegalArgumentException {
		
		if(((SquidURLSelector)s).full)
			return;
		
		if(this.full)
			clone_non_empty_selector(((SquidURLSelector)s));
		
		if (this.empty || ((SquidURLSelector)s).empty)
			this.empty(); 

		if(this.type == ((SquidURLSelector)s).type){
			reduced_automaton = BasicOperations.intersection(reduced_automaton, ((SquidURLSelector)s).reduced_automaton);
			//TODO: aggiornare la variabile regex altrimenti l'istruzione successiva non funzionerà
			regexp = update_regex(reduced_automaton);
			automata = generate_url_automaton();
		}
		else if(this.type == SquidURLType.DSTDOMAIN && ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN_REGEX || 
				this.type == SquidURLType.DSTDOMAIN_REGEX && ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN)
		{
			type = SquidURLType.DSTDOMAIN;
//			reduced_automaton = BasicOperations.intersection(reduced_automaton, ((SquidURLSelector)s).reduced_automaton);
//			//TODO: aggiornare la variabile regex altrimenti l'istruzione successiva non funzionerà
//			
			
			regexp = update_regex(reduced_automaton);
			automata = generate_url_automaton();
			
		}
		else{
			type = SquidURLType.URL_REGEX;
			automata = BasicOperations.intersection(automata, ((RegExpSelectorImpl) s).automata);
			reduced_automaton = automata;
		}
	}
	
	@Override
	public boolean isIntersecting(Selector s) throws IllegalArgumentException {
		if (type == SquidURLType.URLPATH_REGEX && ( ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN || ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN_REGEX) )
			return false;
		
		if (((SquidURLSelector)s).type == SquidURLType.URLPATH_REGEX && ( type == SquidURLType.DSTDOMAIN || type == SquidURLType.DSTDOMAIN_REGEX) )
			return false;
		
		if (type == SquidURLType.URL_REGEX)
			if ( ((SquidURLSelector)s).type == SquidURLType.URL_REGEX  )
					return super.isIntersecting(s);
			else {
				//this URLREGEX, s!=URLREGEX
				return OtherOperations.intersecting(automata,  ( (SquidURLSelector) s).reduced_automaton);
			}
		else if ( ((SquidURLSelector)s).type == SquidURLType.URL_REGEX  ) {
			//This != URLREGEX, s == URLREGEX
			return OtherOperations.intersecting(reduced_automaton,  ( (SquidURLSelector) s).automata);
		}
		
		//Both different from URLREGEX
		return OtherOperations.intersecting(reduced_automaton, ( (SquidURLSelector) s).reduced_automaton);
		
	}
	

	@Override
	public boolean isEquivalent(Selector s) throws IllegalArgumentException {
		if (type == SquidURLType.URLPATH_REGEX && ( ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN || ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN_REGEX) )
			return false;
		if (((SquidURLSelector)s).type == SquidURLType.URLPATH_REGEX && ( type == SquidURLType.DSTDOMAIN || type == SquidURLType.DSTDOMAIN_REGEX) )
			return false;
		
		return super.isEquivalent(s);
	}

	@Override
	public boolean isFull() {
		return this.full;
	}
	


	@Override
	public boolean isSubset(Selector s) throws IllegalArgumentException {
		return super.isSubset(s);
	}

	@Override
	public boolean isSubsetOrEquivalent(Selector s)	throws IllegalArgumentException {
		return super.isSubsetOrEquivalent(s);
	}	


	@Override
	public Selector selectorClone() {
		SquidURLSelector clone = new SquidURLSelector(this.type);
		clone.automata = automata.clone();
		clone.reduced_automaton = reduced_automaton.clone();
		clone.setLabel(this.getLabel());
		clone.negated=negated;
		return clone;
	}
	
	@Override
	public void setLabel(String label) {
		super.setLabel(label);
	}

	@Override
	public void setMinus(Selector s) throws IllegalArgumentException {
		if (type == SquidURLType.URLPATH_REGEX && ( ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN || ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN_REGEX) ){
			return;
		}		
		if (((SquidURLSelector)s).type == SquidURLType.URLPATH_REGEX && ( type == SquidURLType.DSTDOMAIN || type == SquidURLType.DSTDOMAIN_REGEX) ){
			return;
		}
		if(this.type == ((SquidURLSelector)s).type){
			reduced_automaton = BasicOperations.minus(reduced_automaton, ((SquidURLSelector)s).reduced_automaton);
			//TODO: aggiornare la variabile regex altrimenti l'istruzione successiva non funzionerà
			regexp = update_regex(reduced_automaton);
			automata = generate_url_automaton();
		}
		else if(this.type == SquidURLType.DSTDOMAIN && ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN_REGEX || 
				this.type == SquidURLType.DSTDOMAIN_REGEX && ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN)
		{
			type = SquidURLType.DSTDOMAIN_REGEX;
			reduced_automaton = BasicOperations.minus(reduced_automaton, ((SquidURLSelector)s).reduced_automaton);
			//TODO: aggiornare la variabile regex altrimenti l'istruzione successiva non funzionerà
			regexp = update_regex(reduced_automaton);
			automata = generate_url_automaton();
		}
		else{
			type = SquidURLType.URL_REGEX;
			super.setMinus(s);
			reduced_automaton = automata;
		}
	}

	@Override
	public String toSimpleString() {
		System.out.println("Inizio conversione Automata - > regex");
		String str = OtherOperations.toRegExp(automata);
		//System.out.println(str);
		System.out.println("Finito conversione Automata - > regex");
		return str;
	}

	@Override
	public String toSquidString() {
		return "to implement";
	}
	


	@Override
	public void union(Selector s) throws IllegalArgumentException {

		if(this.type == ((SquidURLSelector)s).type){
			reduced_automaton = BasicOperations.union(reduced_automaton, ((SquidURLSelector)s).reduced_automaton);
			//TODO: aggiornare la variabile regex altrimenti l'istruzione successiva non funzionerà
			regexp = update_regex(reduced_automaton);
			automata = generate_url_automaton();
		}
		else if(this.type == SquidURLType.DSTDOMAIN && ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN_REGEX || 
				this.type == SquidURLType.DSTDOMAIN_REGEX && ((SquidURLSelector)s).type == SquidURLType.DSTDOMAIN)
		{
			type = SquidURLType.DSTDOMAIN_REGEX;
			reduced_automaton = BasicOperations.union(reduced_automaton, ((SquidURLSelector)s).reduced_automaton);
			//TODO: aggiornare la variabile regex altrimenti l'istruzione successiva non funzionerà
			regexp = update_regex(reduced_automaton);
			automata = generate_url_automaton();
		}
		else{
			type = SquidURLType.URL_REGEX;
			super.union(s);
			reduced_automaton = automata;
		}
		
	}

	@Override
	public long length() {		
		return -1;
	}

	public String toString(){
		
		if (type == SquidURLType.DSTDOMAIN)
			return "DSTDOMAIN: "+ reduced_automaton.getSingleton();
		
		if (type == SquidURLType.DSTDOMAIN_REGEX)
			return "DSTDOMAIN REGEX: "+ OtherOperations.toRegExp(reduced_automaton);
		
		if (type == SquidURLType.URLPATH_REGEX)
			return "URLPATH REGEX: "+ OtherOperations.toRegExp(reduced_automaton);

		return "URL REGEX: "+ OtherOperations.toRegExp(automata);	 
	}


	@Override
	public int getFirstAssignedValue() {
		return 0;
	}


	@Override
	public void full() {
		// TODO Auto-generated method stub
		
	}
}
