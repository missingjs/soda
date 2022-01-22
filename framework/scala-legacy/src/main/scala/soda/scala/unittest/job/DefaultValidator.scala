package soda.scala.unittest.job

class DefaultValidator extends Validator[AnyRef] {
    def validate(expect: AnyRef, result: AnyRef): Boolean = {
        if (expect != null) expect.equals(result) else result == null
    }
}
