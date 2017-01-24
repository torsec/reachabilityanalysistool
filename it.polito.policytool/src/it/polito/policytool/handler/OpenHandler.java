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

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytoollib.exception.policy.NoExternalDataException;
import it.polito.policytoollib.model.PolicyAnalysisModel;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.core.commands.AbstractHandler;

public class OpenHandler extends AbstractHandler{

	@Override
	  public Object execute(ExecutionEvent event) throws  ExecutionException{
		Shell shell = HandlerUtil.getActiveShell(event);
		FileDialog dialog = new FileDialog(shell,SWT.OPEN);
		if(dialog.open()==null) return null;
		
		
		
		PolicyAnalysisModel model = null;
		
		try {
			model = new PolicyAnalysisModel(dialog.getFilterPath()+"/"+dialog.getFileName(),dialog.getFileName());
			//eventBroker.send("updateProjectExplorer",model);
			PolicyToolUIModel.getInstance().setPolicyToolModel(model);
		} catch (IOException e) {
			MessageBox message = new MessageBox(shell); 
			message.setMessage("Error in opening file IO");
			message.open();
			e.printStackTrace();
		} catch (NoExternalDataException e) {
			MessageBox message = new MessageBox(shell); 
			message.setMessage("Error in opening file External Data");
			message.open();
			e.printStackTrace();
		}catch (Exception e) {
			MessageBox message = new MessageBox(shell); 
			message.setMessage("Error in opening file Ex");
			message.open();
			e.printStackTrace();
		}
		
		
		return model;
			
		
		
//			logger.info("info test");
//			logger.error("error test");
//			logger.debug("debug test");
//			logger.warn("warn test");
		
	}
}
