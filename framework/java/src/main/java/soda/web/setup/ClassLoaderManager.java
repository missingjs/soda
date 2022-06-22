package soda.web.setup;

import soda.web.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClassLoaderManager {

	private Map<String, ClassLoader> loaderMap = new HashMap<>();

	public synchronized void remove(String path) {
		loaderMap.remove(path);
	}

	public synchronized void setupForJar(String key, byte[] jarBytes) throws IOException {
		var parent = ClassLoaderManager.class.getClassLoader();
		var loader = new JarFileBytesClassLoader(jarBytes, parent);
		loaderMap.put(key, loader);
		Logger.info(String.format("new class loader for %s", key));
	}

	public synchronized ClassLoader getForJar(String key) {
		return loaderMap.get(key);
	}
	
}
