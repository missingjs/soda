package soda.scala.web

import java.io.File
import java.net.URLClassLoader
import scala.collection.mutable

object ClassLoaderCache {

  private val loaderMap = mutable.Map[String, ClassLoader]()

  def set(path: String): Unit = this.synchronized {
    val parent = Thread.currentThread().getContextClassLoader
    val loader = new URLClassLoader(Array(new File(path).toURI.toURL), parent)
    loaderMap(path) = loader
    Logger.info(s"new class loader for $path: $loader")
  }

  def get(path: String): ClassLoader = this.synchronized {
    loaderMap.get(path) match {
      case Some(loader) => loader
      case None =>
        set(path)
        loaderMap(path)
    }
  }

  def remove(path: String): Unit = this.synchronized {
    loaderMap.remove(path)
  }

}
