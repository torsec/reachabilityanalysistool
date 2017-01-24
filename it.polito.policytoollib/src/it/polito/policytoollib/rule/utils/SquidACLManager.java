package it.polito.policytoollib.rule.utils;

import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidNetException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.impl.DomainSelector;
import it.polito.policytoollib.rule.selector.impl.HTTPMethodSelector;
import it.polito.policytoollib.rule.selector.impl.IntMatcherSelector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.MimeTypeSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.RegExpSelectorImpl;
import it.polito.policytoollib.rule.selector.impl.SquidURLSelector;
import it.polito.policytoollib.rule.selector.impl.StringMatcherSelector;
import it.polito.policytoollib.rule.selector.impl.TimeSelector;
import it.polito.policytoollib.rule.selector.impl.UriSelector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


public class SquidACLManager {
	
	private static SquidACLManager instance=null;
	
	private LinkedList<String[]> http_access=null;
	private HashMap<String, Selector> aclMap=null;
	private HashMap<String, String> aclTypeMap=null;
	private IpAddressManagement ipmgmnt=null;
	private String filename;
	
	private SquidACLManager() throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException{
		ipmgmnt = IpAddressManagement.getInstance();
	}
	
	public static SquidACLManager getInstance() throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException{
		if (instance==null)
			instance = new SquidACLManager();
		return instance;
	}
	
