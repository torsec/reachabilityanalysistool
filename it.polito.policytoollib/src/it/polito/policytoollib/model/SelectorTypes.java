package it.polito.policytoollib.model;

import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.Selector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.StringTokenizer;

public class SelectorTypes {

	private LinkedHashMap<String, Selector> selectorTypes;
	private PolicyAnalysisModel					model;
	
	public SelectorTypes() {
		selectorTypes = new LinkedHashMap<String, Selector>();
	}
	
	public SelectorTypes(PolicyAnalysisModel model) {
		selectorTypes = new LinkedHashMap<String, Selector>();
		this.model = model;
	}

	public LinkedHashMap<String, Selector> getAllSelectorTypes() {
		return selectorTypes;
	}

	public Selector getSelectorType(String selectorType) {
		return selectorTypes.get(selectorType).selectorClone();
	}

	public void addSelectorType(String selectorType, Selector selector) {
		Selector s = selector.selectorClone();
		s.empty();
		s.setLabel(selectorType);
		selectorTypes.put(selectorType, s);
	}
	
	public void moveSelectorUp(int selectorPosition)
	{
		if(selectorPosition==0) return;
		LinkedHashMap<String, Selector> newSelectorTypes = new LinkedHashMap<String, Selector>();
		int i=0;
		Entry<String,Selector> e2 = null;
		for(Entry<String, Selector> e : selectorTypes.entrySet())
		{
			if(i==selectorPosition-1)
				e2=e;
			else
			{
				newSelectorTypes.put(e.getKey(), e.getValue());
				if(e2!=null)
				{
					newSelectorTypes.put(e2.getKey(), e2.getValue());
					e2=null;
				}
			}
			i++;
		}
		selectorTypes = newSelectorTypes;
	}


	public void moveSelectorDown(int selectorPosition) {
		if(selectorPosition==selectorTypes.size()-1) return;
		LinkedHashMap<String, Selector> newSelectorTypes = new LinkedHashMap<String, Selector>();
		int i=0;
		Entry<String,Selector> e2 = null;
		for(Entry<String, Selector> e : selectorTypes.entrySet())
		{
			if(i==selectorPosition)
				e2=e;
			else
			{
				newSelectorTypes.put(e.getKey(), e.getValue());
				if(e2!=null)
				{
					newSelectorTypes.put(e2.getKey(), e2.getValue());
					e2=null;
				}
			}
			i++;
		}
		selectorTypes = newSelectorTypes;
	}
	
	public void modifySelectorType(String selectorName, String newSelectorName, Selector newSelector)
	{
		LinkedHashMap<String, Selector> newSelectorTypes = new LinkedHashMap<String, Selector>();
		for(Entry<String, Selector> e : selectorTypes.entrySet())
		{
			if(!e.getKey().equals(selectorName))
			{
				newSelectorTypes.put(e.getKey(), e.getValue());
			}
			else
			{
				Selector s = newSelector.selectorClone();
				s.setLabel(newSelectorName);
				s.empty();
				newSelectorTypes.put(newSelectorName, s);
			}
		}
		selectorTypes = newSelectorTypes;
		LinkedList<Policy> policies = new LinkedList<Policy>();
		policies.addAll(model.getPolicyList());
		policies.addAll(model.getNATPolicyList());
		policies.addAll(model.getVPNPolicyList());
		for(Policy p : policies)
		{
			for(GenericRule r : p.getRuleSet())
			{
				ConditionClause cc = r.getConditionClause();
				Selector oldSel = cc.getSelectors().get(selectorName);
				if(oldSel==null) continue;
				String oldSelClassName = oldSel.getClass().getName();
				cc.getSelectors().remove(selectorName);
				if(newSelector.getClass().getName().equals(oldSelClassName))
				{
					oldSel.setLabel(newSelectorName);
					try {
						cc.addSelector(newSelectorName, oldSel);
					} catch (IncompatibleSelectorException e) {
						System.err.println("Incompatible selector in rule "+r.getName()+" of policy "+p.getName());
					}
				}
			}
			Iterator<String> iter = p.getSelectorNames().iterator();
			if(p.getSelectorNames().size()>0)
			{
				do
				{
					String selectorName2 = iter.next();
					if(selectorName2.equals(selectorName))
					{
						p.getSelectorNames().remove(selectorName2);
						break;
					}
				}while(iter.hasNext());
			}
			p.getSelectorNames().add(newSelectorName);
		}	
	}
	
	public void removeSelectorType(String selectorName)
	{
		selectorTypes.remove(selectorName);
		LinkedList<Policy> policies = new LinkedList<Policy>();
		policies.addAll(model.getPolicyList());
		policies.addAll(model.getNATPolicyList());
		policies.addAll(model.getVPNPolicyList());
		for(Policy p : policies)
		{
			for(GenericRule r : p.getRuleSet())
				r.getConditionClause().getSelectors().remove(selectorName);
			p.getSelectorNames().remove(selectorName);
		}	
	}
	
	public void addSelectorTypes(InputStream input) throws IOException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {

		String selectorName;
		String selectorType;
		Selector s;

		BufferedReader br = new BufferedReader(new InputStreamReader(input));

		String str = null;
		str = br.readLine();

		while (str != null) {

			StringTokenizer st = new StringTokenizer(str, ":");

			if (st.hasMoreTokens()) {
				selectorName = st.nextToken();
				selectorType = null;
				if (st.hasMoreTokens()) {
					selectorType = st.nextToken();
				} else {
					throw new IOException("Error In configuration file: ");
				}

				Thread thread = Thread.currentThread();
				ClassLoader loader = thread.getContextClassLoader();
				thread.setContextClassLoader(this.getClass().getClassLoader());
				Class selectorClass = null;
				try {
					ClassLoader cloader = Thread.currentThread().getContextClassLoader();
					selectorClass = cloader.loadClass(selectorType);
				} finally {
					thread.setContextClassLoader(loader);
				}

				Constructor resConstructor = null;
				resConstructor = selectorClass.getConstructor(new Class[0]);
				s = (Selector) resConstructor.newInstance(new Object[0]);
				s.setLabel(selectorName);
				
				selectorTypes.put(selectorName, s);
			}

			str = br.readLine();
		}

		br.close();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (String sel : selectorTypes.keySet()) {
			sb.append(sel + selectorTypes.get(sel).getClass().getName() + "\n");
		}

		return sb.toString();
	}

	public String[] getSelectorNames() {
		return selectorTypes.keySet().toArray(new String[selectorTypes.size()]);
	}
	
}
