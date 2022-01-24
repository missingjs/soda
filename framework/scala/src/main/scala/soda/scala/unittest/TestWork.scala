package soda.scala.unittest

import scala.io.StdIn.readLine
import scala.reflect.runtime.universe._
//import scala.util.parsing.combinator

class TestWork(val solutionType: Type, val methodName: String) {

  private val methodMirror = getMethodMirror()

  private val methodType = getEtaExpandedMethodType(methodMirror)

  private val argumentTypes = methodType.typeArgs.dropRight(1)

  private val returnType = methodType.typeArgs.last

  private var arguments: Array[Any] = Array.empty

  var compareSerial = false

  def run() = {
    val input = LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
    val workInput = new WorkInput(input)
    System.err.println("from scala: " + input);
    System.err.println("id: " + workInput.id)
    System.err.println("has expected: " + workInput.hasExpected)
//    println(input)
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

  private def getMethodMirror() = {
    val rMirror = runtimeMirror(getClass.getClassLoader)
    val instMirror = rMirror.reflect(
      rMirror.reflectModule(solutionType.typeSymbol.asClass.companion.asModule).instance
    )
    instMirror.reflectMethod(
      solutionType.companion.decl(TermName(methodName)).asMethod
    )
  }
}
