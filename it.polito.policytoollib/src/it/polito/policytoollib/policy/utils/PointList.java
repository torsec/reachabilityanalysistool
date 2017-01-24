
package it.polito.policytoollib.policy.utils;

import it.polito.policytoollib.exception.policy.EmptySelectorException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.TotalOrderedSelector;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


//TODO: dovrebbe essere reimplementata come per i regex in cui ogni blocco contiene sia selettore che bs

public class PointList implements BlockList
{
	private List<Block> list;
	private List<Block> end_points;
	private Selector sel;
	private String sel_label;
	private BitSet indexBS;
	
	public List<Block> getPointList(){
		return Collections.unmodifiableList(list);
	}
	public List<Block> getEndPoints(){
		return Collections.unmodifiableList(end_points);
	}
	public List<Block> getBlocks(){
		return Collections.unmodifiableList(end_points);
	}

	/**
	 * @return the sel
	 */
	public Selector getBaseSel() {
		return sel;
	}
	
	public String getSelectorLabel(){
		return sel_label;
	}
	
	
	public int getIntervalNumber(){
		return list.size();
	}
	
	public PointList(Selector s, String label) throws EmptySelectorException, UnsupportedSelectorException
	{
		
		
		if(s.isEmpty()) 
			throw new EmptySelectorException();
			
		this.sel = s;
		this.sel_label = label;
		list = new LinkedList<Block>();
		end_points = new LinkedList<Block>();
		boolean toggle = true;
				
		if (s instanceof ExactMatchSelector) {
			BitSet bb = ((ExactMatchSelector)s).getPointSet();
			int prev;
			for(int i= bb.nextSetBit(0); i >= 0; i = bb.nextSetBit(i+1)){
				list.add(Point.createStartPoint(i-1));
				prev = i;
				while(bb.nextSetBit(i+1) == prev+1){
					prev++; i++;
				}
				Point end = Point.createEndPoint(prev);
				list.add(end);
				end_points.add(end);
			}
		}
		else if (s instanceof TotalOrderedSelector){
			for(BigInteger n: ((TotalOrderedSelector)s).getRanges()){
				if(n.compareTo(BigInteger.ZERO)<0) continue; //PEZZA A COLORE ANTI BUG DEI -1
				if(toggle)
					list.add(Point.createStartPoint(n.subtract(BigInteger.ONE)));
				else{
					Point end = Point.createEndPoint(n);
					list.add(end);
					end_points.add(end);
				}
				toggle = !toggle;
			}
		}
		else throw new UnsupportedSelectorException(s.getClass() + "not supported");
		
		indexBS = new BitSet();
	}
	
	
	public boolean atLeastOneLabel(){
		 if(end_points.isEmpty()) return false;
		 for(Block p: end_points)
			 if(p.getBs().isEmpty()) return false;
		 return true;
		}

	public boolean allPointsHaveAllLabels(){
		 if(end_points.isEmpty()) return false;

		 for(Block p: end_points){
			 BitSet clone = (BitSet)indexBS.clone();
			 clone.andNot(p.getBs());
			 if(!clone.isEmpty())
				 return false;
		}
		 return true;
	}

	
	
	private void insertRange(BigInteger start, BigInteger end, int index){
		
		boolean start_inserted = false;

		Point ps = new Point(start.subtract(BigInteger.ONE));
		//ps.start = true;
		Point pe = new Point(end);
		pe.getBs().set(index);
		//pe.end = true;
		
		ListIterator<Block> it = list.listIterator(); 
		
		
		while(it.hasNext()){
			Point p = (Point) it.next();
			
			if(!start_inserted){	
				if(p.getVal() == ps.getVal()){
					start_inserted = true;
				}
				
				if(p.getVal().compareTo(ps.getVal())>0){
					ps.setBs((BitSet) p.getBs().clone());
					it.previous();
					it.add(ps);
					start_inserted = true;
					//count++;
					end_points.add(ps);
				}
			}
			else{
				if(p.getVal().compareTo(pe.getVal())<0){
					p.getBs().set(index);
				}
				else if(p.getVal() == pe.getVal()){
					p.getBs().set(index);
					//TODO: check che non ci siano ripetizioni in end_points
					//end_points.add(pe);
					break;
				}
				else{
					pe.getBs().or(p.getBs());
					it.previous();
					it.add(pe);
					end_points.add(pe);
					//count++;
					break;
				}
			}
		}
		
	}

	
	public boolean insert(Selector s, int index) throws UnsupportedSelectorException
	{
		if(s == null){
			for(Block b:end_points){
				b.getBs().set(index);
			}
			return true;
		}
		
		if(!sel.isIntersecting(s)) 
			return false;
		
		indexBS.set(index);
		
		Selector temp = s.selectorClone();
		temp.intersection(sel);
		
		
		boolean toggle = false;
		BigInteger n_old = BigInteger.ZERO;
		if(s instanceof TotalOrderedSelector){
			for(BigInteger n: ((TotalOrderedSelector)temp).getRanges()){
				if(toggle)
					insertRange(n_old, n, index);
				else
					n_old = n;
				toggle = !toggle;
			}
		}
		else if(s instanceof ExactMatchSelector){
			BitSet bb = ((ExactMatchSelector)temp).getPointSet();
			int prev;
			int start;
			for(int i= bb.nextSetBit(0); i >= 0; i = bb.nextSetBit(i+1)){
				start = i;
				prev = i;
				while(bb.nextSetBit(i+1) == prev+1){
					prev++; i++;
				}
				insertRange(BigInteger.valueOf(start), BigInteger.valueOf(prev), index);
			}
		}
		else throw new UnsupportedSelectorException(s.getClass() + "not supported");
		
		return true;
	}	
	
	
	public List<Selector> getBlocksAsSelectors()
	{

		List<Selector> selectors = new LinkedList<Selector>();
		
		Point previous=null;
		
		@SuppressWarnings("unused")
		boolean first = true;
		
		for(Block b: list){
			Point p = (Point) b;
			if(p.isStart()){
				previous = p;
				first = false;
			}				
			else if(p.isEnd()){
				Selector sel = generateCondition(previous, p);
				selectors.add(sel);
//				System.out.println("Inserito "+ sel);
			}
			else{
					Selector sel = generateCondition(previous, p);
					selectors.add(sel);
//					System.out.println("Inserito "+ sel);
					previous = p;
//				}
			}

		}
		
		
		return selectors;
	}
	
