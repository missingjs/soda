package soda.scala.unittest

import play.api.libs.json.JsValue

import scala.reflect.runtime.universe._

class GenericTestWork[R: TypeTag](proxy: TaskProxy[R]) extends Validatable[R] {

  def run(): Unit = {
    val input = new WorkInput(Utils.fromStdin())
    val result = proxy.execute(input)
    val elapseMillis = proxy.elapseMillis
    val output = validate(input, typeOf[R], result, elapseMillis)
    println(output.jsonString)
  }

  def setValidator(v: (R, R) => Boolean): Unit = {
    validator = Some(v)
  }
}

object GenericTestWork {

  def create1[P1: TypeTag, R: TypeTag](func: P1 => R): GenericTestWork[R] = {
    new GenericTestWork[R](new Task1(func))
  }

  def create1u[P1: TypeTag](func: P1 => Unit): GenericTestWork[P1] = {
    new GenericTestWork[P1](new Task1((p1: P1) => { func(p1); p1 }))
  }

  def create2[P1: TypeTag, P2: TypeTag, R: TypeTag](func: (P1, P2) => R): GenericTestWork[R] = {
    new GenericTestWork[R](new Task2(func))
  }

  def forStruct(classType: Type): GenericTestWork[JsValue] = {
    create2(new StructTester((classType)).test)
  }
}
