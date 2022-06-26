package soda.scala.web.bootstrap

import scala.collection.mutable

class ContextManager {

  private val contextMap = mutable.WeakHashMap[String, BootstrapContext]()

  def register(key: String, artifact: Array[Byte]): BootstrapContext = this.synchronized {
    val context = new BootstrapContext(key, artifact)
    contextMap(key) = context
    context
  }

  def get(key: String): Option[BootstrapContext] = this.synchronized {
    contextMap.get(key)
  }

}
