package soda.scala.unittest

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

trait TaskProxy[R] {

  def argumentTypes: List[Type]

  def arguments: List[Any]

  def execute(input: WorkInput): R

  def elapseMillis: Double

}

abstract class _TaskBase[R] extends TaskProxy[R] {

  private var _elapse: Double = -1.0
  private var _argTypes: List[Type] = List.empty
  private var _args: List[Any] = List.empty

  protected def run(argTypes: List[Type], input: WorkInput, task: () => R): R = {
    _argTypes = argTypes
    _args = TestWork.parseArguments(argTypes, input.arguments).toList
    val startNano = System.nanoTime()
    val result = task()
    val endNano = System.nanoTime()
    _elapse = (endNano - startNano) / 1e6
    result
  }

  protected def arg[A](index: Int): A = _args(index).asInstanceOf[A]

  override def elapseMillis: Double = _elapse

  override def argumentTypes: List[universe.Type] = _argTypes

  override def arguments: List[Any] = _args

}

class Task1[A1: TypeTag, R: TypeTag](func: A1 => R) extends _TaskBase[R] {
  override def execute(input: WorkInput): R = {
    val argTypes = List(typeOf[A1])
    run(argTypes, input, () => func(arg(0)))
  }
}

class Task2[A1: TypeTag, A2: TypeTag, R: TypeTag](func: (A1, A2) => R) extends _TaskBase[R] {
  override def execute(input: WorkInput): R = {
    val argTypes = List(typeOf[A1], typeOf[A2])
    run(argTypes, input, () => func(arg(0), arg(1)))
  }
}
