package it.polito.policytoollib.policy.utils;

import java.math.BigInteger;
import java.util.BitSet;

public class Point implements Block{
	
		private BigInteger val;
		private BitSet bs;
		boolean start;
		boolean end;

		
		public Point(BigInteger val)
		{
			this.val=val;
			bs = new BitSet();
			this.start = false;
			this.end = false;
		}
		
		public Point(long val)
		{
			this.val=BigInteger.valueOf(val);
			bs = new BitSet();
			this.start = false;
			this.end = false;
		}
		
		static Point createStartPoint(BigInteger val){	
			Point p =  new Point(val);
			p.start = true;
			return p;
		}
		static Point createEndPoint(BigInteger val){
			Point p =  new Point(val);
			p.end = true;
			return p;	
		}
			
		static Point createStartPoint(long val){	
			Point p =  new Point(BigInteger.valueOf(val));
			p.start = true;
			return p;
		}
		static Point createEndPoint(long val){
			Point p =  new Point(BigInteger.valueOf(val));
			p.end = true;
			return p;	
		}
		
		public boolean isStart()
		{
			return start;
		}

		public boolean isEnd() {
			return end;
		}

		
		/**
		 * @return the bs
		 */
		public BitSet getBs() {
			return bs;
		}
		/**
		 * @param bs the bs to set
		 */
		public void setBs(BitSet bs) {
			this.bs = bs;
		}
		/**
		 * @return the val
		 */
		public BigInteger getVal() {
			return val;
		}
		/**
		 * @param val the val to set
		 */
		
		@Override
		public String toString()
		{
			String s = ""+val+":"+bs;
			return s;
		}
		

		
//		public Point(int val, BitSet bs)
//		{
//			this.val=val;
//			this.bs = bs;
//		}
//		public void setVal(long val) {
//		this.val = val;
//	}
//		public void setStart() {
//			this.start = true;
//			this.end = false;
//		}
//
//
//		public void setEnd() {
//			this.start = false;
//			this.end = true;
//		}



	}
