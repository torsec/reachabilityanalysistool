package it.polito.policytool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassList {

	public static void main(String[] args) throws Exception {
		File f = new File("prova123");
		FileWriter fw = new FileWriter(f);
		for (Class c : getClasses("it.polito.policytoollib.policy.resolution.impl")) {
			fw.write(c.getName()+"\n");
		}
		System.out.println(f.getAbsolutePath());
		fw.flush();
	}

	public static Set<Class<?>> getClasses(String packageName)
			throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return getClasses(loader, packageName);
	}

	private static Set<Class<?>> getClasses(ClassLoader loader,
			String packageName) throws IOException, ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = loader.getResources(path);
		if (resources != null) {
			while (resources.hasMoreElements()) {
				String filePath = resources.nextElement().getFile();
				// WINDOWS HACK
				if (filePath.indexOf("%20") > 0)
					filePath = filePath.replaceAll("%20", " ");
				if (filePath != null) {
					if ((filePath.indexOf("!") > 0)
							&& (filePath.indexOf(".jar") > 0)) {
						String jarPath = filePath.substring(0,
								filePath.indexOf("!")).substring(
								filePath.indexOf(":") + 1);
						// WINDOWS HACK
						if (jarPath.indexOf(":") >= 0)
							jarPath = jarPath.substring(1);
						classes.addAll(getFromJARFile(jarPath, path));
					} else {
						classes.addAll(getFromDirectory(new File(filePath),
								packageName));
					}
				}
			}
		}
		return classes;
	}

	public static Set<Class<?>> getFromDirectory(File directory,
			String packageName) throws ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		if (directory.exists()) {
			for (String file : directory.list()) {
				if (file.endsWith(".class")) {
					String name = packageName + '.'
							+ stripFilenameExtension(file);
					Class<?> clazz = Class.forName(name);
					classes.add(clazz);
				}
			}
		}
		return classes;
	}

	public static Set<Class<?>> getFromJARFile(String jar, String packageName)
			throws IOException, ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		JarInputStream jarFile = new JarInputStream(new FileInputStream(jar));
		JarEntry jarEntry;
		do {
			jarEntry = jarFile.getNextJarEntry();
			if (jarEntry != null) {
				String className = jarEntry.getName();
				if (className.endsWith(".class")) {
					className = stripFilenameExtension(className);
					System.out.println(className);
					if (className.startsWith(packageName))
					{
						System.out.println(className.replace('/', '.'));
						classes.add(Class.forName(className.replace('/', '.')));
					}
				}
			}
		} while (jarEntry != null);
		return classes;
	}

	private static String stripFilenameExtension(String filename) {
		int i = filename.lastIndexOf('.');
		if (i > 0) {
			return filename.substring(0, i);
		} else {
			return filename;
		}
	}
}
