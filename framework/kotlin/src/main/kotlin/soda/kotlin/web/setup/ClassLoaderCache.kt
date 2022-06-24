package soda.kotlin.web.setup

object ClassLoaderCache {

    private val loaderMap = mutableMapOf<String, ClassLoader>()

    @Synchronized
    fun remove(path: String) {
        loaderMap.remove(path)
    }

    @Synchronized
    fun setupForJar(key: String, jarBytes: ByteArray) {
        val parent = Thread.currentThread().contextClassLoader
        loaderMap[key] = JarFileBytesClassLoader(jarBytes, parent)
    }

    @Synchronized
    fun getForJar(key: String): ClassLoader? {
        return loaderMap[key]
    }

}