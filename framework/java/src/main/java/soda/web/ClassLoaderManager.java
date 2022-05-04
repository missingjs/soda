package soda.web;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ClassLoaderManager {

	private Map<String, ClassLoader> loaderMap = new HashMap<>();
	
	public synchronized void set(String path) throws MalformedURLException {
		File file = new File(path);
		ClassLoader parent = ClassLoaderManager.class.getClassLoader();
    	URLClassLoader loader = new URLClassLoader(new URL[] {file.toURI().toURL()}, parent);
    	loaderMap.put(path, loader);
		Logger.info(String.format("new class loader for %s: %s", path, loader));
	}
	
	public synchronized ClassLoader get(String path) {
		if (!loaderMap.containsKey(path)) {
			try {
				set(path);
			} catch (MalformedURLException ex) {
				Logger.exception("invalid path: " + path, ex);
			}
		}
		return loaderMap.get(path);
	}

	public synchronized void remove(String path) {
		loaderMap.remove(path);
	}

	public synchronized void setupForJar(String key, String jarFile) throws MalformedURLException {
		ClassLoader parent = ClassLoaderManager.class.getClassLoader();
		URLClassLoader loader = new URLClassLoader(new URL[] {new URL("file:" + jarFile)}, parent);
		loaderMap.put(key, loader);
		Logger.info(String.format("new class loader for %s: %s", key, loader));
	}

	public synchronized ClassLoader getForJar(String key) {
		return loaderMap.get(key);
	}
	
}
