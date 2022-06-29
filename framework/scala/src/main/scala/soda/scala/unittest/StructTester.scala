package soda.scala.unittest

import scala.collection.immutable.ArraySeq
import scala.collection.mutable
import scala.reflect.runtime.universe._

import play.api.libs.json._
import soda.scala.unittest.conv._

class StructTester(classType: Type) {

  private val runtimeMir = runtimeMirror(Thread.currentThread().getContextClassLoader)

  def test(operations: List[String], parameters: JsValue): JsValue = {
    val ctorMir = ctorMirror
    val cparams = Utils.parseArguments(Utils.getParamTypes(ctorMir), parameters(0))
    val obj = ctorMir.apply(ArraySeq.unsafeWrapArray(cparams): _*)
    val res = mutable.ArrayBuffer[JsValue]()
    res.append(JsNull)
    val instMir = runtimeMir.reflect(obj)
    for (i <- 1 until parameters.as[JsArray].value.size) {
      val methodName = operations(i)
      val method = instMir.reflectMethod(classType.decl(TermName(methodName)).asMethod)
      val params = Utils.parseArguments(Utils.getParamTypes(method), parameters(i))
      val r = method.apply(ArraySeq.unsafeWrapArray(params): _*)
      val retType = Utils.getReturnType(method)
      if (retType.toString != typeOf[Unit].toString) {
        val conv = ConverterFactory.create[Any](retType)
        res.append(conv.toJsonSerializable(r))
      } else {
        res.append(JsNull)
      }
    }
    Json.toJson(res.toList)
  }

  def ctorMirror: MethodMirror = {
    val cm = runtimeMir.reflectClass(classType.typeSymbol.asClass)
    cm.reflectConstructor(classType.decl(termNames.CONSTRUCTOR).asMethod)
  }

}
