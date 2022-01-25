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

  var validator: Option[(_, _) => Boolean] = None

  var expectedOutput: Option[Any] = None

  def run(): Unit = {
    val inputText = LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
    val input = new WorkInput(inputText)
    arguments = parseArguments(argumentTypes, input.arguments)

    val startNano = System.nanoTime()
    var retType = returnType
    var result = methodMirror.apply(arguments: _*)

    if (retType == typeOf[Unit]) {
      retType = argumentTypes.head
      result = arguments(0)
    }
    val endNano = System.nanoTime()
    val elapseMillis = (endNano - startNano) / 1e6

    val resConv = ConverterFactory.create(retType).asInstanceOf[ObjectConverter[Any]]
    val serialResult = resConv.toJsonSerializable(result)
    val output = new WorkOutput
    output.id = input.id
    output.result = serialResult
    output.elapse = elapseMillis

    var success = true
    if (input.hasExpected) {
      if (compareSerial && validator == null) {
        val a = Json.stringify(input.expected)
        val b = Json.stringify(serialResult)
        success = a == b
      } else {
        val expect = resConv.fromJsonSerializable(input.expected)
        expectedOutput = Some(expect)
        success = validator match {
          case Some(va) => va.asInstanceOf[(Any,Any)=>Boolean].apply(expect, result)
          case None => ValidatorFactory.create(retType)(expect, result)
        }
      }
    }
    output.success = success

    println(output.jsonString)
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