	public List<GenericRule> readRuleFromFile(String filename){
		String line="";
		try {	
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			
			FileWriter fw = new FileWriter(filename+".new");
			
			this.filename=filename;
			
			line = br.readLine();
			String str="";
			
			http_access = new LinkedList<String[]>();
			aclMap = new HashMap<String, Selector>();
			aclTypeMap = new HashMap<String, String>();
			FileWriter fwc = new FileWriter("co.dat",false);
			fwc.append("no");
			while (line!=null){
				
				if(line.equalsIgnoreCase("http_access allow all")){
					System.out.println("Found \"http_access allow all\", this will be ignored, add manually if wanted");
				} else if (line.equalsIgnoreCase("http_access deny all")){
					
				} else if (line.length()>0){
					StringTokenizer st = new StringTokenizer(line);
					if (st.countTokens()>0){
						str = st.nextToken();
						
						if (str.equalsIgnoreCase("acl")) {
							
							if(line.contains("acl manager proto cache_object")){
								fwc.close();
								fwc = new FileWriter("co.dat",false);
								fwc.append("yes");
								
							} else {
							
							String aclname = st.nextToken();	
							String acl_type = st.nextToken();		
							
							aclTypeMap.put(aclname, acl_type);
							
							if (acl_type.equalsIgnoreCase("src") ||
									acl_type.equalsIgnoreCase("dst")) {
								
								IpSelector ipadd = new IpSelector();
								ipadd.setLabel(aclname);
								
								while (st.hasMoreTokens()){
									String ststr=st.nextToken();
									
									if(ststr.equals("#") || ststr.startsWith("#"))
											break;
									
									StringTokenizer st2 = new StringTokenizer(ststr,"/");
									StringTokenizer st3 = new StringTokenizer(st2.nextToken(),"-");
									
									String netmask="255.255.255.255";
									if (st2.hasMoreTokens())
										netmask = st2.nextToken();
									String [] ip = new String[2];
									
									ip[0] = st3.nextToken();
									if (st3.hasMoreTokens())
										ip[1]=st3.nextToken();
									else ip[1]="";
									
									int net;
									
									if (netmask.length()>2)
										net = ipmgmnt.getNetNumber(netmask);
									else net = Integer.parseInt(netmask);
										
									if (ip[1].equalsIgnoreCase(""))
										ipadd.addRange(ip[0], net);	
									else  ipadd.addRange(ip[0], ip[1]);
								}
								
								if (aclMap.containsKey(aclname)){
									Selector s = aclMap.get(aclname);
									s.union(ipadd);
								} else aclMap.put(aclname, ipadd);
								
								//System.out.println("ipadd");
							} else if (acl_type.equalsIgnoreCase("port")){
								//TODO sistemare string tokenizer  
								//TODO mettere unione per selettori in tutte le acltype
								
								PortSelector ps = new PortSelector();
								ps.setLabel(aclname);
								
								while (st.hasMoreTokens()){
									String ststr=st.nextToken();
									
									if(ststr.equals("#") || ststr.startsWith("#"))
											break;
									
									StringTokenizer st2 = new StringTokenizer(ststr,"-");
		 
									String port=st2.nextToken();
									
									
									if (st2.hasMoreTokens()){
										String st2str=st2.nextToken();
										
										if(st2str.equals("#") || st2str.startsWith("#"))
												break;
										
										ps.addRange(Integer.parseInt(port), Integer.parseInt(st2str));
									} else ps.addRange(Integer.parseInt(port));
								}
								
								if (aclMap.containsKey(aclname)){
									Selector s = aclMap.get(aclname);
									s.union(ps);
								} else aclMap.put(aclname, ps);
	
							} else if (acl_type.equalsIgnoreCase("proto")){
								UriSelector uri = new UriSelector();
								uri.setLabel(aclname);
								
								while (st.hasMoreTokens()){
									String ststr=st.nextToken();
									
									if(ststr.equals("#") || ststr.startsWith("#"))
											break;
									
									uri.addRange(ststr);
								}
								
								if (aclMap.containsKey(aclname)){
									Selector s = aclMap.get(aclname);
									s.union(uri);
								} else aclMap.put(aclname, uri);
								
							} else if (acl_type.equalsIgnoreCase("method")) {
								HTTPMethodSelector hms = new HTTPMethodSelector();
								hms.setLabel(aclname);
								
								while(st.hasMoreTokens()){
									String ststr=st.nextToken();
									
									if(ststr.equals("#") || ststr.startsWith("#"))
											break;
									
									hms.addRange(ststr);
								}
								
								if (aclMap.containsKey(aclname)){
									Selector s = aclMap.get(aclname);
									s.union(hms);
								} else aclMap.put(aclname, hms);
								
							} else if (acl_type.equalsIgnoreCase("srcdomain") ||
									acl_type.equalsIgnoreCase("dstdomain"))	{
								
								DomainSelector ds = new DomainSelector();
								ds.setLabel(aclname);
								ds.addRange(st.nextToken());
								
								if (aclMap.containsKey(aclname)){
									Selector s = aclMap.get(aclname);
									s.union(ds);
								} else aclMap.put(aclname, ds);
								
							}else if (acl_type.equalsIgnoreCase("srcdom_regex") || 
											acl_type.equalsIgnoreCase("dstdom_regex") ||
											acl_type.equalsIgnoreCase("url_regex") ||
											acl_type.equalsIgnoreCase("urlpath_regex") ||
											acl_type.equalsIgnoreCase("browser") ||
											acl_type.equalsIgnoreCase("ident_regex") ||
											acl_type.equalsIgnoreCase("proxy_auth_regex")) {
								
								RegExpSelectorImpl res = new SquidURLSelector();
								res.setLabel(aclname);
								String s = st.nextToken();
								if (s.equalsIgnoreCase("-i")){
									//TODO gestione caseinsensitive regexp
									s = st.nextToken();
								}
								//always case insensitive
								s = s.toLowerCase();
								
								if(s.charAt(0)=='^'){
									s=s.substring(1);
									s=s+".*";
								}
								
								if(s.contains(".")){
									for (int t=0;t<s.length();t++)
										if(s.charAt(t)=='.'){
											if(t==0){
												s = "\\"+s;
											} else {
												if(s.charAt(t-1)!='\\')
													s = s.substring(0, t)+"\\"+s.substring(t);
											}
										}
									
									
								}
								
								res.setRegExp(s);
								
								if (aclMap.containsKey(aclname)){
									Selector sl = aclMap.get(aclname);
									sl.union(res);
								} else aclMap.put(aclname, res);
								
							} else if (acl_type.equalsIgnoreCase("ident") || 
									acl_type.equalsIgnoreCase("proxy_auth") ||
									acl_type.equalsIgnoreCase("snmp_community") || 
									acl_type.equalsIgnoreCase("arp") ) {
								
								StringMatcherSelector sm = new StringMatcherSelector();
								sm.setLabel(aclname);
								
								while (st.hasMoreTokens()){
									String ststr=st.nextToken();
									
									if(ststr.equals("#") || ststr.startsWith("#"))
											break;
									
									sm.addRange(ststr);
								}
								
								if (aclMap.containsKey(aclname)){
									Selector sl = aclMap.get(aclname);
									sl.union(sm);
								} else aclMap.put(aclname, sm);
								
							} else if (acl_type.equalsIgnoreCase("time")){
								
								TimeSelector ts = new TimeSelector();
								ts.setLabel(aclname);
								
								String s=null;
								String s2=null;
								
								while (st.hasMoreTokens()){
									if (s==null)
										s = st.nextToken();
									s2="";
									
									boolean f=false;
									if (st.hasMoreTokens()){
										String ststr=st.nextToken();
										
										if(!(ststr.equals("#") || ststr.startsWith("#"))){		
											s2 = ststr;
											for (int i=0;i<TimeSelector.abb.length;i++)
												if (s2.equalsIgnoreCase(TimeSelector.abb[i]))
													f=true;
										}
									}
									
									if (!f){
										if (!s2.equalsIgnoreCase(""))
											s = s + " "+s2;
										ts.addRange(s);
										s=null;
									} else {
										ts.addRange(s);
										s = s2;
									}
		
								}
								
								if (aclMap.containsKey(aclname)){
									Selector sl = aclMap.get(aclname);
									sl.union(ts);
								} else aclMap.put(aclname, ts);
								
							}  else if (acl_type.equalsIgnoreCase("src_as") ||
										acl_type.equalsIgnoreCase("dst_as") ||
										acl_type.equalsIgnoreCase("max_conn") ||
										acl_type.equalsIgnoreCase("max_user_ip")){
								
								IntMatcherSelector ims = new IntMatcherSelector();
								ims.setLabel(aclname);
								
								while (st.hasMoreTokens()) {
									String ststr=st.nextToken();
									
									if(ststr.equals("#") || ststr.startsWith("#"))
											break;
									
									ims.addRange(ststr);
								}
								
								if (aclMap.containsKey(aclname)){
									Selector sl = aclMap.get(aclname);
									sl.union(ims);
								} else aclMap.put(aclname, ims);
								
							} else if (acl_type.equalsIgnoreCase("req_mime_type") ||
									   acl_type.equalsIgnoreCase("rep_mime_type")) {
								
								MimeTypeSelector mts = new MimeTypeSelector();
								mts.setLabel(aclname);
								
								while (st.hasMoreTokens()){
									String ststr=st.nextToken();
									
									if(ststr.equals("#") || ststr.startsWith("#"))
											break;
	
									mts.addRange(st.nextToken());
								}
								
								if (aclMap.containsKey(aclname)){
									Selector sl = aclMap.get(aclname);
									sl.union(mts);
								} else aclMap.put(aclname, mts);
							} else System.err.println("NOT FOUND: "+acl_type);
						} //end if cacheobject	
						
						} else if (str.equalsIgnoreCase("http_access")){
							
							if (line.contains("manager")){
								fwc.append("\n");
								fwc.append(line);
							} else{ 
							
								String [] acllist = new String[st.countTokens()];
								
								int i=0;
								while (st.hasMoreTokens())
									acllist[i++]=st.nextToken();
								
								boolean ok=true;
								
								if(acllist.length==2)
									if (acllist[0].equalsIgnoreCase("deny") && acllist[1].equalsIgnoreCase("all"))
										ok=false;
								
								if(ok)
									http_access.add(acllist);
							}
							
						} else {
								//System.out.println("Altro valore: "+str);
								fw.append(line);
								fw.append("\n");
						}

				} //end if stringtok
				}
				line = br.readLine();
				
			}
			//System.out.println("FUORI");
			br.close();
			fr.close();
			fw.close();
			fwc.close();
			
			LinkedList<GenericRule> rules = new LinkedList<GenericRule>();
			//TODO
			for (String[] acllist: http_access){
				LinkedHashMap<String, Selector> sel = new LinkedHashMap<String, Selector>();
				
				
				for (int i=1;i<acllist.length;i++){
					boolean neg=false;
				
					if (acllist[i].charAt(0)=='!'){
						neg=true;
						acllist[i]=acllist[i].substring(1);
					}
					String sqlabel = aclTypeMap.get(acllist[i]);
					//String label = factory.getLabelFromSquidAclType(sqlabel);
					Selector s;
					if (neg){
						s = aclMap.get(acllist[i]);
						s.complement();
					} else s = aclMap.get(acllist[i]);
					
					if (sel.containsKey(sqlabel)){
						sel.get(sqlabel).intersection(s);
					} else sel.put(sqlabel, s);
				}
				ConditionClause cc = new ConditionClause(sel);
				GenericRule r = new GenericRule(acllist[0].equalsIgnoreCase("allow") ? FilteringAction.ALLOW : FilteringAction.DENY, cc, "");
				//r.setLabel(acllist[0]);
				rules.add(r);
	
			}

			return rules;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchElementException e){
			e.printStackTrace();
			System.err.println("Error detected in Configuration File.");
			System.err.println("LINE: "+line);
			return null;
		} catch (InvalidNetException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidRangeException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidIpAddressException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("Errore: "+line);
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void writeRuleToFile(List<GenericRule> rules){
		HashMap<String, Selector> aclMap = new HashMap<String, Selector>();
		
		
		try {
			FileWriter fw;
			FileReader frc;
					 
			fw = new FileWriter(filename+".acl");
			frc = new FileReader("co.dat");
			BufferedReader br = new BufferedReader(frc);
			if(br.readLine().equalsIgnoreCase("yes")){
				fw.append("acl manager proto cache_object");
				fw.append("\n");
				String line = br.readLine();
				while (line!=null){
					fw.append(line);
					fw.append("\n");
					line = br.readLine();
				}
				fw.append("\n");
			}
			
			br.close();
			frc.close();
			for (GenericRule r : rules){
				
				for (String s : r.getConditionClause().getSelectorsNames()){
					String str = s;
					//str = str + " " + factory.getSquidfromLabel(s);
					
					if (!aclMap.containsKey(str)){
						aclMap.put(str, r.getConditionClause().get(s));
					} else {
						Selector s2 = aclMap.get(str);
						if (!s2.isEquivalent(r.getConditionClause().get(s))){
							System.err.println("ATTENZIONE STESSA LABEL MA SEL DIVERSI");
							System.err.println("s:\n"+r.getConditionClause().get(s)+"\ns2:\n"+s2);
						}
					}
				}
			}
			
			for (String str : aclMap.keySet()){
				fw.append("acl ");
				fw.append(str);
				fw.append(" ");
				fw.append(aclMap.get(str).toSquidString());
				fw.append("\n");
			}
			
			fw.append("\n");
			
			for(GenericRule r : rules){
				fw.append("http_access ");
				fw.append(r.getAction().toString().toLowerCase());
				fw.append(" ");
				for (String s : r.getConditionClause().getSelectorsNames()){
					fw.append(s);
					fw.append(" ");
				}
				fw.append("\n");
			}
			
			fw.append("\n");
			fw.append("http_access deny all");
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
