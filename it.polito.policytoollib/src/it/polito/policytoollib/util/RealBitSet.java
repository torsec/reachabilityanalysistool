package it.polito.policytoollib.util;

import java.util.BitSet;

@SuppressWarnings("serial")
public class RealBitSet extends BitSet implements Cloneable{
	
	private int realSize;

	public RealBitSet() {
	}

	public RealBitSet(int nbits) {
		super(nbits);
		realSize = nbits;
		
	}

	@Override
	public int size() {	
		return realSize;
	}

}
