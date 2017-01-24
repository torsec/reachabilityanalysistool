package it.polito.policytoollib.rule.selector.impl;

import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.selector.Selector;

import java.math.BigInteger;


public class GenericTotalOrderedSelector extends TotalOrderedSelectorImpl {

	private int [] ranges;
	private int [] r_copy;
	private Integer minValue;
	private Integer maxValue;
	private static String selName="Generic Total Ordered Selector";
	
	public GenericTotalOrderedSelector(int minValue, int maxValue)
	{
		this.minValue=minValue;
		this.maxValue=maxValue;
	}
	
	public GenericTotalOrderedSelector()
	{
		this.minValue=0;
		this.maxValue=Integer.MAX_VALUE;
	}
	
	protected String label="";

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
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String getName() {
		return selName;
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
	
	public long length(){
		long sum =0;boolean toggle = true;
		long prev=0;
		for(int r: ranges){
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
	
	@Override
	public void addRange(Object object) throws InvalidRangeException{
		if (object instanceof java.lang.Integer)
			addRange(((Integer) object).intValue());
		else if (object instanceof java.lang.String)
			addRange(Integer.parseInt((String) object));
		else throw new InvalidRangeException();
	}
	
	@Override
	public void addRange(Object start, Object end) throws InvalidRangeException {
		if (start instanceof java.lang.Integer && end instanceof java.lang.Integer)
			addRange(((Integer) start).intValue(), ((Integer) end).intValue());
		else if (start instanceof java.lang.Long && end instanceof java.lang.Long)
			addRange(((Long) start).intValue(), ((Long) end).intValue());
		else if (start instanceof java.lang.String && end instanceof java.lang.String)
			addRange(Integer.parseInt((String) start), Integer.parseInt((String) end));
		else throw new InvalidRangeException();
	}
	
	private void inizialize(int [] array){
		for (int i=0;i<array.length;i++)
			array[i]=-2;
	}
	private void copy(){
		for (int i=0;i<ranges.length;i++)
			r_copy[i]=ranges[i];
	}
	
	public void addRange(int point) throws InvalidRangeException{
		addRange(point,point);
	}
	
	public void addRange(int start, int end) throws InvalidRangeException {
		if (start<minValue || end>maxValue || start>end)
			throw new InvalidRangeException();
		
		if (this.isEmpty()){
			ranges = new int [2];
			ranges[0]=start;
			ranges[1]=end;
		} else {
			int size = ranges.length;
			r_copy = new int [size];
			copy();
			
			if (size > 2 && ranges[size-1]==-2)
				ranges = new int[size];
			else ranges = new int[size+4];
				
			inizialize(ranges);
			
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
					//i+=2;
					index++;
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
				for (;i<r_copy.length && !(r_copy[i]==-2);i++){
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
		if (this.isEmpty()) {
			ranges=	 new int[2];
			ranges[0]=minValue;
			ranges[1]=maxValue;
		} else {
			int size = ranges.length;
			r_copy = new int [size];
			copy();
			if (size > 2 && ranges[size-1]==-2)
				ranges = new int[size];
			else ranges = new int[size+2];
			inizialize(ranges);
			int index=0;
			if (r_copy[0]!=-2){
				ranges[0]=0;
				ranges[1]= r_copy[0]-1;
				index+=2;
			}
			int i=1;
			for (;i<size-1;i++){
				ranges[index]=r_copy[i]+1;
				if (r_copy[++i]!=-2)
					ranges[++index]=r_copy[i]-1;
				else {
					ranges[++index]= maxValue;
					r_copy[i]=maxValue;
					break;
				}
				index++;
			}
			if (r_copy[i]<maxValue){
				ranges[index]= r_copy[i]+1;
				ranges[++index]= maxValue;
			}
		}
	}
	
	public void empty() {
			ranges=null;
	}
	
	public void intersection(Selector s) throws IllegalArgumentException {
		
		if (((GenericTotalOrderedSelector) s).isEmpty()){
			this.empty();
			return;
		} else if (this.isEmpty())
			return;
		else {
			int index = ranges.length + ((GenericTotalOrderedSelector) s).ranges.length;
			r_copy = new int[index];
			inizialize(r_copy);
			index=0;
					
			int [] external = ((GenericTotalOrderedSelector) s).ranges;
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
					int temp_s, temp_e;
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
			} 
			int i=2;
			boolean f=false;
			for (;i<r_copy.length && !f;i++)
				f = r_copy[i]==-2;
			if (f)
				ranges = new int[--i];
			else 
				ranges = new int[i];
			inizialize(ranges);
			for (int j=0;j<i;j++){
				ranges[j]=r_copy[j];
				ranges[++j]=r_copy[j];
			}			
		} 
	}
	
	public boolean isSubsetOrEquivalent(Selector s) throws IllegalArgumentException {
		if (this.isEmpty() || s.isEmpty())
			return false;
		if (s.isFull())
			return true;
		
		int []si = ((GenericTotalOrderedSelector) s).ranges;

		int j=0,i=0, size= ranges.length;
		boolean done;
		
		int st,e;
		
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
		if (this.isEmpty())
			if (s.isEmpty())
				return true;
			else return false;
		else if (s.isEmpty())
			return false;

		int [] external = ((GenericTotalOrderedSelector) s).ranges;
		
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
		
		GenericTotalOrderedSelector s1 = (GenericTotalOrderedSelector) s.selectorClone();
		s1.complement();
		intersection(s1);	
	}

	public boolean isEquivalent(Selector s) throws IllegalArgumentException{
		if (this.isEmpty() )
			if (s.isEmpty())
				return true;
			else return false;
		
		if (s.isEmpty())
			return false;
		
		if (this.isFull() && s.isFull())
			return true;
		
		int [] temp = ((GenericTotalOrderedSelector) s).ranges;
		int size, t_size;
		size = ranges.length;
		t_size = temp.length;
		boolean eq=true;
		int i=0;
		for (;i<size && i<t_size && eq && temp[i]!=-2 && ranges[i]!=-2 ;i++)
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
			return ranges[0]==minValue && ranges[1]==maxValue;
		return false;
	}


	public void union(Selector s) throws IllegalArgumentException {
		if (!s.isEmpty()){
			int [] temp = ((GenericTotalOrderedSelector) s).ranges;
			int t_size = temp.length;
			for (int i=0;i<t_size;i++)
				try {
					if (temp[i]==-2)
						return;
					this.addRange(temp[i], temp[++i]);
				} catch (InvalidRangeException e) {
					e.printStackTrace();
				}	
		}
	}
	
	public boolean isSubset(Selector s) throws IllegalArgumentException {
		if (this.isEmpty() || s.isEmpty())
			return false;
		if (s.isFull())
			return true;
		
		int []si = ((GenericTotalOrderedSelector) s).ranges;

		int j=0,i=0, size= ranges.length;
		boolean done,atLeastOne=false;
		
		int st,e;
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

	public int getMaxPort() {
		return maxValue;
	}

	public int getMinPort() {
		return minValue;
	}
	
	public Selector selectorClone() {
		GenericTotalOrderedSelector s = new GenericTotalOrderedSelector();
		if (!this.isEmpty()){
			int size = ranges.length;
			s.ranges = new int[size];
			for (int i=0;i<size;i++)
				s.ranges[i]=ranges[i];
		}
		s.setLabel(this.label);
		return s;
	}
	
	public String toString(){
		if (this.isEmpty())
			return "empty";
		if (this.isFull())
			return "full";
		
		String str = "";
		int size = ranges.length;
		for (int i=0;i<size;i++){
			if (ranges[i]==-2 && ranges[i+1]==-2)
				break;
			str = str + "["+ranges[i]+" - "+ranges[++i]+"] ";
		}
		return str;
	}
	
	public String toSimpleString() {
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
			sb.append(ranges[i]);
			sb.append("-");
			sb.append(ranges[++i]);
		}
		return sb.toString();
	}
	
	public String toSquidString() {
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
			sb.append(ranges[i]);
			sb.append("-");
			sb.append(ranges[++i]);
		}
		return sb.toString();
	}



	


	@Override
	public int getFirstAssignedValue() {
		if (ranges!=null)
			if (ranges[0]>0)
				return ranges[0];
		
		return 0;
	}

	@Override
	public void full() {
		ranges = new int[2];
		ranges[0] = minValue;
		ranges[1] = maxValue;
	}

	@Override
	public String getMin() {
		return minValue.toString();
	}

	@Override
	public String getMax() {
		return maxValue.toString();
	}

}
