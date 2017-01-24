package it.polito.policytoollib.policy.utils;

import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.rule.selector.Selector;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

public interface BlockList {

	public boolean insert(Selector s, int index) throws UnsupportedSelectorException;
	public List<Block> getBlocks();
	public List<Selector> getBlocksAsSelectors();
	public HashMap<Selector, BitSet> getBlocksAndBitSets();
	public boolean atLeastOneLabel();
	public BitSet getBitSet(int index);
	
}
