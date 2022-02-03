package soda.scala.unittest

import play.api.libs.json._
import soda.scala.unittest.conv._
import soda.scala.unittest.task._
import soda.scala.unittest.validate.FeatureFactory

import scala.reflect.runtime.universe._

class GenericTestWork[R: TypeTag](proxy: TaskProxy[R]) {

  var compareSerial: Boolean = false

  private var validator: Option[(R, R) => Boolean] = None

  private var _arguments: List[Any] = List.empty

  def arguments: List[Any] = _arguments

  def run(text: String): String = {
    val input = new WorkInput(text)
    val result = proxy.execute(input)
    _arguments = proxy.arguments

    val retType = proxy.returnType
    val resConv = ConverterFactory.create(retType).asInstanceOf[ObjectConverter[R]]
    val serialResult = resConv.toJsonSerializable(result)
    val output = new WorkOutput
    output.id = input.id
    output.result = serialResult
    output.elapse = proxy.elapseMillis

    var success = true
    if (input.hasExpected) {
      if (compareSerial && validator.isEmpty) {
        val a = Json.stringify(input.expected)
        val b = Json.stringify(serialResult)
        success = a == b
      } else {
        val expect = resConv.fromJsonSerializable(input.expected)
        success = validator match {
          case Some(va) => va.apply(expect, result)
          case None => FeatureFactory.create(retType).isEqual(expect, result)
        }
      }
    }
    output.success = success
    output.jsonString
  }

  def run(): Unit = {
    println(run(Utils.fromStdin()))
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

  def create3[P1: TypeTag, P2: TypeTag, P3: TypeTag, R: TypeTag](func: (P1, P2, P3) => R): GenericTestWork[R] = {
    new GenericTestWork[R](new Task3(func))
  }

  def create4[P1: TypeTag, P2: TypeTag, P3: TypeTag, P4: TypeTag, R: TypeTag](func: (P1, P2, P3, P4) => R): GenericTestWork[R] = {
    new GenericTestWork[R](new Task4(func))
  }

  def create5[P1: TypeTag, P2: TypeTag, P3: TypeTag, P4: TypeTag, P5: TypeTag, R: TypeTag](func: (P1, P2, P3, P4, P5) => R): GenericTestWork[R] = {
    new GenericTestWork[R](new Task5(func))
  }

  def create6[P1: TypeTag, P2: TypeTag, P3: TypeTag, P4: TypeTag, P5: TypeTag, P6: TypeTag, R: TypeTag](func: (P1, P2, P3, P4, P5, P6) => R): GenericTestWork[R] = {
    new GenericTestWork[R](new Task6(func))
  }

  def forStruct(classType: Type): GenericTestWork[JsValue] = {
    create2(new StructTester((classType)).test)
  }
}
