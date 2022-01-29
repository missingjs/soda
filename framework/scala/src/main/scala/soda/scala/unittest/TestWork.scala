package soda.scala.unittest

import scala.reflect.runtime.universe._

import soda.scala.unittest.task.ReflectionTask

class TestWork(methodMirror: MethodMirror) {

  private val gwork = new GenericTestWork[Any](new ReflectionTask(methodMirror))

  var compareSerial: Boolean = false

  def run(text: String): String = {
    gwork.compareSerial = compareSerial
    gwork.run(text)
  }

  def run(): Unit = {
    println(run(Utils.fromStdin()))
  }

  def setValidator(v: (_, _) => Boolean): Unit = {
    gwork.setValidator(v.asInstanceOf[(Any,Any)=>Boolean])
  }

  def arguments: List[Any] = gwork.arguments
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
}
