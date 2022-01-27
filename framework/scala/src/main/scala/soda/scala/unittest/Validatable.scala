package soda.scala.unittest

import play.api.libs.json.Json

import scala.reflect.runtime.universe.Type

trait Validatable[R] {

  var compareSerial: Boolean = false

  protected var validator: Option[(R, R) => Boolean] = None

  var expectedOutput: Option[R] = None

  def validate(input: WorkInput, retType: Type, result: R, elapseMillis: Double): WorkOutput = {
    val resConv = ConverterFactory.create(retType).asInstanceOf[ObjectConverter[R]]
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
          case None => ValidatorFactory.create(retType)(expect, result)
        }
      }
    }
    output.success = success
    output
  }

}
