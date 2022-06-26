package soda.scala.web.bootstrap

import soda.scala.web.WebUtils

class BootstrapContext(private val key: String, artifact: Array[Byte]) {

  private var md5: String = ""

  private var classLoader: ClassLoader = null

  private def init(): Unit = {
    md5 = WebUtils.md5Hex(artifact)
    val parentLoader = Thread.currentThread().getContextClassLoader
    classLoader = new JarFileBytesClassLoader(artifact, parentLoader)
  }

  def getKey: String = key

  def getMd5: String = md5

  def getClassLoader: ClassLoader = classLoader

  init()

}
