package it.polito.policytoollib.rule.selector.impl;

import java.math.BigInteger;

import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidNetException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.utils.IpAddressManagement;


public class IpSelector extends TotalOrderedSelectorImpl {

	private long [] ranges;
	private long [] r_copy;
	private static String min="0.0.0.0";
	private static String max="255.255.255.255";
	private static long min_l = 0L;
	private static long max_l = 4294967295L;
	private static String selName="Ip Address";
	
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
	public void addRange(String Ip) throws InvalidIpAddressException, InvalidRangeException{
		IpAddressManagement ipam = IpAddressManagement.getInstance();
		//if (ipam.getNetNumber(Ip)!=-1)
			//throw new InvalidIpAddressException();
		
		try{
			long s = ipam.toLong(Ip);
			if (s<min_l || s>max_l)
				throw new InvalidRangeException();
			addRange(s,s);
		}
		catch(NumberFormatException e){
			throw new InvalidIpAddressException();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.TotalOrderedSelector#addRange(java.lang.Object, java.lang.Object)
	 */
	public void addRange(String IpStart, String IpEnd) throws InvalidIpAddressException, InvalidRangeException{
		IpAddressManagement ipam = IpAddressManagement.getInstance();
		
		int net = ipam.getNetNumber(IpEnd);
		if (net==-1){
            int n;
            try {
                n = Integer.parseInt(IpEnd);
                addRange(IpStart,n);
                return;
            } catch (NumberFormatException e){
            } catch (InvalidNetException ex){
                throw new InvalidIpAddressException();
            }
            try {
            	long s = ipam.toLong(IpStart);
    			long e = ipam.toLong(IpEnd);
    			if (s<min_l || e>max_l || s>e)
    				throw new InvalidRangeException();
    			addRange(s,e);
            } catch (NumberFormatException e){
            	throw new InvalidIpAddressException();
            }
		} else {
			long [] r = ipam.parseNet(IpStart, net);
			if (r[0]<min_l || r[1]>max_l || r[0]>r[1])
				throw new InvalidRangeException();
			addRange(r[0],r[1]);
		}
			
	}
	

	public void addRange(String IpStart, int net) throws InvalidNetException, InvalidRangeException {
		if (net<0 || net>32)
			throw new InvalidNetException();
		
		IpAddressManagement ipam = IpAddressManagement.getInstance();
		long[] r;
		try {
			r = ipam.parseNet(IpStart, net);
		} catch (Exception e) {
			throw new InvalidRangeException();
		}
		if (r[0]<min_l || r[1]>max_l || r[0]>r[1])
			throw new InvalidRangeException();
		addRange(r[0],r[1]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.polito.ruleManagement.selector.TotalOrderedSelector#addRange(java.lang.Object, java.lang.Object)
	 */
	public void addRange(Object Start, Object End) throws InvalidRangeException {
		if (Start instanceof java.lang.String || End instanceof java.lang.String)
			try {
				addRange((String) Start, (String) End);
			} catch (InvalidIpAddressException e) {
				throw new InvalidRangeException();
			}
		else throw new InvalidRangeException();
		
	}
	
	private void copy(){
		for (int i=0;i<ranges.length;i++)
			r_copy[i]=ranges[i];
	}
	
	public void addRange(long start, long end) {
		if (this.isEmpty()){
			ranges = new long [2];
			ranges[0]=start;
			ranges[1]=end;
		} else {
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
				if(ranges[1]<r_copy[0])
					i=0;
				for (;i<size&&index<ranges.length;i++){
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
			this.addRange(min_l,max_l);
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
		if(!(s instanceof IpSelector))
			throw new IllegalArgumentException();
		
		if (!this.label.equalsIgnoreCase(s.getLabel()))
			if(!s.getLabel().equals(""))
				label = label + " || "+s.getLabel();
		
		if (isEmpty() || s.isFull())
			return;
		
		if (isFull()){
			ranges = ((IpSelector)s).ranges.clone();
			return;
		}
		
		if (s.isEmpty()){
			empty();
			return;
		}
		
	
		int index = ranges.length + ((IpSelector) s).ranges.length;
		r_copy = new long[index];
		initialize(r_copy);
		index=0;
					
		long [] external = ((IpSelector) s).ranges;
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
		
		long []si = ((IpSelector) s).ranges;

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
		

		long [] external = ((IpSelector) s).ranges;
		
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
		
		IpSelector s1 = (IpSelector) s.selectorClone();
		
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
		
		long [] temp = ((IpSelector) s).ranges;
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
		IpSelector s = new IpSelector();
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
			long [] temp = ((IpSelector) s).ranges;
			int t_size = temp.length;
			for (int i=0;i<t_size;i++)
				this.addRange(temp[i], temp[++i]);	
			
			if (!this.label.equalsIgnoreCase(s.getLabel()))
				if(!s.getLabel().equals(""))
					label = label + " || "+s.getLabel();
		}
		
	}
	
	public String toString(){
        IpAddressManagement ipam = IpAddressManagement.getInstance();
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		String str = "";
		int size = ranges.length;
		for (int i=0;i<size;i++){
			if (ranges[i]==-2 && ranges[i+1]==-2)
				break;
			str = str + "["+ipam.getIpFromLong(ranges[i])+" - "+ipam.getIpFromLong(ranges[++i])+"] ";
		}
		return str;
	}
	
	public String toSquidString() {
	    IpAddressManagement ipam = IpAddressManagement.getInstance();
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
			sb.append(ipam.getIpFromLong(ranges[i]));
			sb.append("-");
			sb.append(ipam.getIpFromLong(ranges[++i]));
		}
		return sb.toString();
	}
	
	public String toSimpleString() {
	    IpAddressManagement ipam = IpAddressManagement.getInstance();
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
			sb.append(ipam.getIpFromLong(ranges[i]));
			sb.append("-");
			sb.append(ipam.getIpFromLong(ranges[++i]));
		}
		return sb.toString();
	}
	
	public String toSingleIpString() {
		IpAddressManagement ipam = IpAddressManagement.getInstance();
		if(this.isEmpty())
			return "";
		for(long ip : ranges)
			if(ip>-1)
				return ipam.getIpFromLong(ip);
		return "";
	}
	
	public String toSubnetString() {
		IpAddressManagement ipam = IpAddressManagement.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(ipam.getIpFromLong(ranges[0]));
		sb.append("/");
		long a = IpSelector.max_l-(ranges[1]-ranges[0]);
		sb.append(32-Long.numberOfTrailingZeros(a));
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

		
		long []si = ((IpSelector) s).ranges;

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
			} catch (InvalidIpAddressException e) {
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
