package it.polito.policytoollib.rule.utils;

import it.polito.policytoollib.exception.rule.InvalidMacAddressException;
import it.polito.policytoollib.rule.selector.impl.MacSelector;

import java.util.StringTokenizer;

public class MacAddressManagement {

	private static MacAddressManagement instance=null;
	private MacAddressManagement(){}
	
	public static MacAddressManagement getInstance(){
		if (instance==null)
			instance = new MacAddressManagement();
		return instance;				
	}
	
	public long toLong(String mac) throws InvalidMacAddressException{
		long temp,i;
		
		StringTokenizer st = new StringTokenizer(mac,":");
		
		try{
			i = Long.decode("0x"+st.nextToken());
			if(i<0||i>255) throw new InvalidMacAddressException();
			temp = i;
			temp <<= 8;
			i = Long.decode("0x"+st.nextToken());
			if(i<0||i>255) throw new InvalidMacAddressException();
			temp+= i;
			temp <<= 8;
			i = Long.decode("0x"+st.nextToken());
			if(i<0||i>255) throw new InvalidMacAddressException();
			temp+= i;
			temp <<= 8;
			i = Long.decode("0x"+st.nextToken());
			if(i<0||i>255) throw new InvalidMacAddressException();
			temp+= i;
			temp <<= 8;
			i = Long.decode("0x"+st.nextToken());
			if(i<0||i>255) throw new InvalidMacAddressException();
			temp+= i;
			temp <<= 8;
			i = Long.decode("0x"+st.nextToken());
			if(i<0||i>255) throw new InvalidMacAddressException();
			temp+= i;
		}
		catch(NumberFormatException e){ throw new InvalidMacAddressException();}
				
		return temp;	
	}
	
	public String getMacFromLong(long l) throws InvalidMacAddressException{
        String ret = "";
        long[] v = new long[6];
        
		if(l<MacSelector.getMinLong()||l>MacSelector.getMaxLong()) throw new InvalidMacAddressException();
        
		v[5] = l & 255;
        l >>= 8;
		v[4] = l & 255;
        l >>= 8;
		v[3] = l & 255;
        l >>= 8;
        v[2] = l & 255;
        l >>= 8;
        v[1] = l & 255;
        l >>= 8;
        v[0] = l & 255;
        
        for(int i=0;i<6;i++)
        {
        	if(v[i]<16) ret += "0"+Long.toHexString(v[i]);
        	else ret += Long.toHexString(v[i]);
        	if(i!=5) ret += ":";
        }
        
        return ret;
    }
	
}
