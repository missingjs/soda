package soda.scala.unittest

import scala.io.StdIn.readLine
import scala.reflect.runtime.universe._
import play.api.libs.json._

import scala.collection.immutable.ArraySeq

//class TestWork(val solutionType: Type, val methodName: String) {
class TestWork(methodMirror: MethodMirror) {

  //  private val methodMirror = getMethodMirror

  private val argumentTypes = Utils.getParamTypes(methodMirror)

  private val returnType = Utils.getReturnType(methodMirror)

  private var arguments: Array[Any] = Array.empty

  var compareSerial = false

  private var validator: Option[(_, _) => Boolean] = None

  var expectedOutput: Option[Any] = None

  def run(): Unit = {
    val inputText = LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
    val input = new WorkInput(inputText)
    arguments = TestWork.parseArguments(argumentTypes, input.arguments)

    val startNano = System.nanoTime()
    var retType = returnType
    var result = methodMirror.apply(ArraySeq.unsafeWrapArray(arguments): _*)

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
      if (compareSerial && validator.isEmpty) {
        val a = Json.stringify(input.expected)
        val b = Json.stringify(serialResult)
        success = a == b
      } else {
        val expect = resConv.fromJsonSerializable(input.expected)
        expectedOutput = Some(expect)
        success = validator match {
          case Some(va) => va.asInstanceOf[(Any, Any) => Boolean].apply(expect, result)
          case None => ValidatorFactory.create(retType)(expect, result)
        }
      }
    }
    output.success = success

    println(output.jsonString)
  }

  def setValidator(v: (_, _) => Boolean): Unit = {
    validator = Some(v)
  }

  //  private def getMethodMirror = {
  //    val rMirror = runtimeMirror(getClass.getClassLoader)
  //    val instMirror = rMirror.reflect(
  //      rMirror.reflectModule(solutionType.typeSymbol.asClass.companion.asModule).instance
  //    )
  //    instMirror.reflectMethod(
  //      solutionType.companion.decl(TermName(methodName)).asMethod
  //    )
  //  }
}

object TestWork {

  def forCompanion(objType: Type, methodName: String): TestWork = {
    val rMirror = runtimeMirror(getClass.getClassLoader)
    val instMirror = rMirror.reflect(
      rMirror.reflectModule(objType.typeSymbol.asClass.companion.asModule).instance
    )
    val mm = instMirror.reflectMethod(
      objType.companion.decl(TermName(methodName)).asMethod
    )
    new TestWork(mm)
  }

  def forStruct(classType: Type): TestWork = {
    val tester = new StructTester(classType)
    val methodName = "test"
    val rMirror = runtimeMirror(getClass.getClassLoader)
    val instMirror = rMirror.reflect(tester)
    val mm = instMirror.reflectMethod(
      instMirror.symbol.typeSignature.decl(TermName(methodName)).asMethod
    )
    new TestWork(mm)
  }

  def parseArguments(types: List[Type], rawParams: JsValue): Array[Any] = {
    val args = Array.fill[Any](types.size)(null)
    for (i <- types.indices) {
      args(i) = ConverterFactory.create(types(i)).fromJsonSerializable(rawParams(i))
    }
    args
  }

  //  def forStruct(classType: Type): TestWork = {
  //    new StructTester(classType)
  //  }
}
