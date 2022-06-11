package soda.kotlin.web

import java.net.URL
import java.net.URLClassLoader

object ClassLoaderCache {

    private val loaderMap = mutableMapOf<String, ClassLoader>()

//    @Synchronized
//    fun set(path: String) {
//        val parent = Thread.currentThread().contextClassLoader
//        val loader = URLClassLoader(arrayOf(File(path).toURI().toURL()), parent)
//        loaderMap[path] = loader
//        Logger.info("new class loader for $path: $loader")
//    }

    @Synchronized
    fun remove(path: String) {
        loaderMap.remove(path)
    }

    @Synchronized
    fun setupForJar(key: String, jarFile: String) {
        val parent = Thread.currentThread().contextClassLoader
        val loader = URLClassLoader(arrayOf(URL("file:$jarFile")), parent)
        loaderMap[key] = loader
        Logger.info("new class loader for $key: $loader")
    }

    @Synchronized
    fun getForJar(key: String): ClassLoader? {
        return loaderMap[key]
    }

}