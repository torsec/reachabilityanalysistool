package it.polito.policytoollib.test.logger;

import static org.junit.Assert.*;

import junit.framework.Assert;

import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.test.selector.IPSelectorTest;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.handler.LogicalHandler;

import org.junit.Test;

public class LoggerTest {
	
	private final Logger LOGGER = Logger.getLogger(LoggerTest.class.getName());
	
	public LoggerTest(){
		for(Handler handler:LOGGER.getHandlers()){
			LOGGER.removeHandler(handler);
		}
	}
	
	@Test
	public void setLevelSevere() throws InvalidIpAddressException, InvalidRangeException{	
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelSevere");
		System.out.println("------------------------------------------------------------------------------------");	
		LOGGER.setLevel(Level.SEVERE);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}
	
	@Test
	public void setLevelWarning() throws InvalidIpAddressException, InvalidRangeException{	
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelWarning");
		System.out.println("------------------------------------------------------------------------------------");	
		LOGGER.setLevel(Level.WARNING);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}
	
	@Test
	public void setLevelInfo() throws InvalidIpAddressException, InvalidRangeException{	
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelInfo");
		System.out.println("------------------------------------------------------------------------------------");	
		LOGGER.setLevel(Level.INFO);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}

	@Test
	public void setLevelConfig() throws InvalidIpAddressException, InvalidRangeException{	
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelConfig");
		System.out.println("------------------------------------------------------------------------------------");	
		LOGGER.setLevel(Level.CONFIG);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}
	
	@Test
	public void setLevelFine() throws InvalidIpAddressException, InvalidRangeException{	
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelFine");
		System.out.println("------------------------------------------------------------------------------------");	
		LOGGER.setLevel(Level.FINE);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}
	
	@Test
	public void setLevelFiner() throws InvalidIpAddressException, InvalidRangeException{	
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelFiner");
		System.out.println("------------------------------------------------------------------------------------");	
		LOGGER.setLevel(Level.FINER);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}
	
	@Test
	public void setLevelFinest() throws InvalidIpAddressException, InvalidRangeException{	
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelFinest");
		System.out.println("------------------------------------------------------------------------------------");	
		LOGGER.setLevel(Level.FINEST);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}
	
	@Test
	public void setLevelAll() throws InvalidIpAddressException, InvalidRangeException{	
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelAll");
		System.out.println("------------------------------------------------------------------------------------");	
		LOGGER.setLevel(Level.ALL);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}
	
	@Test
	public void setLevelOff() throws InvalidIpAddressException, InvalidRangeException{
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("setLevelOff");
		System.out.println("------------------------------------------------------------------------------------");
		LOGGER.setLevel(Level.OFF);
		LOGGER.severe("Info Log SEVERE");
	    LOGGER.warning("Info Log WARNING");
	    LOGGER.info("Info Log INFO");
	    LOGGER.config("Info Log CONFIG");
	    LOGGER.fine("Info Log FINE");
	    LOGGER.finer("Info Log FINER");
	    LOGGER.finest("Really not important FINEST");
	}
}
