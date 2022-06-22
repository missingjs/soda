package soda.scala.web

import com.sun.net.httpserver.HttpExchange
import java.util.concurrent.{TimeoutException, TimeUnit}
import scala.reflect.runtime.universe._

import play.api.libs.json._

class WorkHandler extends BaseHandler {

  val maxTimeoutMillis: Long = 10000

  private val timeoutMillis: Long = maxTimeoutMillis

  override def handleWork(exchange: HttpExchange): String = {
    val content = readPostBody(exchange)
    Logger.info(s"request: $content")
    implicit val _format: OFormat[WorkRequest] = Json.format[WorkRequest]
    val jr = Json.parse(content).as[WorkRequest]

    val task: () => String = () => {
      val oldCtxLoader = Thread.currentThread().getContextClassLoader
      try {
        val classLoader = ClassLoaderCache.getForJar(jr.classpath)
        Thread.currentThread().setContextClassLoader(classLoader)
        val rm = runtimeMirror(classLoader)
        val csym = rm.staticClass(jr.bootClass)
        val classType = csym.typeSignature
        val cm = rm.reflectClass(csym)
        val ctor = cm.reflectConstructor(classType.decl(termNames.CONSTRUCTOR).asMethod)
        val obj = ctor.apply()
        val im = rm.reflect(obj)
        val applySym = classType.decl(TermName("apply"))
        val mm = im.reflectMethod(applySym.asMethod)
        mm.apply(jr.testCase).asInstanceOf[String]
      } finally {
        Thread.currentThread().setContextClassLoader(oldCtxLoader)
      }
    }

    val job = new TimeLimitedWork(task)
    val future = job.start()
    try {
      future.get(timeoutMillis, TimeUnit.MILLISECONDS)
    } catch {
      case ex: TimeoutException =>
        job.kill()
        throw new RuntimeException("Work execution timeout", ex)
    }
  }
}
