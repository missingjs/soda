package soda.scala.unittest.job

trait Validator[T] {
    def validate(expect: T, result: T): Boolean
}
