package soda.groovy.web.exception

class ServiceException extends RuntimeException {

    private int bizCode

    private String errMessage

    private int httpCode

    ServiceException(int bizCode, String errMessage, int httpCode, Throwable cause) {
        super(errMessage, cause)
        this.bizCode = bizCode
        this.errMessage = errMessage
        this.httpCode = httpCode
    }

    ServiceException(int bizCode, String errMessage, int httpCode) {
        super(errMessage)
        this.bizCode = bizCode
        this.errMessage = errMessage
        this.httpCode = httpCode
    }

    ServiceException(int bizCode, String errMessage) {
        this(bizCode, errMessage, 500)
    }

    int getBizCode() {
        return bizCode
    }

    String getErrorMessage() {
        return errMessage
    }

    int getHttpCode() {
        return httpCode
    }

}
