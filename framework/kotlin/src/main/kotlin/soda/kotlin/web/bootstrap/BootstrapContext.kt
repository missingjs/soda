package soda.kotlin.web.bootstrap

import soda.kotlin.web.WebUtils

class BootstrapContext(private val key: String, artifact: ByteArray) {

    private var md5: String

    private var classLoader: ClassLoader

    init {
        md5 = WebUtils.md5Hex(artifact)
        val parentLoader = Thread.currentThread().contextClassLoader
        classLoader = JarFileBytesClassLoader(artifact, parentLoader)
    }

    fun getKey() = key

    fun getMd5() = md5

    fun getClassLoader() = classLoader

}