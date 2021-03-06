package it.polito.policytoollib.rule.utils;

import it.polito.policytoollib.exception.rule.InvalidIpAddressException;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class IpAddressManagement {

	private static IpAddressManagement instance=null;
	private IpAddressManagement(){}
	
	private static String [] ipnetToNum = {	"0.0.0.0", 
		"128.0.0.0", "192.0.0.0", "224.0.0.0", "240.0.0.0","248.0.0.0", "252.0.0.0", "254.0.0.0", "255.0.0.0", 
		"255.128.0.0","255.192.0.0", "255.224.0.0", "255.240.0.0", "255.248.0.0", "255.252.0.0","255.254.0.0", "255.255.0.0", 
		"255.255.128.0", "255.255.192.0", "255.255.224.0", "255.255.240.0", "255.255.248.0", "255.255.252.0", "255.255.254.0", "255.255.255.0", 
		"255.255.255.128", "255.255.255.192", "255.255.255.224", "255.255.255.240", "255.255.255.248", "255.255.255.252", "255.255.255.254", "255.255.255.255"};

	private static String [] orNet = {	"0.0.0.0", "0.0.0.1", "0.0.0.3", "0.0.0.7", "0.0.0.15", "0.0.0.31",
		"0.0.0.63", "0.0.0.127", "0.0.0.255", "0.0.1.255", "0.0.3.255",
		"0.0.7.255", "0.0.15.255", "0.0.31.255", "0.0.63.255", "0.0.127.255",
		"0.0.255.255", "0.1.255.255", "0.3.255.255", "0.7.255.255",
		"0.15.255.255", "0.31.255.255", "0.63.255.255", "0.127.255.255",
		"0.255.255.255", "1.255.255.255", "3.255.255.255", "7.255.255.255",
		"15.255.255.255", "31.255.255.255", "63.255.255.255",
		"127.255.255.255", "255.255.255.255"};
	
	public static IpAddressManagement getInstance(){
		if (instance==null)
			instance = new IpAddressManagement();
		return instance;				
	}
	
	public int getNetNumber(String IpAddress){
		int i=0; 
		boolean found=false;
		
		for (;i<=32 && !found;i++)
			found=IpAddress.equals(ipnetToNum[i]);
		if (found)
			return --i; 
		
		return -1;
	}
	
	public long toLong(String ip) throws InvalidIpAddressException{
		long temp,i;
		
		StringTokenizer st = new StringTokenizer(ip,".");
		try
		{i = Long.parseLong(st.nextToken());
		if(i<0||i>256) throw new InvalidIpAddressException();
		temp = i;
		temp <<= 8;
		i=Long.parseLong(st.nextToken());
		if(i<0||i>256) throw new InvalidIpAddressException();
		temp+= i;
		temp <<= 8;
		i=Long.parseLong(st.nextToken());
		if(i<0||i>256) throw new InvalidIpAddressException();
		temp+= i;
		temp <<= 8;
		i=Long.parseLong(st.nextToken());
		if(i<0||i>256) throw new InvalidIpAddressException();
		temp+= i;
		}catch(NoSuchElementException e){throw new InvalidIpAddressException();}
		return temp;	
	}
	
	public long[] parseNet(String ip, long net) {
		long a,b,c,d,a1,b1,c1,d1,a2,b2,c2,d2;
		StringTokenizer ipAddress = new StringTokenizer(ip,".");
		StringTokenizer netmask = new StringTokenizer(ipnetToNum[(int)net],".");
		long []result = new long[2];
		
		ip = ipAddress.nextToken();
		a=Long.parseLong(ip);
		ip = ipAddress.nextToken();
		b=Long.parseLong(ip);
		ip = ipAddress.nextToken();
		c=Long.parseLong(ip);
		ip = ipAddress.nextToken();
		d=Long.parseLong(ip);
				
		ip = netmask.nextToken();
		a1=Long.parseLong(ip);
		ip = netmask.nextToken();
		b1=Long.parseLong(ip);
		ip = netmask.nextToken();
		c1=Long.parseLong(ip);
		ip = netmask.nextToken();
		d1=Long.parseLong(ip);
		
		a2 = a & a1;
		b2 = b & b1;
		c2 = c & c1;
		d2 = d & d1;
				
		result[0]= a2;
		result[0] <<= 8;
		result[0]+= b2 ;
		result[0] <<= 8;
		result[0]+= c2 ;
		result[0] <<= 8;
		result[0]+= d2;
		
		netmask = new StringTokenizer(orNet[32-((int)net)],".");
		
		ip = netmask.nextToken();
		a1=Long.parseLong(ip);
		ip = netmask.nextToken();
		b1=Long.parseLong(ip);
		ip = netmask.nextToken();
		c1=Long.parseLong(ip);
		ip = netmask.nextToken();
		d1=Long.parseLong(ip);
		
		a2 = a | a1;
		b2 = b | b1;
		c2 = c | c1;
		d2 = d | d1;
		
		result[1] = a2;
		result[1] <<= 8;
		result[1]+= b2;
		result[1] <<= 8;
		result[1]+= c2;
		result[1] <<= 8;
		result[1]+= d2;
		
		return result;
	}
        
        public String getIpFromLong(long l){
            long a,b,c,d;
            
            d = l & 255;
            l >>= 8;
            c = l & 255;
            l >>= 8;
            b = l & 255;
            l >>= 8;
            a = l & 255;
            
            return a+"."+b+"."+c+"."+d;
        }
	
}
