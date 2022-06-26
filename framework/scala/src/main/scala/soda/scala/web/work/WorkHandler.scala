package soda.scala.web.work

import com.sun.net.httpserver.HttpExchange
import play.api.libs.json._
import soda.scala.web.http.RequestHelper
import soda.scala.web.resp.{Response, ResponseFactory}
import soda.scala.web.bootstrap.ContextManager
import soda.scala.web.{BaseHandler, Logger}

import java.util.concurrent.{TimeUnit, TimeoutException}
import scala.reflect.runtime.universe._

class WorkHandler(private val contextManager: ContextManager, private val timeoutMillis: Long) extends BaseHandler {

  override def doPost(exchange: HttpExchange): Response = {
    val content = RequestHelper.bodyString(exchange)
    val jr = Json.parse(content).as[WorkRequest]

    Logger.info(s"context key: ${jr.key}")
    Logger.info(s"boot class: ${jr.bootClass}")
    Logger.info(s"test case: ${jr.testCase}")

    val task: () => String = () => {
      val oldCtxLoader = Thread.currentThread().getContextClassLoader
      try {
        val classLoader = contextManager.get(jr.key) match {
          case Some(ctx) => ctx.getClassLoader
          case None => throw new RuntimeException(s"no context found by key: ${jr.key}")
        }
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
      val result = future.get(timeoutMillis, TimeUnit.MILLISECONDS)
      Logger.info(s"test output: $result")
      ResponseFactory.text(result)
    } catch {
      case ex: TimeoutException =>
        job.kill()
        throw new RuntimeException("Work execution timeout", ex)
    }
  }
}
