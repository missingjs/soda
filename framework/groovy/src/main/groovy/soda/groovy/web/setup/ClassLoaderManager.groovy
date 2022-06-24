package soda.groovy.web.setup

import soda.groovy.web.Logger

import java.nio.charset.StandardCharsets

class ClassLoaderManager {

    private Map<String, ClassLoader> loaderMap = [:]

    synchronized void remove(String path) {
        loaderMap.remove(path)
    }

    synchronized void setupForScript(String key, byte[] fileBytes) {
        def parent = ClassLoaderManager.class.classLoader
        def loader = new GroovyClassLoader(parent)
        loader.parseClass(new String(fileBytes, StandardCharsets.UTF_8))
        loaderMap[key] = loader
    }

    synchronized ClassLoader getForScript(String key) {
        return loaderMap[key]
    }

}
