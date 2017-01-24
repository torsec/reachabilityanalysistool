package it.polito.policytool.ui.wizard.NewSelectorType;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import it.polito.policytool.PolicyToolUIModel;
import it.polito.policytool.ui.wizard.ModifyRule.ModifyRuleExactMatchWizard;
import it.polito.policytoollib.util.ClassList;
import it.polito.policytoollib.exception.rule.IncompatibleSelectorException;
import it.polito.policytoollib.rule.selector.Selector;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class NewSelectorTypeWizardPage extends WizardPage {

	private final Logger	LOGGER	= Logger.getLogger(NewSelectorTypeWizardPage.class.getName());

	private Composite					container;
	private Label						selectorTypeNameLabel;
	private Text						selectorTypeName;
	private Label						selectorTypeLabel;
	private Combo						selectorType;
	//private Selector[]				selectorTypeSet;
	private Vector<Selector>			selectorTypeSet;
	private String 						selectorName;
	

	protected NewSelectorTypeWizardPage(String pageName, String selectorName) {
		super(pageName);
		setTitle(pageName);
		setDescription(pageName);
		this.selectorName=selectorName;
		selectorTypeSet = new Vector<Selector>();
		
	}

	@Override
	public void createControl(Composite parent) {
		
		container = new Composite(parent, SWT.NO);
		
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		selectorTypeNameLabel = new Label(container, SWT.NONE);
		selectorTypeNameLabel.setText("SelectorType Name : ");

		selectorTypeName = new Text(container, SWT.BORDER | SWT.SINGLE);
		if(selectorName!=null)
			selectorTypeName.setText(selectorName);
		else
			selectorTypeName.setText("");
		selectorTypeName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!selectorTypeName.getText().isEmpty() && selectorType.getSelectionIndex() != -1) {
					setPageComplete(true);
				} else {
					setPageComplete(false);
				}
			}

		});
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan=2;
		selectorTypeName.setLayoutData(data);;
		
		selectorTypeLabel = new Label(container, SWT.NONE);
		selectorTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		selectorTypeLabel.setText("SelectorType : ");

		selectorType = new Combo(container, SWT.NONE);
		selectorType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		selectorType.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!selectorTypeName.getText().isEmpty() && selectorType.getSelectionIndex() != -1) {
					setPageComplete(true);
				} else {
					setPageComplete(false);
				}
			}
		});
		
		final Composite finalParent = parent;
		Button load_selector = new Button(container, SWT.PUSH | SWT.BORDER | SWT.SINGLE);
		load_selector.setText("Load Selector Java Source "); 
		load_selector.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
				if(compiler == null)
				{
					MessageBox messageBox = new MessageBox(finalParent.getShell(), SWT.ICON_WARNING | SWT.OK);
				    messageBox.setText("Error");
			        messageBox.setMessage("A java compiler installed on the system is needed to compile at runtime a custom selector from java source");
			        int buttonID = messageBox.open();
			        switch(buttonID) {
			          case SWT.YES:
			        	  return;
			        }
				}
				else
				{
					//Load java source file
					System.out.println("Start compilation");
					FileDialog dialog = new FileDialog(finalParent.getShell(),SWT.OPEN);
					if(dialog.open()==null) return;
					File[] files = new File[1];
					files[0] = new File(dialog.getFilterPath()+"/"+dialog.getFileName());
				    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
				    Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
				    List<String> optionList = new ArrayList<String>();
				    // set compiler's classpath to be same as the runtime's
				    //TODO: prima dell'esportazione ripristinare la ricerca nella cartella dell'applicazione, per debug diamo cartella codice 
				    String classpath = System.getProperty("java.class.path");
					File f = new File(classpath);
					File dir = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
					System.out.println(dir.getAbsolutePath());
					File[] files2 = dir.listFiles(new FilenameFilter() {
					    public boolean accept(File dir, String name) {
					        return name.toLowerCase().startsWith("it.polito.policytoollib");//name.toLowerCase().endsWith(".txt");
					    }
					});
					/*try {
						Set<Class<?>> classList = ClassList.getFromJARFile(files2[0].getAbsolutePath(), "it/polito/policytoollib/rule/selector/impl");
					} catch (ClassNotFoundException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}*/
					System.out.println();
					System.out.println(files2[0].getAbsolutePath());
					System.out.println();
				    File dir2 = new File((files2[0].getAbsolutePath().substring(0,files2[0].getAbsolutePath().lastIndexOf(File.separatorChar)))
							.substring(0,files2[0].getAbsolutePath().lastIndexOf(File.separatorChar))+"/user/selectors/");
				    dir2.mkdirs();
				    optionList.addAll(Arrays.asList("-classpath",files2[0].getAbsolutePath()));
				    optionList.addAll(Arrays.asList("-d",dir2.getAbsolutePath()));
//				    optionList.addAll(Arrays.asList("-classpath","/home/leonardo/policy-tool/it.polito.policytoollib/bin"));
//				    optionList.addAll(Arrays.asList("-d","/home/leonardo/policy-tool/user/selectors"));
				    // Compile source file.
				    FileWriter compilationLog = null;
					try {
						compilationLog = new FileWriter("customSelectorCompilation.log");
					} catch (IOException e2) {
						MessageBox messageBox = new MessageBox(finalParent.getShell(), SWT.ICON_WARNING | SWT.OK);
				        
				        messageBox.setText("Error");
				        messageBox.setMessage("Cannot open or create compilation log file");
				        int buttonID = messageBox.open();
				        switch(buttonID) {
				          case SWT.YES:
				        	  return;
				        }
					}
				    compiler.getTask(compilationLog, fileManager, null, optionList, null, compilationUnit).call();
					try {
						fileManager.close();
					} catch (IOException e1) {
						MessageBox messageBox = new MessageBox(finalParent.getShell(), SWT.ICON_WARNING | SWT.OK);
				        
				        messageBox.setText("Error");
				        messageBox.setMessage("Cannot close file manager");
				        int buttonID = messageBox.open();
				        switch(buttonID) {
				          case SWT.YES:
				        	  return;
				        }
					}
					
					
					//compiler.run(System.in, System.out, System.err, "-cp /home/leonardo/policy-tool/it.polito.policytoollib/bin" ,(new File(dialog.getFilterPath()+"/"+dialog.getFileName())).getPath());
				
					// Load and instantiate compiled class.
					/*URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
					Class<?> cls = Class.forName("test.Test", true, classLoader); // Should print "hello".
					Selector instance = (Selector) cls.newInstance(); // Should print "world".
					System.out.println(instance); // Should print "test.Test@hashcode".*/
				}
				refreshCombo();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});		
		
		refreshCombo();
		Selector s = PolicyToolUIModel.getInstance().getSelectorTypes().getAllSelectorTypes().get(selectorName);
		if(s!=null)
			for(int i=0; i<selectorTypeSet.size(); i++)
				if(selectorTypeSet.get(i).getName().equals(s.getName()))
				{
					selectorType.select(i);
					break;
				}
				
		container.layout();
		container.getShell().setSize(container.computeSize(selectorTypeLabel.getBounds().width*5, selectorTypeLabel.getBounds().height*18));
		setControl(container);
		setPageComplete(false);
	}

	public String getSelectorTypeName() {
		return selectorTypeName.getText();
	}

	public Selector getSelectorType() {
		return selectorTypeSet.get(selectorType.getSelectionIndex()).selectorClone();
	}
	
	public void refreshCombo(){
		selectorType.removeAll();
		try {
			//TODO prima dell'esportazione ripristinare la ricerca nella cartella dell'applicazione, per debug diamo cartella codice
			String classpath = System.getProperty("java.class.path");
			File f = new File(classpath);
			//System.out.println(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
			File dir = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
			//System.out.println(dir.getAbsolutePath());
			File[] files = dir.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().startsWith("it.polito.policytoollib");//name.toLowerCase().endsWith(".txt");
			    }
			});
			
			//PolicyToolUIModel.getInstance().setClasses(ClassList.getFromJARFile(files[0].getAbsolutePath(), "it/polito/policytoollib/rule/selector/impl", PolicyToolUIModel.getInstance().getClasses()));
			System.out.println("files[0].getAbsolutePath()  "+files[0].getAbsolutePath());
			//TODO files[0].getAbsolutePath()=<cartella principale>/main/plugins
			//TODO verificare substring della riga sotto per far andare nella cartella main cioè la prima parent (potrebbe essere penultimo index e non ultimo di '/')
			//D:\Desktop\export_test\eclipse\plugins\it.polito.policytoollib_0.0.1.jar
			PolicyToolUIModel.getInstance().setUserClasses
				(ClassList.getRawFromDirectory
					(new File((files[0].getAbsolutePath().substring(0,files[0].getAbsolutePath().lastIndexOf(File.separatorChar)))
								.substring(0,files[0].getAbsolutePath().lastIndexOf(File.separatorChar))
							+"/user/selectors/it/polito/policytoollib/rule/selector/impl/"),
					"it.polito.policytoollib.rule.selector.impl",
					PolicyToolUIModel.getInstance().getUserClasses()));
			//PolicyToolUIModel.getInstance().setUserClasses(ClassList.getRawFromDirectory(new File("/home/leonardo/policy-tool/user/selectors/it/polito/policytoollib/rule/selector/impl/"), "it.polito.policytoollib.rule.selector.impl", PolicyToolUIModel.getInstance().getUserClasses()));
			//PolicyToolUIModel.getInstance().setClasses(ClassList.getFromDirectory(new File("/home/leonardo/policy-tool/it.polito.policytoollib/bin/it/polito/policytoollib/rule/selector/impl/"), "it.polito.policytoollib.rule.selector.impl", PolicyToolUIModel.getInstance().getClasses()));
			//PolicyToolUIModel.getInstance().setUserClasses(ClassList.getRawFromDirectory(new File("D:/Documents/policy-tool/user/selectors/it/polito/policytoollib/rule/selector/impl/"), "it.polito.policytoollib.rule.selector.impl", PolicyToolUIModel.getInstance().getUserClasses()));
			//PolicyToolUIModel.getInstance().setClasses(ClassList.getFromDirectory(new File("D:/Documents/policy-tool/it.polito.policytoollib/bin/it/polito/policytoollib/rule/selector/impl/"), "it.polito.policytoollib.rule.selector.impl", PolicyToolUIModel.getInstance().getClasses()));
			
			//classList.addAll(ClassList.getFromDirectory(new File("/home/leonardo/policy-tool/user/selectors/it/polito/policytoollib/rule/selector/impl/"), "it.polito.policytoollib.rule.selector.impl"));
			HashMap<String,Class<?>> allClasses = new HashMap<String,Class<?>>();
			//allClasses.addAll(PolicyToolUIModel.getInstance().getClasses().values());
			//allClasses.addAll(PolicyToolUIModel.getInstance().getUserClasses().values());
			allClasses.putAll(PolicyToolUIModel.getInstance().getUserClasses());
			allClasses.putAll(PolicyToolUIModel.getInstance().getClasses());
			for(Class<?> c : allClasses.values())
			{
				System.out.println(c);
				if(c.isEnum()) continue;
				if(Modifier.isAbstract(c.getModifiers())) continue;
				Constructor resConstructor = null;
				int constructorNumberOfArguments = 0;
				Object s2 = null;
				try{
					resConstructor = c.getConstructor(new Class[0]);
				}
				catch(NoSuchMethodException e)
				{
					for(Constructor constructor : c.getConstructors())
					{
						constructorNumberOfArguments = constructor.getParameterTypes().length;
						try{
							s2 = constructor.newInstance(new Object[constructorNumberOfArguments]);
							break;
						}
						catch(Exception e2)
						{
							continue;
						}
					}
				}
				try{
					if(s2 == null)
						s2 = resConstructor.newInstance(new Object[constructorNumberOfArguments]);
					if(s2 instanceof Selector)
					{	
						selectorTypeSet.addElement((Selector)s2);
						selectorType.add(((Selector)s2).getName());
					}
				}
				catch(Exception e)
				{
					//TODO messagebox: sta classe non si può importare!!!
					e.printStackTrace();
				}
			}
			//TODO spezzare in due tra classlist e classlist add all per fare in modo che se uno importa una roba che non implementa selector bisogna cancellare il file compilato e avvisarlo che deve creare una classe che implementi l'interfaccia selector
		} catch (SecurityException | IllegalArgumentException | ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
