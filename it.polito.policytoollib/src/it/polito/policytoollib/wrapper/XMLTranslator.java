package it.polito.policytoollib.wrapper;

import it.polito.policytoollib.exception.policy.DuplicateExternalDataException;
import it.polito.policytoollib.exception.policy.IncompatibleExternalDataException;
import it.polito.policytoollib.exception.policy.InvalidActionException;
import it.polito.policytoollib.exception.policy.InvalidRuleIdenfierException;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.exception.rule.IllegalParamException;
import it.polito.policytoollib.exception.rule.InvalidIpAddressException;
import it.polito.policytoollib.exception.rule.InvalidNetException;
import it.polito.policytoollib.exception.rule.InvalidRangeException;
import it.polito.policytoollib.exception.rule.UnsupportedSelectorException;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.landscape.Host;
import it.polito.policytoollib.landscape.Landscape;
import it.polito.policytoollib.landscape.Service;
import it.polito.policytoollib.model.SelectorTypes;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.policy.impl.PolicyImpl;
import it.polito.policytoollib.policy.resolution.ExternalDataResolutionStrategy;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.translation.canonicalform.CanonicalForm;
import it.polito.policytoollib.policy.translation.morphisms.FMRMorphism;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.ActionData;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.action.IPSecAction;
import it.polito.policytoollib.rule.action.IPSecActionType;
import it.polito.policytoollib.rule.action.LoggingAction;
import it.polito.policytoollib.rule.action.NATAction;
import it.polito.policytoollib.rule.action.NATActionType;
import it.polito.policytoollib.rule.action.TransformatonAction;
import it.polito.policytoollib.rule.impl.ConditionClause;
import it.polito.policytoollib.rule.impl.GenericRule;
import it.polito.policytoollib.rule.selector.ExactMatchSelector;
import it.polito.policytoollib.rule.selector.Selector;
import it.polito.policytoollib.rule.selector.TotalOrderedSelector;
import it.polito.policytoollib.rule.selector.impl.IpSelector;
import it.polito.policytoollib.rule.selector.impl.PortSelector;
import it.polito.policytoollib.rule.selector.impl.StandardRegExpSelector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The class XMLTranslator is a helper class to write a network structure and
 * its policies in a XML file and to read a XML file and returns the network
 * structure and policies saved in it.
 */
public class XMLTranslator {
	
	private SelectorTypes selectorTypes;
	
	public XMLTranslator(SelectorTypes selectorTypes){
		this.selectorTypes = selectorTypes;
	}

