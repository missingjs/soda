package soda.web.exception;

import soda.web.BusinessCode;

public class ParameterMissingException extends ServiceException {
    public ParameterMissingException(String key) {
        super(BusinessCode.COMMON_ERROR, "parameter missing - " + key, 400);
    }
}
