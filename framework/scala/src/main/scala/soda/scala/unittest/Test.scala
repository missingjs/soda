package soda.scala.unittest

object Test extends App {

  val a = List(1,2,3,4).foldLeft(0)((a, b) => a * 11 + b)
  println(a)

}
