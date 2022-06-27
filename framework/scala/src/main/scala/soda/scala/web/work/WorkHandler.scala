package soda.scala.web.work

import com.sun.net.httpserver.HttpExchange
import play.api.libs.json._
import soda.scala.web.http.RequestHelper
import soda.scala.web.resp.{Response, ResponseFactory}
import soda.scala.web.bootstrap.ContextManager
import soda.scala.web.exception.{ParameterMissingException, ServiceException}
import soda.scala.web.{BaseHandler, BusinessCode, Logger}

import java.nio.charset.StandardCharsets
import java.util.concurrent.{TimeUnit, TimeoutException}
import scala.reflect.runtime.universe._

class WorkHandler(private val contextManager: ContextManager, private val timeoutMillis: Long) extends BaseHandler {

  override def doPost(exchange: HttpExchange): Response = {
    val req = WorkHandler.parse(exchange)
    Logger.info(s"context key: ${req.key}")
    Logger.info(s"entry class: ${req.entryClass}")
    Logger.info(s"test case: ${req.testCase}")

    val task: () => String = () => {
      val oldCtxLoader = Thread.currentThread().getContextClassLoader
      try {
        val classLoader = contextManager.get(req.key) match {
          case Some(ctx) => ctx.getClassLoader
          case None => throw new RuntimeException(s"no context found by key: ${req.key}")
        }
        Thread.currentThread().setContextClassLoader(classLoader)
        val rm = runtimeMirror(classLoader)
        val csym = rm.staticClass(req.entryClass)
        val classType = csym.typeSignature
        val cm = rm.reflectClass(csym)
        val ctor = cm.reflectConstructor(classType.decl(termNames.CONSTRUCTOR).asMethod)
        val obj = ctor.apply()
        val im = rm.reflect(obj)
        val applySym = classType.decl(TermName("apply"))
        val mm = im.reflectMethod(applySym.asMethod)
        mm.apply(req.testCase).asInstanceOf[String]
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

object WorkHandler {
  private def parse(exchange: HttpExchange): WorkRequest = {
    val contentType = exchange.getRequestHeaders.getFirst("Content-Type")
    if (contentType.contains("application/json")) {
      val content = RequestHelper.bodyString(exchange)
      Json.parse(content).as[WorkRequest]
    } else if (contentType.contains("multipart/form-data")) {
      val formData = RequestHelper.multipartFormData(exchange)
      val key = formData.firstValue("key") match {
        case Some(key) => key
        case None => throw new ParameterMissingException("key")
      }
      val entryClass = formData.firstValue("entry_class") match {
        case Some(ec) => ec
        case None => throw new ParameterMissingException("entry_class")
      }
      val caseBytes = formData.firstFile("test_case") match {
        case Some(cb) => cb
        case None => throw new ParameterMissingException("test_case")
      }
      val testCase = new String(caseBytes, StandardCharsets.UTF_8)
      WorkRequest(key, entryClass, testCase)
    } else {
      throw new ServiceException(BusinessCode.COMMON_ERROR, "unknown Content-Type: " + contentType, 400)
    }
  }
}
