package soda.scala.unittest

import play.api.libs.json.{JsValue, Json}

import scala.io.StdIn.readLine
import scala.reflect.runtime.universe._

class GenericTestWork[R: TypeTag](proxy: TaskProxy[R]) {

  var compareSerial = false

  private var validator: Option[(R, R) => Boolean] = None

  var expectedOutput: Option[R] = None

  def run(): Unit = {
    val inputText = LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
    val input = new WorkInput(inputText)

    val result = proxy.execute(input)
    val elapseMillis = proxy.elapseMillis

    val resConv = ConverterFactory.create[R]()
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
          case Some(va) => va.apply(expect, result)
          case None => ValidatorFactory.create[R]().apply(expect, result)
        }
      }
    }
    output.success = success

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