	public HashMap<Selector, BitSet> getBlocksAndBitSets()
	{

		HashMap<Selector, BitSet> selectors = new HashMap<Selector, BitSet>();
		
		Point previous=null;
		
		@SuppressWarnings("unused")
		boolean first = true;

		for(Block b: list){
			Point p = (Point) b;
			if(p.isStart()){
				previous = p;
				first = false;
			}				
			else if(p.isEnd()){
				Selector sel = generateCondition(previous, p);
				selectors.put(sel,p.getBs());
				System.out.println("Inserito "+ sel);
			}
			else{
				Selector sel = generateCondition(previous, p);
				selectors.put(sel,p.getBs());
				System.out.println("Inserito "+ sel);
				previous = p;
//				}
			}

		}
		
		
		return selectors;
	}	
	
	private Selector generateCondition(Point previous, Point end){
		
		Selector selector = sel.selectorClone();
		selector.empty();
		
		if(sel instanceof TotalOrderedSelector){
			try {
				((TotalOrderedSelector) selector).addRange(previous.getVal().add(BigInteger.ONE), end.getVal());
			} catch (InvalidRangeException e) {
				e.printStackTrace();
			}
		}
		else if(sel instanceof ExactMatchSelector){
			

			for(long j = previous.getVal().longValue()+1; j <= end.getVal().longValue(); j++)
				try {
					((ExactMatchSelector) selector).addRange(j);
				} catch (InvalidRangeException e) {
					e.printStackTrace();
				}

		}
		
		return selector;
	}
	
	
	@Override
	public String toString()
	{
		String s = "";
		for(Block pb: list)
		{
			Point p = (Point) pb;
			if(p.isStart()){
				s = s + "---------\n["+(p.getVal().add(BigInteger.ONE));
				System.out.println("punto start "+p.getVal());
			}
			else if(p.isEnd()){
				s+= ","+p.getVal()+"]"+p.getBs()+"\n";
				System.out.println("punto end "+p.getVal());
			}
			else{
				s+= ","+ p.getVal()+"]"+p.getBs()+
					"\n["+(p.getVal().add(BigInteger.ONE));
				System.out.println("punto strano "+p.getVal());
			}
		}
		return s;
	}
	
	@Override
	public BitSet getBitSet(int index) {
		BitSet bitSet = new BitSet();
		
		for(Block b:end_points){
			if(b.getBs().get(index)){
				bitSet.or(b.getBs());
			}
		}
		
		return bitSet;
	}

//	public Selector deriveSelectors(int i) throws SecurityException, NoSuchMethodException, InvalidRangeException{
//	
//	//Selector sel = pl.getBaseSel();
//	Selector selec = sel.getFactory().createEmptySelector();
//	
//	if (sel instanceof TotalOrderedSelector) {
//		TotalOrderedSelector ordsel = (TotalOrderedSelector) selec;
//		long prev=-1;
//		for(Block pb: list){
//			Point p = (Point) pb;
//			if(p.getBs().get(i)){
//				ordsel.addRange(prev+1, p.getVal());
//			}
//			prev = p.getVal();
//		}
//	}
//	else if (sel instanceof ExactMatchSelector) {
//		ExactMatchSelector sbsel = (ExactMatchSelector) selec;
//		long prev=-1;
//		System.out.println(this);
//		for(Block pb: list){
//			Point p = (Point) pb;
//			if(p.getBs().get(i)){
//				for(int j = (int)(prev+1); j <= p.getVal(); j++)
//					try {
//						sbsel.addRange(j);
//					} catch (InvalidRangeException e) {
//						e.printStackTrace();
//					}
//			}
//			prev = p.getVal();
//		}
//
//	}
//	return selec;
//}
//
//public Selector[] deriveAllSelectors() throws InvalidRangeException{
////	Selector sel = pl.getBaseSel();
//	int i, Nrules = 10; //TODO: valore fisso
//	Selector[] selectors = new Selector[Nrules];
//	
//	
//	for(i = 0; i < Nrules; i++)
//		selectors[i] = sel.getFactory().createEmptySelector();
//	
//	
//	long prev = -1;
//	for(Block pb: list){
//		Point p = (Point) pb;
//		for (i = p.getBs().nextSetBit(0); i >= 0; i = p.getBs().nextSetBit(i+1)) {
//			if (sel instanceof TotalOrderedSelector) {
//				TotalOrderedSelector ordsel = (TotalOrderedSelector) selectors[i-1];
//				ordsel.addRange(prev+1, p.getVal());
//			}
//			else if (sel instanceof ExactMatchSelector) {
//				ExactMatchSelector sbsel = (ExactMatchSelector) selectors[i-1];
//				for(int j = (int)(prev+1); j <= p.getVal(); j++)
//					try {
//						sbsel.addRange(j);
//					} catch (InvalidRangeException e) {
//						e.printStackTrace();
//					}
//	
//			}
//		     
//		 }
//		prev = p.getVal();
//	}
//	
//	return selectors;
//	
//}


	
}