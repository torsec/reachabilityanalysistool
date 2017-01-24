package it.polito.policytool.util;

import java.net.URL;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import it.polito.policytool.Activator;

public enum Figure
{
	
	NETWORK("network.gif"), HOST("host.gif"), POLICY("policy.gif"), OK ("tick.png"), ERROR("error.png"); 
//	/** The ABox phase image. **/
//	ABOX_PHASE("abox.gif"),
//	/** The automatic image. **/
//	AUTOMATIC("automatic.png"),
//	/** The axiom image. **/
//	AXIOM("axiom.gif"),
//	/** The axiom set image. **/
//	AXIOM_SET("axiomset.gif"),
//	/** The checked image. **/
//	CHECKED("checked.gif"),
//	/** The class image. **/
//	CLASS("class.gif"),
//	/** The configuration image. **/
//	CONFIGURATION("configuration.gif"),
//	/** The configuration rule image. **/
//	CONFIGURATION_RULE("confrule.gif"),
//	/** The data property image. **/
//	DATA_PROPERTY("dataproperty.gif"),
//	/** The data model image. **/
//	DATAMODEL("datamodel.gif"),
//	/** The device image. **/
//	DEVICE("device.gif"), 
//	/** The do all image. **/
//	DO_ALL("doall.png"),
//	/** The drools image. **/
//	DROOLS("drools.png"),
//	/** The enrichment image. **/
//	ENRICHMENT("enrichment.gif"),
//	/** The exclude image. **/
//	EXCLUDE("exclude.gif"),
//	/** The explain image. **/
//	EXPLAIN("explain.gif"),
//	/** The filter condition image. **/
//	FILTER_CONDITION("filtercond.gif"),
//	/** An ontology individual image. **/
//	INDIVIDUAL("individual.gif"),
//	/** The IT policy image. **/
//	IT_POLICY("policy.gif"),
//	/** The IT security rule image. **/
//	IT_SECURITY_RULE("secrule.png"),
//	/** The key-value image. **/
//	KEY_VALUE("keyvalue.gif"),
//	/** The LA image. **/
//	LA("la.png"),
//	/** The LA aggregation image. **/
//	LA_AGGREGATION("laaggregation.png"),
//	/** The LA extraction image. **/
//	LA_EXTRACTION("laextraction.png"),
//	/** The LA implementation image. **/
//	LA_IMPLEMENTATION("laimpl.png"),
//	/** The link image. **/
//	LINK("link.gif"),
//	/** The low-level mapping phase image. **/
//	LOW_LEVEL_MAPPING("lowlevel.gif"),
//	/** The manual image. **/
//	MANUAL("manual.png"),
//	/** The object property image. **/
//	OBJECT_PROPERTY("objproperty.gif"),
//	/** The OWL image. **/
//	OWL("owl.png"),
//	/** The PoSecCo logo. **/
//	POSECCO("posecco.png"),
//	/** The privilege image. **/
//	PRIVILEGE("privilege.gif"),
//	/** The RDF image. **/
//	RDF("rdf.png"),
//	/** The refinable item image. **/
//	REFINABLE_ITEM("item.png"),
//	/** The reset image. **/
//	RESET("reset.gif"),
//	/** The root image. **/
//	ROOT("root.gif"),
//	/** The TBox phase image. **/
//	TBOX_PHASE("tbox.gif"),
//	/** The unchecked image. **/
//	UNCHECKED("unchecked.gif"),
//	/** The network image. **/
//	NETWORK("network.gif"),
//	/** The node image. **/
//	NODE("node.gif"),
//	/** The unknown image. **/
//	UNKNOWN("unknown.gif"),
//	/** The refinable items selection image. **/
//	REFINABLE_ITEMS_SELECTION("itemsselection.gif"),
//	/** The model image. **/
//	MODEL("model.gif"),
//	/** The new image. **/
//	NEW("new.gif"),
//	/** The delete image. **/
//	DELETE("delete.gif"),
//	/** The clone image. **/
//	CLONE("clone.gif"),
//	/** The edit image. **/
//	EDIT("edit.gif"),
//	/** The ok image. **/
//	OK("ok.gif");

	/**
	 * Create an image.
	 * @param fileName
	 *            The image file name.
	 **/
	private Figure(String fileName)
	{
		address = "platform:/plugin/" + Activator.getDefault().getName() + "/icons/" + fileName;
	}

	/**
	 * Load the image.
	 * @return The image or an empty image is something went wrong.
	 **/
	public Image load()
	{
		try
		{
			URL url = new URL(address);
			return new Image(Display.getDefault(), url.openConnection().getInputStream());
		}
		catch (Exception e)
		{
			return new Image(Display.getDefault(), 1, 1);
		}
	}

	/**
	 * Load the image with an error decoration.
	 * @return The image or an empty image is something went wrong.
	 **/
	public Image loadError()
	{
		return loadDecorated("error.gif");
	}

	/**
	 * Load the image with a warning decoration.
	 * @return The image or an empty image is something went wrong.
	 **/
	public Image loadWarning()
	{
		return loadDecorated("warning.gif");
	}

	/**
	 * Load a decorated image.
	 * @param fileName
	 *            The filename of the decorator.
	 * @return The image or an empty image is something went wrong.
	 **/
	private Image loadDecorated(String fileName)
	{
		try
		{
			String address = "platform:/plugin/" + Activator.getDefault().getName() + "/icons/" + fileName;
			final URL url = new URL(address);
			CompositeImageDescriptor compositeImageDescriptor = new CompositeImageDescriptor()
			{
				@Override
				protected void drawCompositeImage(int width, int height)
				{
					drawImage(base.getImageData(), 0, 0);
					drawImage(over.getImageData(), 0, base.getBounds().height - over.getBounds().height);
				}

				@Override
				protected Point getSize()
				{
					return new Point(base.getBounds().width, base.getBounds().height);
				}

				private Image base = load();
				private Image over = new Image(Display.getDefault(), url.openConnection().getInputStream());
			};

			return compositeImageDescriptor.createImage();
		}
		catch (Exception e)
		{
			return new Image(Display.getDefault(), 1, 1);
		}
	}

	/** The image bundle address. **/
	private String address;
}
