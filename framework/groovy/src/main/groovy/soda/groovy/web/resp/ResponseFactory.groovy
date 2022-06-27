package soda.groovy.web.resp

import soda.groovy.web.BusinessCode
import soda.groovy.web.exception.ServiceException

class ResponseFactory {

    private static final int HTTP_OK = 200

    private static final int HTTP_INTERNAL_ERROR = 500

    private static final int UNCLASSIFIED_ERROR_CODE = BusinessCode.COMMON_ERROR

    static class CommonResp {
        public int code
        public String message
        public Object detail = Collections.emptyMap()

        CommonResp(int code, String message, Object detail) {
            this.code = code
            this.message = message
            if (detail != null) {
                this.detail = detail
            }
        }

        CommonResp(int code, String message) {
            this(code, message, null)
        }

    }

    static Response response(int httpCode, int bizCode, String message, Object detail) {
        return new JsonResponse(httpCode, new CommonResp(bizCode, message, detail))
    }

    static Response response(int httpCode, int bizCode, String message) {
        return response(httpCode, bizCode, message, null)
    }

    static Response success(String message, Object detail) {
        return response(HTTP_OK, BusinessCode.SUCCESS, message, detail)
    }

    static Response success(String message) {
        return success(message, null)
    }

    static Response success() {
        return success("success")
    }

    static Response exception(ServiceException ex) {
        return response(ex.getHttpCode(), ex.getBizCode(), ex.getErrorMessage())
    }

    static Response exception(Throwable ex) {
        return response(HTTP_INTERNAL_ERROR, UNCLASSIFIED_ERROR_CODE, "internal server error: $ex")
    }

    static Response text(int httpCode, String content) {
        return new TextResponse(httpCode, content)
    }

    static Response text(String content) {
        return new TextResponse(HTTP_OK, content)
    }

}
