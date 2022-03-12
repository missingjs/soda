package soda.groovy.web

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

}
