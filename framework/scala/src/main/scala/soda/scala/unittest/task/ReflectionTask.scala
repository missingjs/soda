package soda.scala.unittest.task

import soda.scala.unittest.{Utils, WorkInput}

import scala.reflect.runtime.universe._

class ReflectionTask(methodMirror: MethodMirror) extends TaskProxy[Any] {

  private val argTypes = Utils.getParamTypes(methodMirror)

  private val retType = Utils.getReturnType(methodMirror)

  private val voidFunc = retType.toString == typeOf[Unit].toString

  private var args: List[Any] = List.empty

  private var elapse: Double = 0.0

  override def execute(input: WorkInput): Any = {
    args = Utils.parseArguments(argTypes, input.arguments).toList
    val startNano = System.nanoTime()
    val result = if (voidFunc) {
      methodMirror.apply(args: _*)
      args.head
    } else {
      methodMirror.apply(args: _*)
    }
    val endNano = System.nanoTime()
    elapse = (endNano - startNano) / 1e6
    result
  }

  override def returnType: Type = if (voidFunc) argTypes.head else retType

  override def argumentTypes: List[Type] = argTypes

  override def elapseMillis: Double = elapse

  override def arguments: List[Any] = args

}
