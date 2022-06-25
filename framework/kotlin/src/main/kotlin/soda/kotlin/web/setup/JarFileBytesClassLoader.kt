package soda.kotlin.web.setup

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.jar.JarInputStream

class JarFileBytesClassLoader(bytes: ByteArray, parent: ClassLoader): ClassLoader(parent) {

    private val classMap = mutableMapOf<String, ByteArray>()

    init {
        val buffer = ByteArray(1024)
        JarInputStream(ByteArrayInputStream(bytes)).use {
            while (true) {
                val entry = it.nextJarEntry ?: break
                val name = entry.realName
                if (name.endsWith(".class")) {
                    val className = name.replace(".class", "").replace("/", ".")
                    val bout = ByteArrayOutputStream()
                    while (true) {
                        val readSize = it.read(buffer, 0, buffer.size)
                        if (readSize == -1) {
                            break
                        }
                        bout.write(buffer, 0, readSize)
                    }
                    classMap[className] = bout.toByteArray()
                }
            }
        }
    }

    override fun findClass(name: String?): Class<*> {
        return classMap[name]?.let {
            defineClass(name, it, 0, it.size)
        } ?: super.findClass(name)
    }

}