	/**
	 * The public function writeOriginalRuleSetFromPolicy() is used to save a
	 * policy in a XML file, it uses the function writeRules().
	 * 
	 * @param policy
	 *            the policy
	 * @param nameFile
	 *            the name file
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean writeOriginalRuleSetFromPolicy(Policy policy, String nameFile) throws Exception {
		return writeRules(policy.getRuleSet(), policy, nameFile);
	}

	/**
	 * The public function writeAnalizedRuleSetFromCanonicalForm() is used to
	 * save a canonical from in a XML file, it uses the function writeRules().
	 * 
	 * @param can
	 *            the can
	 * @param nameFile
	 *            the name file
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean writeAnalizedRuleSetFromCanonicalForm(CanonicalForm can, String nameFile) throws Exception {

		FMRMorphism exporter = new FMRMorphism(can);

		return writeRules(new HashSet<GenericRule>(exporter.exportRules()), can.getOriginalPolicy(), nameFile);
	}
	
	
	public boolean writeLandscape(Landscape landscape, String nameFile) throws Exception {
		
		if (nameFile.equals(""))
			throw new Exception("Filename not specified");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Create the document
		Document doc = builder.newDocument();

		// Create and append the root element
		Element root = (Element) doc.createElement("Network");
		doc.appendChild(root);
		
		//fwA-->itfA.1-->fwB liste perchè un firewall può avere più interfaccia e ogni interfaccia può essere collegata a più firewall
		//LinkedList<String[]> links = new LinkedList<String[]>();  

		for(Entry<String, FilteringZone> e : landscape.getZoneList().entrySet())
		{
			Element zoneEl = (Element) doc.createElement("FilteringZone");
			FilteringZone zone = e.getValue();
			zoneEl.setAttribute("Name", zone.getName());
			zoneEl.setIdAttribute("Name", true);
			root.appendChild(zoneEl);
		}
		
		Element firstZoneEl = (Element) root.getFirstChild();
		for(Entry<String, SecurityControl> e : landscape.getFirewallList().entrySet())
		{
			Element firewallEl = (Element) doc.createElement("Firewall");
			SecurityControl fw = e.getValue();
			firewallEl.setAttribute("Name", fw.getName());
			for(Entry<String, IpSelector> e2 : fw.getInterfacesIp().entrySet())
			{
				Element fwItfEl = (Element) doc.createElement("Interface");
				fwItfEl.setAttribute("Name", e2.getKey());
				fwItfEl.setAttribute("IP", e2.getValue().toSingleIpString());
				firewallEl.appendChild(fwItfEl);
			}
			for(Entry<String, FilteringZone> e2 : fw.getFilteringZonesWithInterfaces().entrySet())
			{
				Element zoneEl = doc.getElementById(e2.getValue().getName());
				zoneEl.setAttribute("Subnet", e2.getValue().getIPSubnet().toSubnetString());
				zoneEl.setAttribute("Firewall", e2.getKey());
			}
			/*for(Entry<String, List<Firewall>> e2 : fw.getConnection_fw().entrySet())
			{
				String itf = e2.getKey();
				for(Firewall fw2 : e2.getValue())
				{
					for(Entry<String, List<Firewall>> e3 : fw2.getConnection_fw().entrySet())
					{
						String itf2 = e3.getKey();
						for(Firewall fw3 : e3.getValue())
						{
							if(fw==fw3)
								for(Entry<String, List<Firewall>> e4 : fw3.getConnection_fw().entrySet())
								{
									String itf3 = e4.getKey();
									if(itf.compareTo(itf3)==0)
									{
										links.add(new String[itf,itf2]));
									}
								}
						}
					}
				}
			}*/
			root.insertBefore(firewallEl, firstZoneEl);
		}
		
		for(Entry<String, Host> e : landscape.getHost_list().entrySet())
		{
			Element hostEl = (Element) doc.createElement("Host");
			Host host = e.getValue();
			hostEl.setAttribute("Name", e.getKey());
			hostEl.setAttribute("FilteringZone", host.getFilteringZone().getName());
			for(Entry<String, IpSelector> e2 : host.getInterfaces().entrySet())
			{
				Element hostItfEl = (Element) doc.createElement("Interface");
				hostItfEl.setAttribute("Name", e2.getKey());
				hostItfEl.setAttribute("IP", e2.getValue().toSingleIpString());
				hostEl.appendChild(hostItfEl);
			}
			for(Entry<String, Service> e2 : host.getServices().entrySet())
			{
				Element serviceEl = (Element) doc.createElement("Service");
				serviceEl.setAttribute("Name", e2.getKey());
				Service srv = e2.getValue();
				serviceEl.setAttribute("IP", srv.getAddress().toSingleIpString());
				serviceEl.setAttribute("Port", srv.getPort().toSinglePortString());
				hostEl.appendChild(serviceEl);
			}
			root.appendChild(hostEl);
		}
		
		for(String[] e: landscape.getLink_list())
		{
			Element linkEl = (Element) doc.createElement("Link");
			linkEl.setAttribute("Endpoint1", e[0]);
			linkEl.setAttribute("Endpoint2", e[1]);
			root.appendChild(linkEl);
		}
		
		TransformerFactory xformFactory = TransformerFactory.newInstance();
		Transformer idTransform = xformFactory.newTransformer();
		idTransform.setOutputProperty(OutputKeys.INDENT, "yes");
		idTransform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		Source input = new DOMSource(doc);
		Result output = new StreamResult(nameFile);
		idTransform.setOutputProperty(OutputKeys.VERSION, "1.0");
		idTransform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "xml_network.dtd");
		idTransform.transform(input, output);

		return true;
	}

	
	public void writeSelectorTypes(SelectorTypes selectorTypes, String nameFile) throws Exception {
		if (nameFile.equals(""))
			throw new Exception("Filename not specified");

		FileWriter f = new FileWriter(nameFile);
		for(Entry<String, Selector> e : selectorTypes.getAllSelectorTypes().entrySet())
			f.write(e.getKey()+':'+e.getValue().getClass().getName()+'\n');
		f.close();
	}
	
	/**
	 * The functions writeRules() is a private function used by the two
	 * functions writeOriginalRuleSetFromPolicy() and
	 * writeAnalizedRuleSetFromCanonicalForm() which writes the rules to the XML
	 * file.
	 * 
	 * @param ruleList
	 *            the rule list
	 * @param policy
	 *            the policy
	 * @param nameFile
	 *            the name file
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	private boolean writeRules(Set<GenericRule> ruleList, Policy policy, String nameFile) throws Exception {

		/*if (ruleList == null || ruleList.size() == 0) {
			System.err.println("Resolution Database Empty");
			return false;
		}*/

		if (nameFile.equals(""))
			throw new Exception("Filename not specified");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Create the document
		Document doc = builder.newDocument();

		// Create and append the root element
		Element root = (Element) doc.createElement("Policy");
		doc.appendChild(root);

		// call the funtions for creating the single elements
		writePolicyInfo(doc, root, policy);
		for (GenericRule r : policy.getRuleSet())
			writeRule(doc, root, r, policy);

		// create the xml file
		TransformerFactory xformFactory = TransformerFactory.newInstance();
		Transformer idTransform = xformFactory.newTransformer();
		idTransform.setOutputProperty(OutputKeys.INDENT, "yes");
		Source input = new DOMSource(doc);
		Result output = new StreamResult(nameFile);
		idTransform.setOutputProperty(OutputKeys.VERSION, "1.0");
		idTransform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "xml_policy.dtd");
		idTransform.transform(input, output);

		return true;
	}

	/**
	 * The function writePolicyInfo() writes the general informations of a
	 * policy in the XML file.
	 * 
	 * @param doc
	 *            the doc
	 * @param root
	 *            the root
	 * @param policy
	 *            the policy
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	private void writePolicyInfo(Document doc, Element root, Policy policy) throws Exception {
		
		Element policyName = doc.createElement("PolicyName");
		Text policyName_text = doc.createTextNode(policy.getName());
		policyName.appendChild(policyName_text);
		root.appendChild(policyName);
		
		Element policyType = doc.createElement("PolicyType");
		Text policyType_text = doc.createTextNode(policy.getPolicyType().toString());
		policyType.appendChild(policyType_text);
		root.appendChild(policyType);

		Element resolver = doc.createElement("Resolver");
		Text resolver_text = doc.createTextNode(policy.getResolutionStrategy().getClass().getName());
		resolver.appendChild(resolver_text);
		root.appendChild(resolver);

		if (policy.getResolutionStrategy() instanceof ExternalDataResolutionStrategy) {
			@SuppressWarnings("rawtypes")
			ExternalDataResolutionStrategy res = ((ExternalDataResolutionStrategy) policy.getResolutionStrategy());
			Element externalDataClass = doc.createElement("ExternalDataClass");
			Text externalDataClass_text = doc.createTextNode(res.getExternalDataClassName());
			externalDataClass.appendChild(externalDataClass_text);
			root.appendChild(externalDataClass);
		}

		Element defAction = doc.createElement("DefAction");
		Text defAction_text = doc.createTextNode(policy.getDefaultAction().toString());
		defAction.appendChild(defAction_text);
		root.appendChild(defAction);
	}

	/**
	 * The function writeRule() writes one single rule of a policy in the XML
	 * file.
	 * 
	 * @param doc
	 *            the doc
	 * @param root
	 *            the root
	 * @param r
	 *            the r
	 * @param policy
	 *            the policy
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void writeRule(Document doc, Element root, GenericRule r, Policy policy) {

		Element rule = doc.createElement("Rule");

		if(r.getAction() instanceof IPSecAction)
			rule.setAttribute("Action", ((IPSecAction)r.getAction()).toStringWithoutKeys());
		else
			rule.setAttribute("Action", r.getAction().toString());
		
		rule.setAttribute("Label", r.getName());

		if (policy.getResolutionStrategy() instanceof ExternalDataResolutionStrategy) {
			Element externalDataValue = doc.createElement("ExternalDataValue");
			Text externalDataValue_text = doc.createTextNode(((ExternalDataResolutionStrategy) policy.getResolutionStrategy()).getExternalData(r).toString());
			externalDataValue.appendChild(externalDataValue_text);
			rule.appendChild(externalDataValue);
		}

		for (String name:r.getConditionClause().getSelectorsNames())
			writeSelector(doc, rule, name, r.getConditionClause().get(name));
		
		if(r.getAction() instanceof TransformatonAction)
		{
			Element transElement = doc.createElement("Transformation");
			ConditionClause trans = ((TransformatonAction)r.getAction()).getTransformation(); 
			if(trans!=null)
				for (String name : trans.getSelectorsNames())
					writeSelector(doc, rule, name, trans.get(name));
		}
		
		ActionData[] adVector = r.getAction().getActionData();
		if(adVector!=null)
			for(ActionData ad : adVector)
			{
				Element adElement = doc.createElement("ActionData");
				adElement.setAttribute("Name", ad.getName());
				adElement.setAttribute("Value", ad.getValue());
				rule.appendChild(adElement);
			}

		root.appendChild(rule);
	}

	/**
	 * The function writeSelector() writes the one single selector of a rule in
	 * the XML file.
	 * 
	 * @param doc
	 *            the doc
	 * @param root
	 *            the root
	 * @param s
	 *            the s
	 */
	private void writeSelector(Document doc, Element root, String name, Selector s) {
		Element selector = doc.createElement("Selector");

		selector.setAttribute("Label", name);

		Text value_text = doc.createTextNode(s.toSimpleString());
		selector.appendChild(value_text);

		root.appendChild(selector);

	}

	/**
	 * The function readRuleSet() reads policies from a XML file and creates the
	 * corresponded classes.
	 * 
	 * @param nameFile
	 *            the name file
	 * @return the sets the
	 * @throws Exception
	 *             the exception
	 */
	public PolicyImpl readPolicy(String nameFile) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating(true);
		factory.setNamespaceAware(true);

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.parse(nameFile);

		NodeList list = doc.getElementsByTagName("Policy");

		return getPolicy(list.item(0));
	}

	/**
	 * The function readRuleSet() reads policies from a XML file and creates the
	 * corresponded classes.
	 * 
	 * @param nameFile
	 *            the name file
	 * @return the sets the
	 * @throws Exception
	 *             the exception
	 */
	public PolicyImpl readPolicy(InputStream nameFile) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating(true);
		factory.setNamespaceAware(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(
				new EntityResolver()
					{
					    @Override
					    public InputSource resolveEntity(String publicId, String systemId)
					            throws SAXException, IOException
					    {
					        if (systemId.contains("xml_network.dtd"))
					        {
					        	InputStream a = XMLTranslator.class.getResourceAsStream("xml_network.dtd");
					        	return new InputSource(a);
					        }
					        else if (systemId.contains("xml_policy.dtd"))
					            return new InputSource(XMLTranslator.class.getResourceAsStream("xml_policy.dtd"));
					        else
					            return null;
					    }
					});
		Document doc = builder.parse(nameFile);

		NodeList list = doc.getElementsByTagName("Policy");

		return getPolicy(list.item(0));
	}

	/**
	 * The function getPolicy() reads one single policy from a XML file.
	 * 
	 * @param node
	 *            the node
	 * @return the policy
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PolicyImpl getPolicy(Node node) throws Exception {

		ResolutionStrategy resolutionStrategy = null;
		Action defAction = null;
		PolicyImpl policy;
		Constructor extConstructor = null;
		String name = "";
		PolicyType policyType = null;

		Node n = node.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getNodeName().equals("Resolver")) {
					if (n.getFirstChild().getNodeValue().equals("it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy")) {
						resolutionStrategy = new FMRResolutionStrategy();
					} else {
						Class resolutionStrategyClass = ClassLoader.getSystemClassLoader().loadClass(n.getFirstChild().getNodeValue());
						Constructor resConstructor = resolutionStrategyClass.getConstructor(new Class[0]);
						resolutionStrategy = (ResolutionStrategy) resConstructor.newInstance(new Object[0]);
					}
				}
				if (n.getNodeName().equals("ExternalDataClass")) {
					Class extDataClass = ClassLoader.getSystemClassLoader().loadClass(n.getFirstChild().getNodeValue());
					Class[] param = { String.class };
					extConstructor = extDataClass.getConstructor(param);
				}
				if (n.getNodeName().equals("DefAction")) {
					String defAction_text = n.getFirstChild().getNodeValue();
					if (defAction_text.equalsIgnoreCase("ALLOW"))
						defAction = FilteringAction.ALLOW;
					else if (defAction_text.equalsIgnoreCase("DENY"))
						defAction = FilteringAction.DENY;
					else
						throw new InvalidActionException();
				}
				if (n.getNodeName().equals("PolicyName")) {
					name = n.getFirstChild().getNodeValue();
				}
				if (n.getNodeName().equals("PolicyType")) {
					String policyTypeName = n.getFirstChild().getNodeValue();
					if(policyTypeName.equals("FILTERING"))
						policyType = PolicyType.FILTERING;
					if(policyTypeName.equals("NAT"))
						policyType = PolicyType.NAT;
					if(policyTypeName.equals("VPN"))
						policyType = PolicyType.VPN;
					if(policyTypeName.equals("LOGGING"))
						policyType = PolicyType.LOGGING;
				}
			}
			n = n.getNextSibling();
		}

		policy = new PolicyImpl(resolutionStrategy, defAction, policyType, name);
		getRules(policy, node, extConstructor);

		return policy;
	}

	/**
	 * The function getRuls() reads one single rule from a XML file.
	 * 
	 * @param policy
	 *            the policy
	 * @param node
	 *            the node
	 * @param extConstructor
	 *            the ext constructor
	 * @param ruleID
	 *            the rule id
	 * @return the rules
	 * @throws InvalidActionException
	 *             the invalid action exception
	 * @throws SecurityException
	 *             the security exception
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws IncompatibleExternalDataException
	 *             the incompatible external data exception
	 * @throws DuplicateExternalDataException
	 *             the duplicate external data exception
	 * @throws NoExternalDataException
	 *             the no external data exception
	 * @throws InvalidRuleIdenfierException
	 *             the invalid rule idenfier exception
	 * @throws InvalidRangeException
	 *             the invalid range exception
	 * @throws IllegalParamException
	 * @throws UnsupportedSelectorException 
	 */
	@SuppressWarnings("rawtypes")
	private void getRules(PolicyImpl policy, Node node, Constructor extConstructor) throws InvalidActionException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, IncompatibleExternalDataException, DuplicateExternalDataException, NoExternalDataException, InvalidRuleIdenfierException, InvalidRangeException, IllegalParamException, UnsupportedSelectorException {
		Action action = null;
		GenericRule rule;
		String extDataValue = null;

		node = node.getFirstChild();

		while (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE)
				if (node.getNodeName().equals("Rule")) {
					LinkedHashMap<String, Selector> hmsel = new LinkedHashMap<String, Selector>();
					ConditionClause transformation = null;
					
					String label_text = ((Element) node).getAttribute("Label");
					String ipSecKey = "";
					String ipSecHashKey = "";
					Node n = node.getFirstChild();
					while (n != null) {
						if (n.getNodeType() == Node.ELEMENT_NODE) {
							if (n.getNodeName().equals("ExternalDataValue")) {
								extDataValue = n.getFirstChild().getNodeValue();
							}
							if (n.getNodeName().equals("Selector")) {
								getSelectors(n, hmsel);
							}
							if (n.getNodeName().equals("Transformation")) {
								Node nn = n.getFirstChild();
								LinkedHashMap<String, Selector> nhmsel = new LinkedHashMap<String, Selector>();
								while (nn != null) {
									if (nn.getNodeType() == Node.ELEMENT_NODE) {
										if (nn.getNodeName().equals("Selector")) {
											getSelectors(nn, nhmsel);
										}
									}
									nn = nn.getNextSibling();
								}
								transformation = new ConditionClause(nhmsel);
							}
							if (n.getNodeName().equals("ActionData")){
								String name = ((Element)n).getAttribute("Name");
								if(name.equals("Key"))
									ipSecKey = ((Element)n).getAttribute("Value");
								else if(name.equals("Hashkey"))
									ipSecHashKey = ((Element)n).getAttribute("Value");
							}
						}
						n = n.getNextSibling();
					}
					
					String action_text = ((Element) node).getAttribute("Action");
					
					if (action_text.equalsIgnoreCase("ALLOW"))
						action = FilteringAction.ALLOW;
					else if (action_text.equalsIgnoreCase("DENY"))
						action = FilteringAction.DENY;
					else if (action_text.equalsIgnoreCase("LOGGING"))
						action = LoggingAction.LOGGING;
					else if (action_text.equalsIgnoreCase("AH"))
						action = new IPSecAction(ipSecKey, ipSecHashKey, IPSecActionType.AH, null);
					else if (action_text.equalsIgnoreCase("INVERT_AH"))
						action = new IPSecAction(ipSecKey, ipSecHashKey, IPSecActionType.INVERT_AH, null);
					else if (action_text.equalsIgnoreCase("ESP"))
						action = new IPSecAction(ipSecKey, ipSecHashKey, IPSecActionType.ESP, transformation);
					else if (action_text.equalsIgnoreCase("INVERT_ESP"))
						action = new IPSecAction(ipSecKey, ipSecHashKey, IPSecActionType.INVERT_ESP, null);
					else if (action_text.equalsIgnoreCase("PRENAT"))
						action = new NATAction(NATActionType.PRENAT, transformation);
					else if (action_text.equalsIgnoreCase("POSTNAT"))
						action = new NATAction(NATActionType.POSTNAT, transformation);
					else
						throw new InvalidActionException();
					
					rule = new GenericRule(action, new ConditionClause(hmsel), label_text);

					if (policy.getResolutionStrategy() instanceof ExternalDataResolutionStrategy) {
						Object[] oParam = { extDataValue };
						Object externalData = extConstructor.newInstance(oParam);

						policy.insertRule(rule, externalData);
					} else
						policy.insertRule(rule);
				}
			node = node.getNextSibling();
		}

	}

	/**
	 * The function getSelectors() reads one single selector from a XML file.
	 * 
	 * @param n
	 *            the n
	 * @param ruleID
	 *            the rule id
	 * @param hmsel
	 *            the hmsel
	 * @return the selectors
	 * @throws SecurityException
	 *             the security exception
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InvalidRuleIdenfierException
	 *             the invalid rule idenfier exception
	 * @throws InvalidRangeException
	 *             the invalid range exception
	 * @throws IllegalParamException
	 */
	private void getSelectors(Node n, HashMap<String, Selector> hmsel) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, InvalidRuleIdenfierException, InvalidRangeException, IllegalParamException {
		String labTemp = null;
		String valTemp = null;

		labTemp = ((Element) n).getAttribute("Label");

		valTemp = n.getFirstChild().getNodeValue();
		

		Selector sel = selectorTypes.getSelectorType(labTemp);
		StringTokenizer st = new StringTokenizer(valTemp, ";");

		if (sel instanceof TotalOrderedSelector) {

			while (st.hasMoreTokens()) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken(), "-");
				if(st2.countTokens() == 2)
					((TotalOrderedSelector) sel).addRange(st2.nextToken(), st2.nextToken());
				else {
					String token = st2.nextToken();
					((TotalOrderedSelector) sel).addRange(token, token);
				}
			}
		} else if (sel instanceof ExactMatchSelector) {

			while (st.hasMoreTokens())
				((ExactMatchSelector) sel).addRange(st.nextToken());

		} else if (sel instanceof StandardRegExpSelector) {

			while (st.hasMoreTokens())
				((StandardRegExpSelector) sel).addRange(st.nextToken());

		} else
			throw new InvalidClassException(sel.getClass().getName());

		hmsel.put(labTemp, sel);
	}

	public Landscape readLandscape(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException, InvalidIpAddressException, InvalidRangeException, NumberFormatException, InvalidNetException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating(true);
		factory.setNamespaceAware(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(
			new EntityResolver()
				{
				    @Override
				    public InputSource resolveEntity(String publicId, String systemId)
				            throws SAXException, IOException
				    {
				        if (systemId.contains("xml_network.dtd"))
				        {
				        	InputStream a = XMLTranslator.class.getResourceAsStream("xml_network.dtd");
				        	return new InputSource(a);
				        }
				        else if (systemId.contains("xml_policy.dtd"))
				            return new InputSource(XMLTranslator.class.getResourceAsStream("xml_policy.dtd"));
				        else
				            return null;
				    }
				});
		
		Document doc = builder.parse(inputStream);

		NodeList list = doc.getElementsByTagName("Network");

		HashMap<String, SecurityControl> fw_list = getFirewalls(list.item(0));
		HashMap<String, FilteringZone> zone_list = getFilteringZones(list.item(0), fw_list);
		HashMap<String, Host> host_list = getHosts(list.item(0), zone_list);

		Landscape landscape = new Landscape(fw_list, zone_list, host_list);

		getLinks(landscape, list.item(0), fw_list);

		return landscape;
	}

	private void getLinks(Landscape landscape, Node network, HashMap<String, SecurityControl> fw_list) {
		Node n = network.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getNodeName().equals("Link")) {
					String ep1 = ((Element) n).getAttribute("Endpoint1");
					String ep2 = ((Element) n).getAttribute("Endpoint2");

					Document doc = network.getOwnerDocument();
					SecurityControl fw1 = fw_list.get(doc.getElementById(ep1).getParentNode().getAttributes().getNamedItem("Name").getNodeValue());
					SecurityControl fw2 = fw_list.get(doc.getElementById(ep2).getParentNode().getAttributes().getNamedItem("Name").getNodeValue());
					
					fw1.addFW(ep1, fw2);
					fw2.addFW(ep2, fw1);
					landscape.addLink(ep1, ep2);
				}
			}
			n = n.getNextSibling();
		}
	}

	public HashMap<String, SecurityControl> getFirewalls(Node network) throws InvalidIpAddressException, InvalidRangeException {
		HashMap<String, SecurityControl> fw_list = new HashMap<String, SecurityControl>();

		Node n = network.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getNodeName().equals("Firewall")) {
					String name = ((Element) n).getAttribute("Name");

					fw_list.put(name, new SecurityControl(getInterfaces(n), new HashMap<String, IpSelector>(), name));
				}
			}
			n = n.getNextSibling();
		}

		return fw_list;
	}

	public HashMap<String, IpSelector> getInterfaces(Node firewall) throws InvalidIpAddressException, InvalidRangeException {
		HashMap<String, IpSelector> interface_list = new HashMap<String, IpSelector>();

		Node n = firewall.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getNodeName().equals("Interface")) {
					String name = ((Element) n).getAttribute("Name");
					String ip = ((Element) n).getAttribute("IP");
					IpSelector ipsel = new IpSelector();
					ipsel.addRange(ip);
					interface_list.put(name,ipsel);
				}
			}
			n = n.getNextSibling();
		}

		return interface_list;
	}

	public HashMap<String, FilteringZone> getFilteringZones(Node network, HashMap<String, SecurityControl> fw_list) throws InvalidIpAddressException, InvalidRangeException, NumberFormatException, InvalidNetException {
		HashMap<String, FilteringZone> zone_list = new HashMap<String, FilteringZone>();

		Node n = network.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getNodeName().equals("FilteringZone")) {
					String name = ((Element) n).getAttribute("Name");
					String fw_itf = ((Element) n).getAttribute("Firewall");
					String IP_Subnet = ((Element) n).getAttribute("Subnet");
					//Firewall firewall = fw_list.get(fw.split("\\.")[0]);
					SecurityControl firewall = fw_list.get(((Element)n.getOwnerDocument().getElementById(fw_itf).getParentNode()).getAttribute("Name"));
					FilteringZone filteringZone = new FilteringZone(name, IP_Subnet, firewall);
					zone_list.put(name, filteringZone);
					firewall.addFilteringZone(fw_itf, filteringZone);
				}
			}
			n = n.getNextSibling();
		}

		

		return zone_list;
	}

	public HashMap<String, Host> getHosts(Node network, HashMap<String, FilteringZone> zone_list) throws InvalidIpAddressException, InvalidRangeException {
		HashMap<String, Host> host_list = new HashMap<String, Host>();
		
		Node n = network.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getNodeName().equals("Host")) {
					String name = ((Element) n).getAttribute("Name");
					String filteringZone = ((Element) n).getAttribute("FilteringZone");

					Node inter = n.getFirstChild();
					HashMap<String, Service> services = new HashMap<String, Service>();
					HashMap<String, IpSelector> interfaces = new HashMap<String, IpSelector>();
					while (inter != null) {
						if (inter.getNodeType() == Node.ELEMENT_NODE) {
							if (inter.getNodeName().equals("Interface")) {
								IpSelector itf_ipsel = new IpSelector();
								String itf_ip = ((Element) inter).getAttribute("IP");
								itf_ipsel.addRange(itf_ip);
								interfaces.put(((Element) inter).getAttribute("Name"), itf_ipsel);
							}
							if (inter.getNodeName().equals("Service")) {
								IpSelector ips = new IpSelector();
								ips.addRange(((Element) inter).getAttribute("IP"));
								PortSelector port = new PortSelector();
								port.addRange(((Element) inter).getAttribute("Port"));
								Service service = new Service(ips,port);
								services.put(((Element) inter).getAttribute("Name"), service);
							}
						}
						inter = inter.getNextSibling();
					}
					Host host = new Host(name, interfaces, zone_list.get(filteringZone));
					host_list.put(name, host);
					host.addServices(services);
				}
			}
			n = n.getNextSibling();
		}
		return host_list;
	}

	
}
