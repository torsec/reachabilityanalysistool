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

import it.polito.policytool.ui.wizard.ExecuteQuery.ExecuteQueryWizard;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;

public class ExecuteQueryHandler extends AbstractHandler{
	
	private final Logger	LOGGER	= Logger.getLogger(ExecuteQueryHandler.class.getName());

	@Override
	  public Object execute(ExecutionEvent event) throws  ExecutionException{

		LOGGER.info("Execute Query Handler");
		
		 WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell().getShell(),
			      new ExecuteQueryWizard());
			    if (wizardDialog.open() == Window.OK) {
			    	
			      
			    } else {
			      System.out.println("Cancel pressed");
			       }
		
		return null;
	}
}
