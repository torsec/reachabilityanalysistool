/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package it.polito.policytool.handler;

import it.polito.policytool.ui.wizard.ExecuteDistributedAnalysis.ExecuteDistributedAnalysisWizard;
import it.polito.policytool.ui.wizard.ExecuteQuery.ExecuteQueryWizard;

import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;

public class ExecuteDistributedAnalysisHandler extends AbstractHandler{
	
	private final Logger	LOGGER	= Logger.getLogger(ExecuteDistributedAnalysisHandler.class.getName());

	@Override
	  public Object execute(ExecutionEvent event) throws  ExecutionException{

		LOGGER.info("Execute Analisys Handler");
		
		 WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(),
			      new ExecuteDistributedAnalysisWizard());
			    if (wizardDialog.open() == Window.OK) {
			    	
			      
			    } else {
			      System.out.println("Cancel pressed");
			       }
		
		return null;
		
	}
}