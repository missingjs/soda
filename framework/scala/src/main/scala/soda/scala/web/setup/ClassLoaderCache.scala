package soda.scala.web.setup

import soda.scala.web.Logger

import scala.collection.mutable

object ClassLoaderCache {

  private val loaderMap = mutable.Map[String, ClassLoader]()

  def remove(path: String): Unit = this.synchronized {
    loaderMap.remove(path)
  }

  def setupForJar(key: String, jarBytes: Array[Byte]): Unit = this.synchronized {
    val parent = Thread.currentThread().getContextClassLoader
    val loader = new JarFileBytesClassLoader(jarBytes, parent)
    loaderMap(key) = loader
  }

  def getForJar(key: String): ClassLoader = this.synchronized {
    loaderMap(key)
  }

}
