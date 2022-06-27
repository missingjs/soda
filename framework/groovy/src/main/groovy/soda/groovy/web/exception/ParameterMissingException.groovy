package soda.groovy.web.exception

import soda.groovy.web.BusinessCode

class ParameterMissingException extends ServiceException {
    ParameterMissingException(String key) {
        super(BusinessCode.COMMON_ERROR, "parameter missing - $key", 400);
    }
}
