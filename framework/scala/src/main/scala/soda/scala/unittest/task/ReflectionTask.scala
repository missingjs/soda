package soda.scala.unittest.task

import soda.scala.unittest.{Utils, WorkInput}

import scala.reflect.runtime.universe._

class ReflectionTask(methodMirror: MethodMirror) extends _TaskBase[Any] {

  private val _argumentTypes = Utils.getParamTypes(methodMirror)

  private val _returnType = Utils.getReturnType(methodMirror)

  private def voidFunc: Boolean = _returnType.toString == typeOf[Unit].toString

  override def returnType: Type = if (voidFunc) _argumentTypes.head else _returnType

  override def argumentTypes: List[Type] = _argumentTypes

  override def execute(input: WorkInput): Any = {
    val task =
      if (voidFunc)
        () => {
          methodMirror.apply(arguments: _*)
          arguments.head
        }
      else
        () => methodMirror.apply(arguments: _*)
    run(_argumentTypes, input, task)
  }
}
