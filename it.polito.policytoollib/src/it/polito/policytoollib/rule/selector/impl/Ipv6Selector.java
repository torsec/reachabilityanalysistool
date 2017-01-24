package it.polito.policytoollib.rule.selector.impl;

import it.polito.policytoollib.exception.rule.InvalidIpv6AddressException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.utils.Ipv6Address;

import java.math.BigInteger;
import java.util.StringTokenizer;

import com.googlecode.ipv6.IPv6Network;
import com.googlecode.ipv6.IPv6NetworkMask;


public class Ipv6Selector extends TotalOrderedSelectorImpl {

	private Ipv6Address [] ranges;
	private Ipv6Address [] r_copy;
	private static String min= Ipv6Address.minValue().toString();
	private static String max= Ipv6Address.maxValue().toString();
	private static Ipv6Address min_l = Ipv6Address.minValue();
	private static Ipv6Address max_l = Ipv6Address.maxValue();
	private static String selName="Ipv6 Address";
	
	protected String label=" ";

	public String getLabel() {
		return label;
	}

	public boolean isPoint(){
		if(ranges!=null){
			if(ranges[0].compareTo(ranges[1])==0)
				if(ranges[0].isValid())
					if(ranges.length>2){
						if(!ranges[3].isValid())
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
		BigInteger l = lengthbig();
		if (l.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) < 0) return l.longValue();
		else return -1;
	}

	public BigInteger lengthbig(){
		BigInteger sum = BigInteger.ZERO; boolean toggle = true;
		Ipv6Address prev = null;
		for(Ipv6Address r: ranges){
			if(toggle){
				prev = r;
				if(!prev.isValid()) break;
			}
			else{
				sum.add(r.toBigInteger().subtract(prev.toBigInteger().subtract(BigInteger.ONE)));
			}
			toggle=!toggle;
		}
		return sum;
	}
	
	private void initialize(Ipv6Address [] array){
		for (int i=0;i<array.length;i++)
			array[i] = new Ipv6Address();
	}

	/*
	 * 
	 */
	public void addRange(String Ip) throws InvalidRangeException, InvalidIpv6AddressException{
		StringTokenizer st = new StringTokenizer(Ip, "/");
		Ipv6Address s = new Ipv6Address(st.nextToken());
		if(st.countTokens()==1)
		{
			int net = Integer.parseInt(st.nextToken());
			IPv6Network network = IPv6Network.fromAddressAndMask(s.getAddress(), IPv6NetworkMask.fromPrefixLength(net));
			Ipv6Address d = new Ipv6Address(network.getLast());
			addRange(s,d); 
		}
		else addRange(s,s);
	}
	
	public void addRange(String IpStart, String IpEnd) throws InvalidRangeException, InvalidIpv6AddressException{
		Ipv6Address s = new Ipv6Address(IpStart);
		Ipv6Address d = new Ipv6Address(IpEnd);
		addRange(s,d);			
	}
	
	public void addRange(String IpStart, int net) throws InvalidIpv6AddressException, InvalidRangeException
	{
		Ipv6Address s = new Ipv6Address(IpStart);
		IPv6NetworkMask m = IPv6NetworkMask.fromPrefixLength(net); 
		IPv6Network n = IPv6Network.fromAddressAndMask(s.getAddress(), m);
		Ipv6Address d = new Ipv6Address(n.getLast());
		addRange(s,d);
	}
	
	public void addRange(Object Start, Object End) throws InvalidRangeException {
		if (Start instanceof java.lang.String || End instanceof java.lang.String)
			try {
				addRange((String) Start, (String) End);
			} catch (InvalidIpv6AddressException e) {
				throw new InvalidRangeException();
			}
		else throw new InvalidRangeException();
	}
	
	private void copy(){
		int j=0;
		for (int i=0;i<ranges.length;i+=2)
		{
			r_copy[j]=ranges[i].getCopy();
			r_copy[j+1]=ranges[i+1].getCopy();
			j+=2;
		}
	}
	
	public void addRange(Ipv6Address start, Ipv6Address end) throws InvalidRangeException {
		if (start.compareTo(end)>0) throw new InvalidRangeException();
		if (this.isEmpty()){
			ranges = new Ipv6Address [2];
			ranges[0]=start.getCopy();
			ranges[1]=end.getCopy();
		} else {
			int size = ranges.length;
			r_copy = new Ipv6Address [size];
			copy();
			
			if (size > 2 && !ranges[size-1].isValid())
				ranges = new Ipv6Address[size];
			else ranges = new Ipv6Address[size+2];
			initialize(ranges);
			
			int i=0, index=0;
			boolean done=false;
			
			while (i<size && !done){
				if (start.compareTo(r_copy[i])>=0 && end.compareTo(r_copy[i+1])<=0) {
					done=true;
				} else if (start.compareTo(r_copy[i])<=0 && end.compareTo(r_copy[i+1])>=0){
					i+=2;
				} else if (end.compareTo(r_copy[i])<0 && !(end.compareTo(r_copy[i].subtract(1))==0)){
					ranges[index]=start.getCopy();
					ranges[++index]=end.getCopy();
					index++;
					i+=2;
					done = true;					
				} else if ( (end.compareTo(r_copy[i])>=0 || (end.add(1)).compareTo(r_copy[i])==0) && start.compareTo(r_copy[i])<0) {
					ranges[index]=start.getCopy();
					ranges[++index]=r_copy[i+1].getCopy();
					i+=2;
					index++;
					done = true;
				} else if (start.compareTo(r_copy[i+1])<=0 || (start.subtract(1)).compareTo(r_copy[i+1])==0){
					start = r_copy[i].getCopy();
					i+=2;
				} else if (start.compareTo(r_copy[i+1])>0){
					if (!(i>0 && !r_copy[i].isValid())){
						ranges[index]=r_copy[i].getCopy();
						ranges[++index]=r_copy[i+1].getCopy();
						index++;
					}
					i+=2;
				}
			}
			if (done) {
				for (;i<size;i++){
					ranges[index]=r_copy[i].getCopy();
					ranges[++index]=r_copy[++i].getCopy();
					index++;
				}
			} else {
				ranges[index]=start.getCopy();
				ranges[++index]=end.getCopy();
			}
		}
	}
	
	public void complement() {
		if (this.isFull()){
			empty();
			return;
		}
		
		if (this.isEmpty()){
			try {
				this.addRange(min_l,max_l);
			} catch (InvalidRangeException e) {
				//blocco catch irraggiungibile
			}
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
			ranges=	 new Ipv6Address[2];
			ranges[0]=min_l;
			ranges[1]=max_l;
		} else {
			int size = ranges.length;
			r_copy = new Ipv6Address [size];
			copy();
			ranges = new Ipv6Address[size+2];
			initialize(ranges);
			int i=0, j=0;
			if(r_copy[0].compareTo(min_l)>0) ranges[0]=min_l;
			else
			{
				ranges[0]=(r_copy[1].getCopy()).add(1);
				i=2;
			}
			for(j=1;i<r_copy.length&&r_copy[i].isValid();j+=2,i+=2)
			{
				ranges[j]=(r_copy[i].getCopy()).subtract(1);
				if(r_copy[i+1].compareTo(max_l)==0)
					break;
				else
					ranges[j+1]=(r_copy[i+1].getCopy()).add(1);
			}
			if(!ranges[j].isValid() && ranges[j-1].isValid()) ranges[j]=max_l;
		}		
	}
	
	public void empty() {
		ranges=null;
	}
	
	@Override
	public void intersection(Selector s) throws IllegalArgumentException {	
		if(!(s instanceof Ipv6Selector))
			throw new IllegalArgumentException();
		
		if (!this.label.equalsIgnoreCase(s.getLabel()))
			if(!s.getLabel().equals(""))
				label = label + " || "+s.getLabel();
		
		if (isEmpty() || s.isFull())
			return;
		
		if (isFull()){
			ranges = ((Ipv6Selector)s).ranges.clone();
			return;
		}
		
		if (s.isEmpty()){
			empty();
			return;
		}
		
	
		int index = ranges.length + ((Ipv6Selector) s).ranges.length;
		r_copy = new Ipv6Address[index];
		initialize(r_copy);
		index=0;
					
		Ipv6Address [] external = ((Ipv6Selector) s).ranges;
		int ex_size = external.length;
		int ac_size = ranges.length;
				
		boolean done=false;
		int pos=0, pos_a=0;
			
		while (!done){				
			if (external[pos+1].compareTo(ranges[pos_a])<0){
				if ( (pos+2)<ex_size && external[pos+2].isValid())
					pos+=2;
				else done = true;
			} else if (external[pos].compareTo(ranges[pos_a+1])>0){
				if ((pos_a+2)<ac_size && ranges[pos_a+2].isValid())
					pos_a+=2;
				else done=true;
			} else {
				Ipv6Address temp_s, temp_e;
				if (ranges[pos_a].compareTo(external[pos])>0)
					temp_s = ranges[pos_a].getCopy();
				else temp_s = external[pos];
				if (ranges[pos_a+1].compareTo(external[pos+1])>0){
					temp_e = external[pos+1].getCopy();
					if ( (pos+2)<ex_size && external[pos+2].isValid())
						pos+=2;
					else done = true;
				} else {
					temp_e = ranges[pos_a+1].getCopy();
					if ((pos_a+2)<ac_size && ranges[pos_a+2].isValid())
						pos_a+=2;
					else done=true;
						
				}
				r_copy[index]=temp_s.getCopy();
				r_copy[++index]=temp_e.getCopy();
				index++;	
			}
		} // fine while
			
		int i=2;
		boolean f=false;
		for (;i<r_copy.length && !f;i++)
			f = r_copy[i].isValid();
	
		if (f)
			ranges = new Ipv6Address[--i];
		else 
		ranges = new Ipv6Address[i];
	
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
		
		Ipv6Address []si = ((Ipv6Selector) s).ranges;

		int j=0,i=0, size= ranges.length;
		boolean done;
		
		Ipv6Address st,e;
		
		while(i<size && ranges[i].isValid()){
			done =false;
			while (j < si.length && si[j].isValid() && !done){
				st = si[j].getCopy();
				e = si[j+1].getCopy();
				if (st.compareTo(ranges[i])<=0 && e.compareTo(ranges[i+1])>=0 && ranges[i].isValid())
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
		if (!ranges[0].isValid() && !ranges[1].isValid())
			ranges=null;
		return ranges==null;
	}
	
	public boolean isIntersecting(Selector s) throws IllegalArgumentException {
		
		if (this.isEmpty() || s.isEmpty())
			return false;
		
		if (this.isFull() || s.isFull())
			return true;
		

		Ipv6Address [] external = ((Ipv6Selector) s).ranges;
		
		int a_size = ranges.length;
		int ex_size = external.length;
		
		boolean done=false;
		int pos=0, pos_a=0;
		
		while (!done){
			if (external[pos+1].compareTo(ranges[pos_a])<0){
				if ((pos+2)<ex_size && external[pos+2].isValid())
					pos+=2;
				else done = true;
			} else if (external[pos].compareTo(ranges[pos_a+1])>0){
				if ((pos_a+2)<a_size && ranges[pos_a+2].isValid())
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
		
		Ipv6Selector s1 = (Ipv6Selector) s.selectorClone();
		
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
		
		Ipv6Address [] temp = ((Ipv6Selector) s).ranges;
		int size, t_size;
		size = ranges.length;
		t_size = temp.length;
		boolean eq=true;
		int i=0;
		for (;i<size && i<t_size && eq && temp[i].isValid() && ranges[i].isValid();i++)
			eq = (temp[i].compareTo(ranges[i])==0 && temp[++i].compareTo(ranges[i])==0);
		
		if (i>=size && i>=t_size)
			return eq;
		else if (i<size && i>=t_size){
			if (!ranges[i].isValid())
				return eq;
			else return false;
		} else if (i>=size && i<t_size){
			if (!temp[i].isValid())
				return eq;
			else return false;
		}
		
		return eq;
			
	}

	public boolean isFull() {
		if (!this.isEmpty())
			return ranges[0].compareTo(min_l)==0 && ranges[1].compareTo(max_l)==0;
		return false;
	}

	public Selector selectorClone() {
		Ipv6Selector s = new Ipv6Selector();
		if (!this.isEmpty()){
			int size = ranges.length;
			s.ranges = new Ipv6Address[size];
			for (int i=0;i<size;i++)
			s.ranges[i]=ranges[i].getCopy();
		}
		
		s.setLabel(this.getLabel());
		return s;
	}

	public void union(Selector s) throws IllegalArgumentException {
		if(!(s instanceof Ipv6Selector))
			throw new IllegalArgumentException();
		
		if (this.isFull())
			return;
		
		if (!s.isEmpty()){
			Ipv6Address [] temp = ((Ipv6Selector) s).ranges;
			int t_size = temp.length;
			for (int i=0;i<t_size;i++)
				try {
					this.addRange(temp[i], temp[++i]);
				} catch (InvalidRangeException e) {
					throw new IllegalArgumentException();
				}	
			
			if (!this.label.equalsIgnoreCase(s.getLabel()))
				if(!s.getLabel().equals(""))
					label = label + " || "+s.getLabel();
		}
		
	}
	
	public String toString(){
        if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		String str = "";
		int size = ranges.length;
		for (int i=0;i<size;i++){
			if (!ranges[i].isValid() && !ranges[i+1].isValid())
				break;
			str = str + "["+ranges[i].toString()+" - "+ranges[++i].toString()+"] ";
		}
		return str;
	}
	
	public String toSquidString() {
	    if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "any";
			
		StringBuffer sb = new StringBuffer();
		int size = ranges.length;
		for (int i=0;i<size;i++){
			if (!ranges[i].isValid() && !ranges[i+1].isValid())
				break;
			if (i>0)
				sb.append(" ");
			sb.append(ranges[i].toString());
			sb.append("-");
			sb.append(ranges[++i].toString());
		}
		return sb.toString();
	}
	
	public String toSimpleString() {
	    if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "any";
			
		StringBuffer sb = new StringBuffer();
		int size = ranges.length;
		for (int i=0;i<size;i++){
			if (!ranges[i].isValid() && !ranges[i+1].isValid())
				break;
			if (i>0)
				sb.append(";");
			sb.append(ranges[i].toString());
			sb.append("-");
			sb.append(ranges[++i].toString());
		}
		return sb.toString();
	}

	public String getMax() {
		return max;
	}

	public String getMin() {
		return min;
	}

	public static Ipv6Address getMaxLong() {
		return max_l;
	}

	public static Ipv6Address getMinLong() {
		return min_l;
	}

	public boolean isSubset(Selector s) throws IllegalArgumentException {
		if (this.isEmpty() || s.isEmpty())
			return false;

		if (this.isFull())
			return false;
		
		if (s.isFull())
			return true;

		
		Ipv6Address []si = ((Ipv6Selector) s).ranges;

		int j=0,i=0, size= ranges.length;
		boolean done,atLeastOne=false;
		
		Ipv6Address st,e;
		while (j < si.length && si[j].isValid()){
			st = si[j].getCopy();
			e = si[++j].getCopy();
			done=false;
			while (i<size && !done){
				if (st.compareTo(ranges[i])<=0 && ranges[i].isValid()) 
					if (e.compareTo(ranges[i+1])>=0 ){
						if (!(ranges[i].compareTo(st)==0 && ranges[i+1].compareTo(e)==0))
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
		//System.err.println("Funzione creata solo per implementare la funzione dell'interfaccia TotalOrderedSelector, usa getRangesIpv6Address");
		BigInteger[] ret = new BigInteger[ranges.length];
		int i=0;
		for(Ipv6Address n : ranges)
			if(n.isValid()) ret[i++]=n.getCopy().toBigInteger();
			else ret[i++] = BigInteger.valueOf(-2);
		return ret;
	}

	/*public Ipv6Address[] getRangesIpv6Address()
	{
		//TODO: chiedere a google come si fa a creare una lista di oggetti da da tipi nativi
		Ipv6Address[] ret = new Ipv6Address[ranges.length];
		int i=0;
		for(Ipv6Address n : ranges)
			ret[i++]=n.getCopy();
		return	ret;
	}*/
	
	@Override
	public void addRange(Object Value) throws InvalidRangeException {
		if (Value instanceof java.lang.String)
			try {
				addRange((String) Value);
			} catch (InvalidIpv6AddressException e) {
				throw new InvalidRangeException();
			}
		else throw new InvalidRangeException();
	}
	
	@Override
	public int getFirstAssignedValue() {
		return 0;
	}

	@Override
	public void full() {
		ranges = new Ipv6Address[2];
		ranges[0] = min_l;
		ranges[1] = max_l;
	}

	
}
