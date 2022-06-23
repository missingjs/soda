package soda.web;

public class ServiceException extends RuntimeException {

    private int bizCode;

    private String errMessage;

    private int httpCode;

    public ServiceException(int bizCode, String errMessage, int httpCode, Throwable cause) {
        super(errMessage, cause);
        this.bizCode = bizCode;
        this.errMessage = errMessage;
        this.httpCode = httpCode;
    }

    public ServiceException(int bizCode, String errMessage, int httpCode) {
        super(errMessage);
        this.bizCode = bizCode;
        this.errMessage = errMessage;
        this.httpCode = httpCode;
    }

    public ServiceException(int bizCode, String errMessage) {
        this(bizCode, errMessage, 500);
    }

    public int getBizCode() {
        return bizCode;
    }

    public String getErrorMessage() {
        return errMessage;
    }

    public int getHttpCode() {
        return httpCode;
    }

}
