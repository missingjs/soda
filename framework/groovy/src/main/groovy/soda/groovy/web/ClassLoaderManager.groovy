package soda.groovy.web

class ClassLoaderManager {

    private Map<String, ClassLoader> loaderMap = [:]

    synchronized void set(String path) throws MalformedURLException {
        File file = new File(path)
        ClassLoader parent = ClassLoaderManager.class.classLoader
        URLClassLoader loader = new URLClassLoader(new URL[] {file.toURI().toURL()}, parent)
        loaderMap[path] = loader
        Logger.info("new class loader for $path: $loader")
    }

//    synchronized ClassLoader get(String path) {
//        if (!loaderMap.containsKey(path)) {
//            try {
//                set(path)
//            } catch (MalformedURLException ex) {
//                Logger.exception("invalid path: $path", ex)
//            }
//        }
//        loaderMap[path]
//    }

    synchronized void remove(String path) {
        loaderMap.remove(path)
    }

    synchronized void setupForScript(String key, String scriptFile) {
        def parent = ClassLoaderManager.class.classLoader
        def fileObj = new File(scriptFile)
        def parentDir = fileObj.parent
        def loader = new URLClassLoader(new URL[] {fileObj.parentFile.toURI().toURL()}, parent)
        def gcl = new GroovyClassLoader(loader)
        gcl.parseClass(fileObj)
        loaderMap[key] = gcl
        Logger.info("new class loader for $key: $loader")
    }

    synchronized ClassLoader getForScript(String key) {
        return loaderMap[key]
    }

}
