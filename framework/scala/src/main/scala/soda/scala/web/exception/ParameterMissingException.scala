package soda.scala.web.exception

import soda.scala.web.BusinessCode

class ParameterMissingException(key: String)
  extends ServiceException(BusinessCode.COMMON_ERROR, s"parameter missing - $key", 400) {
}
