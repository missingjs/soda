package soda.scala.web.setup

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.jar.{JarEntry, JarInputStream}
import scala.collection.mutable
import scala.util.Using

class JarFileBytesClassLoader(bytes: Array[Byte], parent: ClassLoader) extends ClassLoader(parent) {

  private val classMap = mutable.Map[String, Array[Byte]]()

  private def init(bytes: Array[Byte]): Unit = {
    val buffer = Array.fill[Byte](1024)(0)
    Using(new JarInputStream(new ByteArrayInputStream(bytes))) { jin =>
      var entry: JarEntry = null
      while ({entry = jin.getNextJarEntry; entry != null}) {
        val name = entry.getRealName
        if (name.endsWith(".class")) {
          val className = name.replaceAll("[.]class", "").replaceAll("/", ".")
          val bout = new ByteArrayOutputStream()
          var readSize = 0
          while ({readSize = jin.read(buffer, 0, buffer.length); readSize != -1}) {
            bout.write(buffer, 0, readSize)
          }
          var classBytes = bout.toByteArray
          classMap(className) = classBytes
        }
      }
    }
  }

  init(bytes)

  override def findClass(name: String): Class[_] = {
    classMap.get(name) match {
      case Some(bytes) => defineClass(name, bytes, 0, bytes.length)
      case None => super.findClass(name)
    }
  }

}
