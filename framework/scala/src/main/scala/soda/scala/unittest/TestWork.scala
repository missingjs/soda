package soda.scala.unittest

import scala.reflect.runtime.universe._
import play.api.libs.json._

import scala.collection.immutable.ArraySeq

class TestWork(methodMirror: MethodMirror) extends Validatable[Any] with MagicWork {

  private val argumentTypes = Utils.getParamTypes(methodMirror)

  private val returnType = Utils.getReturnType(methodMirror)

  def run(text: String): String = {
    val input = new WorkInput(text)
    val args = TestWork.parseArguments(argumentTypes, input.arguments)
    _arguments = args.toList

    val startNano = System.nanoTime()
    var retType = returnType
    var result = methodMirror.apply(ArraySeq.unsafeWrapArray(args): _*)

    if (retType.toString == typeOf[Unit].toString) {
      retType = argumentTypes.head
      result = args.head
    }
    val endNano = System.nanoTime()
    val elapseMillis = (endNano - startNano) / 1e6
    val output = validate(input, retType, result, elapseMillis)
    output.jsonString
  }

  def run(): Unit = {
    println(run(Utils.fromStdin()))
  }

  def setValidator(v: (_, _) => Boolean): Unit = {
    validator = Some(v.asInstanceOf[(Any,Any)=>Boolean])
  }

  override protected var _arguments: List[Any] = _
}

object TestWork {

  def forObject(objType: Type, methodName: String): TestWork = {
    val rMirror = runtimeMirror(Thread.currentThread().getContextClassLoader)
    val instMirror = rMirror.reflect(
      rMirror.reflectModule(objType.typeSymbol.asClass.companion.asModule).instance
    )
    val mm = instMirror.reflectMethod(
      objType.companion.decl(TermName(methodName)).asMethod
    )
    new TestWork(mm)
  }

  def forInstance(inst: Any, methodName: String): TestWork = {
    val rMirror = runtimeMirror(Thread.currentThread.getContextClassLoader)
    val instMirror = rMirror.reflect(inst)
    val mm = instMirror.reflectMethod(
      instMirror.symbol.typeSignature.decl(TermName(methodName)).asMethod
    )
    new TestWork(mm)
  }

  def forStruct(classType: Type): TestWork = {
    val tester = new StructTester(classType)
    val methodName = "test"
    forInstance(tester, methodName)
  }

  def parseArguments(types: List[Type], rawParams: JsValue): Array[Any] = {
    val args = Array.fill[Any](types.size)(null)
    for (i <- types.indices) {
      args(i) = ConverterFactory.create(types(i)).fromJsonSerializable(rawParams(i))
    }
    args
  }
}
