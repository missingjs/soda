package soda.scala.unittest.task

import soda.scala.unittest.WorkInput

import scala.reflect.runtime.universe.Type

trait TaskProxy[R] {

  def returnType: Type

  def argumentTypes: List[Type]

  def arguments: List[Any]

  def execute(input: WorkInput): R

  def elapseMillis: Double

}
