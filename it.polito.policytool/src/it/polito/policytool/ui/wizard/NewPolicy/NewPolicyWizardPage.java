package it.polito.policytool.ui.wizard.NewPolicy;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.util.ClassList;
import it.polito.policytoollib.policy.resolution.ResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.ATPResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.DTPResolutionStrategy;
import it.polito.policytoollib.policy.resolution.impl.FMRResolutionStrategy;
import it.polito.policytoollib.policy.utils.PolicyType;
import it.polito.policytoollib.rule.action.Action;
import it.polito.policytoollib.rule.action.FilteringAction;
import it.polito.policytoollib.rule.selector.Selector;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewPolicyWizardPage extends WizardPage {

	private final Logger						LOGGER	= Logger.getLogger(NewPolicyWizardPage.class.getName());

	private Composite									container;
	private Label										policyNameLabel;
	private Text										policyName;
	private Label										defaultActionLabel;
	private Combo										defaultAction;
	private Label										resolutionStrategyLabel;
	private Combo										resolutionStrategy;
	private Vector<ResolutionStrategy>	RSSet;
	private Action[]									ActionSet;
	private PolicyType									policyType;

	protected NewPolicyWizardPage(String pageName, PolicyType policyType) {
		super(pageName);
		setTitle("First Page");
		setDescription("Fake Wizard. First page");
		this.policyType = policyType;
		RSSet = new Vector<ResolutionStrategy>();
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		policyNameLabel = new Label(container, SWT.NONE);
		policyNameLabel.setText("Policy Name : ");

		policyName = new Text(container, SWT.BORDER | SWT.SINGLE);
		policyName.setText("");
		policyName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (policyType == PolicyType.FILTERING) {
					if (!policyName.getText().isEmpty() && defaultAction.getSelectionIndex() != -1 && resolutionStrategy.getSelectionIndex() != -1) {
						setPageComplete(true);
					} else {
						setPageComplete(false);
					}
				} else {
					if (!policyName.getText().isEmpty()) {
						setPageComplete(true);
					} else {
						setPageComplete(false);
					}
				}
			}

		});
		policyName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (policyType == PolicyType.FILTERING) {

			defaultActionLabel = new Label(container, SWT.NONE);
			defaultActionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			defaultActionLabel.setText("Default Action : ");

			defaultAction = new Combo(container, SWT.NONE);
			defaultAction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			defaultAction.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (!policyName.getText().isEmpty() && defaultAction.getSelectionIndex() != -1 && resolutionStrategy.getSelectionIndex() != -1) {
						setPageComplete(true);
					} else {
						setPageComplete(false);
					}
				}
			});

			ActionSet = new Action[2];
			defaultAction.add("ALLOW");
			ActionSet[0] = FilteringAction.ALLOW;
			defaultAction.add("DENY");
			ActionSet[1] = FilteringAction.DENY;

			resolutionStrategyLabel = new Label(container, SWT.NONE);
			resolutionStrategyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			resolutionStrategyLabel.setText("Resolution Strategy : ");

			resolutionStrategy = new Combo(container, SWT.NONE);
			resolutionStrategy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			resolutionStrategy.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (!policyName.getText().isEmpty() && defaultAction.getSelectionIndex() != -1 && resolutionStrategy.getSelectionIndex() != -1) {
						setPageComplete(true);
					} else {
						setPageComplete(false);
					}
				}
			});

			/*RSSet = new GenericConflictResolutionStrategy[3];
			resolutionStrategy.add("FMRResolutionStrategy");
			resolutionStrategy.add("ATPResolutionStrategy");
			resolutionStrategy.add("DTPResolutionStrategy");
			RSSet[0] = new FMRResolutionStrategy();*/
			
			//TODO prima dell'esportazione ripristinare la ricerca nella cartella dell'applicazione, per debug diamo cartella codice
			String classpath = System.getProperty("java.class.path");
			File f = new File(classpath);
			System.out.println(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
			File dir = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
			System.out.println(dir.getAbsolutePath());
			File[] files = dir.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().startsWith("it.polito.policytoollib");//name.toLowerCase().endsWith(".txt");
			    }
			});
			try {
				Set<Class<?>> classList = ClassList.getFromJARFile(files[0].getAbsolutePath(), "it/polito/policytoollib/policy/resolution/impl");
				//Set<Class<?>> classList = ClassList.getFromDirectory(new File("/home/leonardo/policy-tool/it.polito.policytoollib/bin/it/polito/policytoollib/policy/resolution/impl/"), "it.polito.policytoollib.policy.resolution.impl");
				for(Class c : classList)
				{
					System.out.println(c);
					if(c.isEnum()) continue;
					if(Modifier.isAbstract(c.getModifiers())) continue;
					Constructor resConstructor = null;
					try
					{
						resConstructor = c.getConstructor(new Class[0]);
						Object resolutionStrategyObject = resConstructor.newInstance(new Object[0]);
						if(resolutionStrategyObject instanceof ResolutionStrategy)
						{	
							RSSet.addElement((ResolutionStrategy)resolutionStrategyObject);
							resolutionStrategy.add(((ResolutionStrategy)resolutionStrategyObject).toString());
						}
					}
					catch(NoSuchMethodException e2)
					{
						continue;
					}
				}
			} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException/* | IOException */e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		setControl(container);
		setPageComplete(false);
	}

	public String getPolicyName() {
		return policyName.getText();
	}

	public Action getDefaultAction() {
		return ActionSet[defaultAction.getSelectionIndex()];
	}

	public ResolutionStrategy getResolutionStartegy() {
		//String className = resolutionStrategy.getText();
		
		return RSSet.get(resolutionStrategy.getSelectionIndex()).cloneResolutionStrategy();
	}
}
