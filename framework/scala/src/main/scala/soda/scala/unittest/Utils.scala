package soda.scala.unittest

import scala.io.StdIn.readLine
import scala.reflect.runtime.universe._

import play.api.libs.json.JsValue
import soda.scala.unittest.conv.ConverterFactory

object Utils {

  def getParamTypes(ms: MethodSymbol): List[Type] = {
    val typ = ms.typeSignature
    typ.paramLists.headOption match {
      case Some(symbols) => symbols.map(_.typeSignatureIn(typ))
      case None => List.empty
    }
  }

  def getParamTypes(mm: MethodMirror): List[Type] = {
    getParamTypes(mm.symbol)
  }

  def getReturnType(ms: MethodSymbol): Type = {
    ms.returnType
  }

  def getReturnType(mm: MethodMirror): Type = {
    getReturnType(mm.symbol)
  }

  def getEtaExpandedMethodType(methodSymbol: MethodSymbol): Type = {
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

  def getEtaExpandedMethodType(methodMirror: MethodMirror): Type = {
    getEtaExpandedMethodType(methodMirror.symbol)
  }

  def fromStdin(): String = {
    LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
  }

  def parseArguments(types: List[Type], rawParams: JsValue): Array[Any] = {
    val args = Array.fill[Any](types.size)(null)
    for (i <- types.indices) {
      args(i) = ConverterFactory.create[Any](types(i)).fromJsonSerializable(rawParams(i))
    }
    args
  }

}
