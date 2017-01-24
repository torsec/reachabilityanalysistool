package it.polito.policytool;

import it.polito.policytool.handler.ExecuteQueryHandler;
import it.polito.policytool.handler.SaveHandler;
import it.polito.policytool.ui.view.LandscapeEditor.NetworkContentProvider;
import it.polito.policytool.ui.wizard.ExecuteQuery.ExecuteQueryWizardPage;
import it.polito.policytool.ui.wizard.NewPolicy.NewPolicyWizardPage;

import java.util.logging.Level;
import java.util.logging.Logger;

import jodd.Jodd;
import jodd.bean.JoddBean;
import jodd.core.JoddCore;
import jodd.upload.JoddUpload;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "it.polito.policytool"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		
		Logger LOGGER_SaveHandler = Logger.getLogger(SaveHandler.class.getName());
		LOGGER_SaveHandler.setLevel(Level.SEVERE);
		
		Logger LOGGER_ExecuteQueryWizardPage = Logger.getLogger(ExecuteQueryWizardPage.class.getName());
		LOGGER_ExecuteQueryWizardPage.setLevel(Level.SEVERE);
		
		Logger LOGGER_NewPolicyWizardPage = Logger.getLogger(NewPolicyWizardPage.class.getName());
		LOGGER_NewPolicyWizardPage.setLevel(Level.SEVERE);
		
		Logger LOGGER_NetworkContentProvider = Logger.getLogger(NetworkContentProvider.class.getName());
		LOGGER_NetworkContentProvider.setLevel(Level.INFO);
		
		Logger LOGGER_ExecuteQuerryHandler = Logger.getLogger(ExecuteQueryHandler.class.getName());
		LOGGER_ExecuteQuerryHandler.setLevel(Level.INFO);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		JoddCore.init();
        JoddBean.init();
        JoddUpload.init();

        System.out.println(Jodd.isModuleLoaded(Jodd.CORE));
        System.out.println(Jodd.isModuleLoaded(Jodd.BEAN));
        System.out.println(Jodd.isModuleLoaded(Jodd.UPLOAD));
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/**
	 * Retrieve the plugin name.
	 * @return The plugin name.
	 **/
	public String getName()
	{
		return plugin.getBundle().getSymbolicName();
	}

}
