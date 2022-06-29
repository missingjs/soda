package soda.scala.unittest.task

import soda.scala.unittest.{Utils, WorkInput}

import scala.reflect.runtime.universe._

abstract class TaskBase[R: TypeTag](private val argTypes: List[Type]) extends TaskProxy[R] {

  private var args: List[Any] = List.empty

  private var elapse: Double = 0.0

  protected def run(): R

  override def execute(input: WorkInput): R = {
    args = Utils.parseArguments(argTypes, input.arguments).toList
    val startNano = System.nanoTime()
    val result = run()
    val endNano = System.nanoTime()
    elapse = (endNano - startNano) / 1e6
    result
  }

  protected def arg[A](index: Int): A = args(index).asInstanceOf[A]

  override def elapseMillis: Double = elapse

  override def returnType: Type = typeOf[R]

  override def argumentTypes: List[Type] = argTypes

  override def arguments: List[Any] = args

}

class Task1[
  A1: TypeTag, R: TypeTag
](func: A1 => R) extends TaskBase[R](
  List(
    typeOf[A1]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0)
    )
  }
}

class Task2[
  A1: TypeTag, A2: TypeTag, R: TypeTag
](func: (A1, A2) => R) extends TaskBase[R](
  List(
    typeOf[A1], typeOf[A2]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0), arg(1)
    )
  }
}

class Task3[
  A1: TypeTag, A2: TypeTag, A3: TypeTag, R: TypeTag
](func: (A1, A2, A3) => R) extends TaskBase[R](
  List(
    typeOf[A1], typeOf[A2], typeOf[A3]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0), arg(1), arg(2)
    )
  }
}

class Task4[
  A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag,
  R: TypeTag
](func: (A1, A2, A3, A4) => R) extends TaskBase[R](
  List(
    typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0), arg(1), arg(2),
      arg(3)
    )
  }
}

class Task5[
  A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag,
  A5: TypeTag, R: TypeTag
](func: (A1, A2, A3, A4, A5) => R) extends TaskBase[R](
  List(
    typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4],
    typeOf[A5]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0), arg(1), arg(2),
      arg(3), arg(4)
    )
  }
}

class Task6[
  A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag,
  A5: TypeTag, A6: TypeTag, R: TypeTag
](func: (A1, A2, A3, A4, A5, A6) => R) extends TaskBase[R](
  List(
    typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4],
    typeOf[A5], typeOf[A6]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0), arg(1), arg(2),
      arg(3), arg(4), arg(5)
    )
  }
}

class Task7[
  A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag,
  A5: TypeTag, A6: TypeTag, A7: TypeTag, R: TypeTag
](func: (A1, A2, A3, A4, A5, A6, A7) => R) extends TaskBase[R](
  List(
    typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4],
    typeOf[A5], typeOf[A6], typeOf[A7]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0), arg(1), arg(2),
      arg(3), arg(4), arg(5),
      arg(6)
    )
  }
}

class Task8[
  A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag,
  A5: TypeTag, A6: TypeTag, A7: TypeTag, A8: TypeTag,
  R: TypeTag
](func: (A1, A2, A3, A4, A5, A6, A7, A8) => R) extends TaskBase[R](
  List(
    typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4],
    typeOf[A5], typeOf[A6], typeOf[A7], typeOf[A8]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0), arg(1), arg(2),
      arg(3), arg(4), arg(5),
      arg(6), arg(7)
    )
  }
}

class Task9[
  A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag,
  A5: TypeTag, A6: TypeTag, A7: TypeTag, A8: TypeTag,
  A9: TypeTag, R: TypeTag
](func: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => R) extends TaskBase[R](
  List(
    typeOf[A1], typeOf[A2], typeOf[A3], typeOf[A4],
    typeOf[A5], typeOf[A6], typeOf[A7], typeOf[A8],
    typeOf[A9]
  )
) {
  override protected def run(): R = {
    func.apply(
      arg(0), arg(1), arg(2),
      arg(3), arg(4), arg(5),
      arg(6), arg(7), arg(8)
    )
  }
}
