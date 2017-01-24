package it.polito.policytoollib.rule.utils;

import it.polito.policytoollib.rule.selector.impl.MimeType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;


public class ParseMime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {																																												
		try {
			
			System.out.println(MimeType.value.length);
			
			FileReader fr = new FileReader("mime.txt");
			BufferedReader br = new BufferedReader(fr);
			
			FileWriter fw = new FileWriter("mimevideo.txt");
			
			
			String line="";
			int count=0;
			while (line!=null){

				StringTokenizer st = new StringTokenizer(line);
				
				if (st.hasMoreTokens()){
					String s = st.nextToken();
					count+=s.length();
					if (count>120){
						fw.append("\n");
						count=0;
					}
					fw.append("\""+s+"\",");
				}
				
				line=br.readLine();
			}
			
			fw.close();
			br.close();
			fr.close();
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
