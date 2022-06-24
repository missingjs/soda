package soda.kotlin.web.exception

import soda.kotlin.web.BusinessCode

class ParameterMissingException(key: String):
    ServiceException(BusinessCode.COMMON_ERROR, "parameter missing - $key", 400) {
}