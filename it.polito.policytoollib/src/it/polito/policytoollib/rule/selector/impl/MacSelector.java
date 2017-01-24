package it.polito.policytoollib.rule.selector.impl;

import it.polito.policytoollib.exception.rule.InvalidMacAddressException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.utils.MacAddressManagement;

import java.math.BigInteger;


public class MacSelector extends TotalOrderedSelectorImpl {

	private long [] ranges;
	private long [] r_copy;
	private static String min="00:00:00:00:00:00";
	private static String max="FF:FF:FF:FF:FF:FF";
	private static long min_l = 0L;
	private static long max_l = 281474976710655L;
	private static String selName="Mac Address";
	
	protected String label=" ";

	public String getLabel() {
		return label;
	}

	public boolean isPoint(){
		if(ranges!=null){
			if(ranges[0]==ranges[1])
				if(ranges[0]!=-2)
					if(ranges.length>2){
						if(ranges[3]<0)
							return true;
					} else return true;
			
		}
		return false;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getName() {
		return selName;
	}
	

	public long length(){
		long sum =0;boolean toggle = true;
		long prev=0;
		for(long r: ranges){
			if(toggle){
				prev = r;
				if(prev<0) break;
			}
			else{
				sum+=r - prev + 1;
			}
			toggle=!toggle;
		}
		return sum;
	}

	
	private void initialize(long [] array){
		for (int i=0;i<array.length;i++)
			array[i]=-2;
	}

	/*
	 * 
	 */
	public void addRange(String Mac) throws InvalidMacAddressException{
		MacAddressManagement macam = MacAddressManagement.getInstance();
		//if (macam.getNetNumber(Ip)!=-1)
			//throw new InvalidIpAddressException();
		
		long s = macam.toLong(Mac);
		if (s<min_l || s>max_l)
			throw new InvalidMacAddressException();
		try{addRange(s,s);}
		catch(InvalidRangeException e){/*Unreachable*/}
	}

	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.TotalOrderedSelector#addRange(java.lang.Object, java.lang.Object)
	 */
	public void addRange(String MacStart, String MacEnd) throws InvalidRangeException, InvalidMacAddressException{
		MacAddressManagement macam = MacAddressManagement.getInstance();
		
		long s = macam.toLong(MacStart);
		long e = macam.toLong(MacEnd);
		if (s>e)
			throw new InvalidRangeException();
		addRange(s,e);        	
	}
	
	public void addRange(Object Start, Object End) throws InvalidRangeException {
		if (Start instanceof java.lang.String || End instanceof java.lang.String)
			try{addRange((String) Start, (String) End);}
			catch(InvalidMacAddressException e){throw new InvalidRangeException();}
		else throw new InvalidRangeException();
	}
	
	private void copy(){
		for (int i=0;i<ranges.length;i++)
			r_copy[i]=ranges[i];
	}
	
	public void addRange(long start, long end) throws InvalidMacAddressException, InvalidRangeException {
		if (this.isEmpty()){
			ranges = new long [2];
			ranges[0]=start;
			ranges[1]=end;
		}
		else if(start<min_l || end > max_l) throw new InvalidMacAddressException();
		else if(start>end) throw new InvalidRangeException();
		else {
			int size = ranges.length;
			r_copy = new long [size];
			copy();
			
			if (size > 2 && ranges[size-1]==-2)
				ranges = new long[size];
			else ranges = new long[size+2];
			initialize(ranges);
			
			int i=0, index=0;
			boolean done=false;
			
			while (i<size && !done){
				if (start >= r_copy[i] && end <= r_copy[i+1]) {
					done=true;
				} else if (start <= r_copy[i] && end >= r_copy[i+1]){
					i+=2;
				} else if (end < r_copy[i] && !(end == r_copy[i]-1)){
					ranges[index]=start;
					ranges[++index]=end;
					index++;
					i+=2;
					done = true;					
				} else if ( (end >= r_copy[i] || end+1 == r_copy[i]) && start < r_copy[i]) {
					ranges[index]=start;
					ranges[++index]=r_copy[i+1];
					i+=2;
					index++;
					done = true;
				} else if (start <= r_copy[i+1] || start-1 == r_copy[i+1]){
					start = r_copy[i];
					i+=2;
				} else if (start > r_copy[i+1]){
					if (!(i>0 && r_copy[i]==-2)){
						ranges[index]=r_copy[i];
						ranges[++index]=r_copy[i+1];
						index++;
					}
					i+=2;
				}
			}
			if (done) {
				for (;i<size;i++){
					ranges[index]=r_copy[i];
					ranges[++index]=r_copy[++i];
					index++;
				}
			} else {
				ranges[index]=start;
				ranges[++index]=end;
			}
		}
	}
	
	public void complement() {
		if (this.isFull()){
			empty();
			return;
		}
		
		if (this.isEmpty()){
			try { this.addRange(min_l,max_l);}
			catch (InvalidMacAddressException e) {/*Unreachable*/}
			catch (InvalidRangeException e) {/*Unreachable*/}
			return;
		}
			
		int sub=0;
		
		if (label.charAt(0)=='!'){
			sub++;
			if(label.charAt(0)=='(')
				sub++;
			label = label.substring(sub);
			if(label.charAt(label.length()-1)==')')
				label = (label.subSequence(0, label.length()-2)).toString();
		} else label = "!("+label+")";
		
		if (this.isEmpty()) {
			ranges=	 new long[2];
			ranges[0]=min_l;
			ranges[1]=max_l;
		} else {
			int size = ranges.length;
			r_copy = new long [size];
			copy();
			ranges = new long[size+2];
			initialize(ranges);
			int i=0, j=0;
			if(r_copy[0]>min_l) ranges[0]=min_l;
			else
			{
				ranges[0]=r_copy[1]+1;
				i=2;
			}
			for(j=1;i<r_copy.length&&(r_copy[i]!=-2);j+=2,i+=2)
			{
				ranges[j]=r_copy[i]-1;
				if(r_copy[i+1]==max_l)
					break;
				else
					ranges[j+1]=r_copy[i+1]+1;
			}
			if(ranges[j]==-2 && ranges[j-1]!=-2) ranges[j]=max_l;
		}		
	}
	
	public void empty() {
		ranges=null;
	}
	
	@Override
	public void intersection(Selector s) throws IllegalArgumentException {	
		if(!(s instanceof MacSelector))
			throw new IllegalArgumentException();
		
		if (!this.label.equalsIgnoreCase(s.getLabel()))
			if(!s.getLabel().equals(""))
				label = label + " || "+s.getLabel();
		
		if (isEmpty() || s.isFull())
			return;
		
		if (isFull()){
			ranges = ((MacSelector)s).ranges.clone();
			return;
		}
		
		if (s.isEmpty()){
			empty();
			return;
		}
		
	
		int index = ranges.length + ((MacSelector) s).ranges.length;
		r_copy = new long[index];
		initialize(r_copy);
		index=0;
					
		long [] external = ((MacSelector) s).ranges;
		int ex_size = external.length;
		int ac_size = ranges.length;
				
		boolean done=false;
		int pos=0, pos_a=0;
			
		while (!done){				
			if (external[pos+1] < ranges[pos_a]){
				if ( (pos+2)<ex_size && external[pos+2]!=-2)
					pos+=2;
				else done = true;
			} else if (external[pos] > ranges[pos_a+1]){
				if ((pos_a+2)<ac_size && ranges[pos_a+2]!=-2)
					pos_a+=2;
				else done=true;
			} else {
				long temp_s, temp_e;
				if (ranges[pos_a] > external[pos])
					temp_s = ranges[pos_a];
				else temp_s = external[pos];
				if (ranges[pos_a+1] > external[pos+1]){
					temp_e = external[pos+1];
					if ( (pos+2)<ex_size && external[pos+2]!=-2)
						pos+=2;
					else done = true;
				} else {
					temp_e = ranges[pos_a+1];
					if ((pos_a+2)<ac_size && ranges[pos_a+2]!=-2)
						pos_a+=2;
					else done=true;
						
				}
				r_copy[index]=temp_s;
				r_copy[++index]=temp_e;
				index++;	
			}
		} // fine while
			
		int i=2;
		boolean f=false;
		for (;i<r_copy.length && !f;i++)
			f = r_copy[i]==-2;
	
		if (f)
			ranges = new long[--i];
		else 
		ranges = new long[i];
	
		initialize(ranges);
		for (int j=0;j<i;j++){
			ranges[j]=r_copy[j];
			ranges[++j]=r_copy[j];
		}			 
	}
	
	public boolean isSubsetOrEquivalent(Selector s) throws IllegalArgumentException {		
		if (this.isEmpty())
			if (s.isEmpty())
				return true;
			else return false;
		
		if (s.isFull())
			return true;
		
		long []si = ((MacSelector) s).ranges;

		int j=0,i=0, size= ranges.length;
		boolean done;
		
		long st,e;
		
		while(i<size && ranges[i]!=-2){
			done =false;
			while (j < si.length && si[j]!=-2 && !done){
				st = si[j];
				e = si[j+1];
				if (st<=ranges[i] && e>=ranges[i+1] && ranges[i]!=-2)
					done=true;
				
				if(!done)
					j+=2;
			}
			if (!done)
				return false;
			i+=2;
			
		}
		
//		while (j < si.length && si[j]!=-2){
//			st = si[j];
//			e = si[++j];
//			done=false;
//			while (i<size && !done){
//				if (st<=ranges[i] && e>=ranges[i+1] && ranges[i]!=-2)
//					done=true;
//				i+=2;
//			}
//			j++;
//			if (!done)
//				return false;		
//		}
		return true;
	}
	
	public boolean isEmpty() {
		if (ranges==null)
			return true;
		if (ranges[0]==-2 && ranges[1]==-2)
			ranges=null;
		return ranges==null;
	}
	
	public boolean isIntersecting(Selector s) throws IllegalArgumentException {
		
		if (this.isEmpty() || s.isEmpty())
			return false;
		
		if (this.isFull() || s.isFull())
			return true;
		

		long [] external = ((MacSelector) s).ranges;
		
		int a_size = ranges.length;
		int ex_size = external.length;
		
		boolean done=false;
		int pos=0, pos_a=0;
		
		while (!done){
			if (external[pos+1] < ranges[pos_a]){
				if ((pos+2)<ex_size && external[pos+2]!=-2)
					pos+=2;
				else done = true;
			} else if (external[pos] > ranges[pos_a+1]){
				if ((pos_a+2)<a_size && ranges[pos_a+2]!=-2)
					pos_a+=2;
				else done=true;
			} else return true;
		}
		
		return false;
	}
	
	public void setMinus(Selector s) throws IllegalArgumentException {
		if(this.isEmpty() || s.isEmpty())
			return;
		
		if (s.isFull())
			this.empty();
		
		MacSelector s1 = (MacSelector) s.selectorClone();
		
//		if (!this.label.equalsIgnoreCase(s.getLabel()))
//			if(!s.getLabel().equals(""))
//				label.concat(" || "+s.getLabel());
//		
//		String nl = new String(label);
		
		s1.complement();
		intersection(s1);	
		
		//label = nl;
	}

	public boolean isEquivalent(Selector s) throws IllegalArgumentException{		
		if (this.isEmpty())
			if (s.isEmpty())
				return true;
			else return false;
		
		if (s.isEmpty())
			return false;
		else if (this.isFull() && s.isFull())
			return true;
		
		long [] temp = ((MacSelector) s).ranges;
		int size, t_size;
		size = ranges.length;
		t_size = temp.length;
		boolean eq=true;
		int i=0;
		for (;i<size && i<t_size && eq && temp[i]!=-2 && ranges[i]!=-2;i++)
			eq = (temp[i]==ranges[i] && temp[++i]==ranges[i]);
		
		if (i>=size && i>=t_size)
			return eq;
		else if (i<size && i>=t_size){
			if (ranges[i]==-2)
				return eq;
			else return false;
		} else if (i>=size && i<t_size){
			if (temp[i]==-2)
				return eq;
			else return false;
		}
		
		return eq;
			
	}

	public boolean isFull() {
		if (!this.isEmpty())
			return ranges[0]==min_l && ranges[1]==max_l;
		return false;
	}

	public Selector selectorClone() {
		MacSelector s = new MacSelector();
		if (!this.isEmpty()){
			int size = ranges.length;
			s.ranges = new long[size];
			for (int i=0;i<size;i++)
			s.ranges[i]=ranges[i];
		}
		
		s.setLabel(this.getLabel());
		return s;
	}

	public void union(Selector s) throws IllegalArgumentException {
		if (this.isFull())
			return;
		
		if (!s.isEmpty()){
			long [] temp = ((MacSelector) s).ranges;
			int t_size = temp.length;
			for (int i=0;i<t_size;i++)
				try {
					this.addRange(temp[i], temp[++i]);
				} catch (InvalidMacAddressException e) {
					throw new IllegalArgumentException();
				} catch (InvalidRangeException e) {
					throw new IllegalArgumentException();
				}	
			
			if (!this.label.equalsIgnoreCase(s.getLabel()))
				if(!s.getLabel().equals(""))
					label = label + " || "+s.getLabel();
		}
		
	}
	
	public String toString(){
        MacAddressManagement macam = MacAddressManagement.getInstance();
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		String str = "";
		int size = ranges.length;
		for (int i=0;i<size;i++){
			if (ranges[i]==-2 && ranges[i+1]==-2)
				break;
			try {
				str = str + "["+macam.getMacFromLong(ranges[i])+" - "+macam.getMacFromLong(ranges[++i])+"] ";
			} catch (InvalidMacAddressException e) {
				return "";
			}
		}
		return str;
	}
	
	public String toSquidString() {
	    MacAddressManagement macam = MacAddressManagement.getInstance();
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "any";
			
		StringBuffer sb = new StringBuffer();
		int size = ranges.length;
		for (int i=0;i<size;i++){
			if (ranges[i]==-2 && ranges[i+1]==-2)
				break;
			if (i>0)
				sb.append(" ");
			try {
				sb.append(macam.getMacFromLong(ranges[i]));
				sb.append("-");
				sb.append(macam.getMacFromLong(ranges[++i]));
			} catch (InvalidMacAddressException e) {
				return "";
			}
		}
		return sb.toString();
	}
	
	public String toSimpleString(){
	    MacAddressManagement macam = MacAddressManagement.getInstance();
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "any";
			
		StringBuffer sb = new StringBuffer();
		int size = ranges.length;
		for (int i=0;i<size;i++){
			if (ranges[i]==-2 && ranges[i+1]==-2)
				break;
			if (i>0)
				sb.append(";");
			try {
				sb.append(macam.getMacFromLong(ranges[i]));
				sb.append("-");
				sb.append(macam.getMacFromLong(ranges[++i]));
			} catch (InvalidMacAddressException e) {
				return "";
			}
		}
		return sb.toString();
	}

	public String getMax() {
		return max;
	}

	public String getMin() {
		return min;
	}

	public static long getMaxLong() {
		return max_l;
	}

	public static long getMinLong() {
		return min_l;
	}

	public boolean isSubset(Selector s) throws IllegalArgumentException {
		if (this.isEmpty() || s.isEmpty())
			return false;

		if (this.isFull())
			return false;
		
		if (s.isFull())
			return true;

		
		long []si = ((MacSelector) s).ranges;

		int j=0,i=0, size= ranges.length;
		boolean done,atLeastOne=false;
		
		long st,e;
		while (j < si.length && si[j]!=-2){
			st = si[j];
			e = si[++j];
			done=false;
			while (i<size && !done){
				if (st<=ranges[i] && ranges[i]!=-2) 
					if (e>=ranges[i+1] ){
						if (!(ranges[i]==st && ranges[i+1]==e))
							atLeastOne=true;
						done=true;
					}
				i+=2;
			}
			j++;
			if (!done)
				return false;		
		}
		return atLeastOne;
	}

	@Override
	public BigInteger[] getRanges()
	{
		//TODO: chiedere a google come si fa a creare una lista di oggetti da da tipi nativi
		BigInteger[] ret = new BigInteger[ranges.length];
		int i=0;
		for(long n : ranges)
			ret[i++]=BigInteger.valueOf(n);
		return	ret;

	}
	
	@Override
	public void addRange(Object Value) throws InvalidRangeException {
		if (Value instanceof java.lang.String)
			try {
				addRange((String) Value);
			} catch (InvalidMacAddressException e) {
				e.printStackTrace();
				throw new InvalidRangeException();
			}
		else throw new InvalidRangeException();
	}
	
	@Override
	public int getFirstAssignedValue() {
		if (ranges!=null)
			if (ranges[0]>0)
				return (int) ranges[0];
		
		return 0;
	}

	@Override
	public void full() {
		ranges = new long[2];
		ranges[0] = min_l;
		ranges[1] = max_l;
	}

	
}
