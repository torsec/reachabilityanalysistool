package it.polito.policytoollib.test.classifier;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import it.polito.policytoollib.exception.policy.EmptySelectorException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.policy.utils.PointList;
import it.polito.policytoollib.rule.selector.impl.PortSelector;

public class PointListTest {

	@Test
	public void pointList() throws InvalidRangeException, EmptySelectorException, UnsupportedSelectorException{
		PortSelector ps = new PortSelector();
		ps.full();
		
		PortSelector ps1 = new PortSelector();
		ps1.addRange(20,60);
		
		PortSelector ps2 = new PortSelector();
		ps2.addRange(30,100);
		
		PortSelector ps3 = new PortSelector();
		ps3.addRange(80,150);
		
		PointList pl = new PointList(ps, "test");
		
		pl.insert(ps1, 1);
		pl.insert(ps2, 2);
		pl.insert(ps3, 3);
		
		Assert.assertEquals(8, pl.getPointList().size());
		Assert.assertEquals(7, pl.getEndPoints().size());
		
		
		Assert.assertTrue(pl.getPointList().get(0).getBs().isEmpty());
		Assert.assertTrue(pl.getPointList().get(1).getBs().isEmpty());
		Assert.assertTrue(pl.getPointList().get(2).getBs().get(1));
		Assert.assertTrue(pl.getPointList().get(3).getBs().get(1));
		Assert.assertTrue(pl.getPointList().get(3).getBs().get(2));
		Assert.assertTrue(pl.getPointList().get(4).getBs().get(2));
		Assert.assertTrue(pl.getPointList().get(5).getBs().get(2));
		Assert.assertTrue(pl.getPointList().get(5).getBs().get(3));
		Assert.assertTrue(pl.getPointList().get(6).getBs().get(3));
		Assert.assertTrue(pl.getPointList().get(7).getBs().isEmpty());
		
	}
}
