package it.polito.policytoollib.rule.utils;

import it.polito.policytoollib.exception.rule.InvalidIpv6AddressException;

import java.math.BigInteger;
import java.util.StringTokenizer;

import com.googlecode.ipv6.IPv6Address;

public class Ipv6Address {

	IPv6Address address;
	boolean valid;
	
	public Ipv6Address(IPv6Address address, boolean valid) {
		this.address = address;
		this.valid = valid;
	}
	
	public Ipv6Address(IPv6Address address) {
		this.address = address;
		valid=true;
	}
	
	public Ipv6Address() {
		address = IPv6Address.fromLongs(0, 0);
		valid=false;
	}

	public Ipv6Address(String address) throws InvalidIpv6AddressException {
		if(address!=null)
		{
			if(!address.contains(":")) throw new InvalidIpv6AddressException();
			if(address.contains("."))
			{
				String[] IPv6parts = address.split(":");
				String IPv4part = IPv6parts[IPv6parts.length-1];
				StringTokenizer st = new StringTokenizer(IPv4part,".");
				while(st.hasMoreTokens())
					if(Long.parseLong(st.nextToken())<0 || Long.parseLong(st.nextToken())>255) throw new InvalidIpv6AddressException();
			}
		}
		try{ this.address = IPv6Address.fromString(address); }
		catch(NumberFormatException e){ throw new InvalidIpv6AddressException();}
		catch(IllegalArgumentException e){ throw new InvalidIpv6AddressException();}
		valid=true;
	}

	public IPv6Address getAddress() {
		return address;
	}

	public void setAddress(IPv6Address address) {
		this.address = address;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Ipv6Address add(int value) {
		return new Ipv6Address(address.add(value),valid);
	}

	public Ipv6Address subtract(int value) {
		return new Ipv6Address(address.subtract(value),valid);
	}
	
	public int compareTo(IPv6Address that) {
		return address.compareTo(that);
	}
	public BigInteger toBigInteger() {
		return address.toBigInteger();
	}
	public String toString() {
		return address.toString();
	}

	public static Ipv6Address minValue() {
		return new Ipv6Address(IPv6Address.fromLongs(0, 0));
	}
	
	public static Ipv6Address maxValue() {
		return new Ipv6Address(IPv6Address.MAX);
	}

	public int compareTo(Ipv6Address ipv6Address) {
		return address.compareTo(ipv6Address.getAddress());
	}

	public Ipv6Address getCopy() {
		return new Ipv6Address(IPv6Address.fromString(this.address.toString()), this.valid);
	}

	
}
