package soda.scala.unittest

import scala.reflect.runtime.universe._
import play.api.libs.json._

import scala.collection.immutable.ArraySeq
import scala.collection.mutable

class StructTester(classType: Type) {

  private val runtimeMir = runtimeMirror(getClass.getClassLoader)

  def test(operations: List[String], parameters: JsValue): JsValue = {
    val ctorMir = ctorMirror
    val cparams = TestWork.parseArguments(Utils.getParamTypes(ctorMir), parameters(0))
    val obj = ctorMir.apply(ArraySeq.unsafeWrapArray(cparams): _*)
    val res = mutable.ArrayBuffer[JsValue]()
    res.append(JsNull)
    val instMir = runtimeMir.reflect(obj)
    for (i <- 1 until parameters.as[JsArray].value.size) {
      val methodName = operations(i)
      val method = instMir.reflectMethod(classType.decl(TermName(methodName)).asMethod)
      val params = TestWork.parseArguments(Utils.getParamTypes(method), parameters(i))
      val r = method.apply(ArraySeq.unsafeWrapArray(params): _*)
      val retType = Utils.getReturnType(method)
      if (retType != typeOf[Unit]) {
        val conv = ConverterFactory.create(retType).asInstanceOf[ObjectConverter[Any]]
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

//object StructTester {
//
//  private val runtimeMir = runtimeMirror(getClass.getClassLoader)
//
//  private val instMir = runtimeMir.reflect(
//    runtimeMir.reflectModule(typeOf[StructTester].typeSymbol.asClass.companion.asModule).instance
//  )
//
//  def main(args: Array[String]): Unit = {
//    create0(Su.f0)
//    create1(Su.f1)
//    create2(Su.f2)
//    create3(Su.f3)
//  }
//
//  def showFunc(func: Any): Unit = {
//    val instMir = runtimeMir.reflect(func)
//    println(instMir)
//    val m = instMir.reflectMethod(
//      instMir.symbol.typeSignature.decl(TermName("apply")).asMethod
////      typeOf[StructTester].companion.decl(TermName("apply")).asMethod
//    )
//    println(m)
//  }
//
//  def create0[R](func: () => R): Unit = {
//    println("()=>R")
//    showFunc(func)
//  }
//
//  def create1[P1,R](func: P1 => R): Unit = {
//    println("(P1)=>R")
//    showFunc(func)
//  }
//
//  def create2[P1,P2,R](func: (P1,P2)=>R): Unit = {
//    println("(p1,p2)=>R")
//    showFunc(func)
//  }
//
//  def create3[P1,P2,P3,R](func: (P1,P2,P3)=>R): Unit = {
//    println("(p1,p2,p3)=>R")
//    showFunc(func)
//  }
//}
