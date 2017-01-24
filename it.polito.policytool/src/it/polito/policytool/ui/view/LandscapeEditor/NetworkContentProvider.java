package it.polito.policytool.ui.view.LandscapeEditor;

import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.rule.selector.impl.IpSelector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

public class NetworkContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {
	
	private final Logger LOGGER = Logger.getLogger(NetworkContentProvider.class.getName());
	
	@Override
	public Object[] getConnectedTo(Object entity) {
		if (entity instanceof SecurityControl) {
			SecurityControl firewall = (SecurityControl) entity;
			Collection<Object> targets = new ArrayList<Object>();
			LOGGER.info("FIREWALL: "+firewall.getName());
			for (SecurityControl fw : firewall.getFirewalls()){
				targets.add(fw);
				LOGGER.info("   FW: "+fw.getName());
			}
			for (FilteringZone fz : firewall.getFilteringZones()){
				targets.add(fz);
				LOGGER.info("   FZ: "+fz.getName());
			}
			return targets.toArray();
		}
		if (entity instanceof Host) {
			Host host = (Host) entity;
			Collection<Object> targets = new ArrayList<Object>();
			StringBuffer sb = new StringBuffer("HOST:");
			for(Entry<String, IpSelector> e : host.getInterfaces().entrySet())
				sb.append(" "+e.getKey()+" "+e.getValue());
		//	targets.add(host.getFilteringZone());
			LOGGER.info("   FZ: "+host.getFilteringZone().getName());
			return targets.toArray();
		}
		if (entity instanceof FilteringZone) {
			FilteringZone filteringZone = (FilteringZone) entity;
			Collection<Object> targets = new ArrayList<Object>();
			LOGGER.info("FILTERINGZONE: "+filteringZone.getName());
			for (Host h : filteringZone.getHostList()) {
				targets.add(h);
				StringBuffer sb = new StringBuffer("HOST:");
				for(Entry<String, IpSelector> e : h.getInterfaces().entrySet())
					sb.append(" "+e.getKey()+" "+e.getValue());
			}
//			targets.add(filteringZone.getFirewall());
			LOGGER.info("   FW: "+filteringZone.getFirewall().getName());
			return targets.toArray();
		}
		LOGGER.info("NULL");
		LOGGER.info(entity.toString());
		return null;
	}
}
