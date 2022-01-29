package soda.scala.unittest.task

import soda.scala.unittest.{Utils, WorkInput}

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

trait TaskProxy[R] {

  def returnType: Type

  def argumentTypes: List[Type]

  def arguments: List[Any]

  def execute(input: WorkInput): R

  def elapseMillis: Double

}

abstract class _TaskBase[R: TypeTag] extends TaskProxy[R] {

  private var _elapse: Double = -1.0
  private var _argTypes: List[Type] = List.empty
  private var _args: List[Any] = List.empty

  protected def run(argTypes: List[Type], input: WorkInput, task: () => R): R = {
    _argTypes = argTypes
    _args = Utils.parseArguments(argTypes, input.arguments).toList
    val startNano = System.nanoTime()
    val result = task()
    val endNano = System.nanoTime()
    _elapse = (endNano - startNano) / 1e6
    result
  }

  protected def arg[A](index: Int): A = _args(index).asInstanceOf[A]

  override def elapseMillis: Double = _elapse

  override def returnType: Type = typeOf[R]

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

class Task3[A1: TypeTag, A2: TypeTag, A3: TypeTag, R: TypeTag](func: (A1, A2, A3) => R) extends _TaskBase[R] {
  override def execute(input: WorkInput): R = {
    val argTypes = List(typeOf[A1], typeOf[A2], typeOf[A3])
    run(argTypes, input, () => func(arg(0), arg(1), arg(2)))
  }
}

class Task4[A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag, R: TypeTag](func: (A1, A2, A3, A4) => R) extends _TaskBase[R] {
  override def execute(input: WorkInput): R = {
    val argTypes = List(typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4])
    run(argTypes, input, () => func(arg(0), arg(1), arg(2), arg(3)))
  }
}

class Task5[A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag, A5: TypeTag, R: TypeTag](func: (A1, A2, A3, A4, A5) => R) extends _TaskBase[R] {
  override def execute(input: WorkInput): R = {
    val argTypes = List(typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4], typeOf[A5])
    run(argTypes, input, () => func(arg(0), arg(1), arg(2), arg(3), arg(4)))
  }
}

class Task6[A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag, A5: TypeTag, A6: TypeTag, R: TypeTag](func: (A1, A2, A3, A4, A5, A6) => R) extends _TaskBase[R] {
  override def execute(input: WorkInput): R = {
    val argTypes = List(typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4], typeOf[A5], typeOf[A6])
    run(argTypes, input, () => func(arg(0), arg(1), arg(2), arg(3), arg(4), arg(5)))
  }
}
