package soda.scala.web

class ParameterMissingException(key: String)
  extends ServiceException(BusinessCode.COMMON_ERROR, s"parameter missing - $key", 400) {
}
