package soda.scala.unittest

import scala.io.StdIn.readLine
import scala.reflect.runtime.universe._

import play.api.libs.json._

class TestWork(val solutionType: Type, val methodName: String) {

  private val methodMirror = getMethodMirror

  private val methodType = getEtaExpandedMethodType(methodMirror)

  private val argumentTypes = methodType.typeArgs.dropRight(1)

  private val returnType = methodType.typeArgs.last

  private var arguments: Array[Any] = Array.empty

  var compareSerial = false

  def run(): Unit = {

    val input = LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
    val workInput = new WorkInput(input)
    arguments = parseArguments(argumentTypes, workInput.arguments)

    val startNano = System.nanoTime()
    var retType = returnType
    var result = methodMirror.apply(arguments: _*)

    if (retType == typeOf[Unit]) {
      retType = argumentTypes.head
      result = arguments(0)
    }
    val endNano = System.nanoTime()
    val elapseMillis = (endNano - startNano) / 1e6

    
  }

  private def parseArguments(types: List[Type], rawParams: JsValue): Array[Any] = {
    val args = Array.fill[Any](types.size)(null)
    for (i <- types.indices) {
      args(i) = ConverterFactory.create(types(i)).fromJsonSerializable(rawParams(i))
    }
    args
  }

  private def getEtaExpandedMethodType(methodSymbol: MethodSymbol): Type = {
    val typ = methodSymbol.typeSignature
    def paramType(paramSymbol: Symbol): Type = {
      // TODO: handle the case where paramSymbol denotes a type parameter
      paramSymbol.typeSignatureIn(typ)
    }
    def rec(paramLists: List[List[Symbol]]): Type = {
      paramLists match {
        case Nil => methodSymbol.returnType
        case params :: otherParams =>
          val functionClassSymbol = definitions.FunctionClass(params.length)
          appliedType(functionClassSymbol, params.map(paramType) :+ rec(otherParams))
      }
    }
    if (methodSymbol.paramLists.isEmpty) { // No arg method
      appliedType(definitions.FunctionClass(0), List(methodSymbol.returnType))
    } else {
      rec(methodSymbol.paramLists)
    }
  }
  private def getEtaExpandedMethodType(methodMirror: MethodMirror): Type = {
    getEtaExpandedMethodType(methodMirror.symbol)
  }

  private def getMethodMirror = {
    val rMirror = runtimeMirror(getClass.getClassLoader)
    val instMirror = rMirror.reflect(
      rMirror.reflectModule(solutionType.typeSymbol.asClass.companion.asModule).instance
    )
    instMirror.reflectMethod(
      solutionType.companion.decl(TermName(methodName)).asMethod
    )
  }
}
