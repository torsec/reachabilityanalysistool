/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package it.polito.policytool.handler;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytoollib.policy.impl.Policy;
import it.polito.policytoollib.wrapper.XMLTranslator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.core.commands.AbstractHandler;

public class SaveHandler extends AbstractHandler {

	private final Logger	LOGGER	= Logger.getLogger(SaveHandler.class.getName());

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.open();

		String tempDir = dialog.getFileName() + ".tmp";
		File tempDirFile = new File(tempDir);

		// if the directory does not exist, create it
		if (tempDirFile.exists()) {
			LOGGER.info(tempDir + " existes");
			deleteFolder(tempDirFile);
			LOGGER.info(tempDir + " delete");
		}

		LOGGER.info("creating directory: " + tempDir);

		if (tempDirFile.mkdir()) {
			LOGGER.info("DIR created");
		}

		XMLTranslator xml = new XMLTranslator(PolicyToolUIModel.getInstance().getSelectorTypes());

		LinkedList<String> project_dir = new LinkedList<String>();
		
		File vpnDirFile = new File(tempDir + "/secure/");

		LOGGER.info("creating directory: " + tempDir + "/secure/");

		if (vpnDirFile.mkdir()) {
			LOGGER.info("DIR created");
		}

		for (Policy p : PolicyToolUIModel.getInstance().getVPNPolicyList()) {
			project_dir.add("secure/" + p.getName() + ".xml");
			try {
				xml.writeOriginalRuleSetFromPolicy(p, tempDir + "/secure/" + p.getName() + ".xml");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		File natDirFile = new File(tempDir + "/nat/");

		LOGGER.info("creating directory: " + tempDir + "/nat/");

		if (natDirFile.mkdir()) {
			LOGGER.info("DIR created");
		}

		for (Policy p : PolicyToolUIModel.getInstance().getNATPolicyList()) {
			project_dir.add("nat/" + p.getName() + ".xml");
			try {
				xml.writeOriginalRuleSetFromPolicy(p, tempDir + "/nat/" + p.getName() + ".xml");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		File policyDirFile = new File(tempDir + "/policy/");

		LOGGER.info("creating directory: " + tempDir + "/policy/");

		if (policyDirFile.mkdir()) {
			LOGGER.info("DIR created");
		}

		for (Policy p : PolicyToolUIModel.getInstance().getPolicyList()) {
			project_dir.add("policy/" + p.getName() + ".xml");
			try {
				xml.writeOriginalRuleSetFromPolicy(p, tempDir + "/policy/" + p.getName() + ".xml");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		
		project_dir.add("landscape.xml");
		try {
			xml.writeLandscape(PolicyToolUIModel.getInstance().getLandscape(), tempDir + "/landscape.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		project_dir.add("selectorTypes.xml");
		try {
			xml.writeSelectorTypes(PolicyToolUIModel.getInstance().getSelectorTypes(), tempDir + "/selectorTypes.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		ZipOutputStream zipOut;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(dialog.getFileName()));

			for (String entryName : project_dir) {
				try {
					zipOut.putNextEntry(new ZipEntry(entryName));

					FileInputStream in = new FileInputStream(tempDir + "/" + entryName);
					byte[] buffer = new byte[1024];
					int len;
					while ((len = in.read(buffer)) > 0) {
						zipOut.write(buffer, 0, len);
					}

					in.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
				zipOut.closeEntry();
			}

			zipOut.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (tempDirFile.exists()) {
			LOGGER.info("Delete " + tempDir);
			deleteFolder(tempDirFile);
		}

		return null;

	}

	private void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}
}
