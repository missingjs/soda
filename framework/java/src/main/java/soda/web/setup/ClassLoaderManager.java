package soda.web.setup;

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
	}

	public synchronized ClassLoader getForJar(String key) {
		return loaderMap.get(key);
	}
	
}
