package soda.groovy.web.resp

import soda.groovy.web.BusinessCode
import soda.groovy.web.exception.ServiceException

class ResponseFactory {

    private static final int HTTP_OK = 200

    private static final int HTTP_INTERNAL_ERROR = 500

    private static final int UNCLASSIFIED_ERROR_CODE = BusinessCode.COMMON_ERROR

    static Response success(String message) {
        var data = ["code": BusinessCode.SUCCESS, "message": message]
        return new JsonResponse(HTTP_OK, data)
    }

    static Response success() {
        return success("success")
    }

    static Response error(int httpCode, int bizCode, String message) {
        var data = ["code": bizCode, "message": message]
        return new JsonResponse(httpCode, data)
    }

    static Response exception(ServiceException ex) {
        return error(ex.getHttpCode(), ex.getBizCode(), ex.getErrorMessage())
    }

    static Response exception(Throwable ex) {
        return error(HTTP_INTERNAL_ERROR, UNCLASSIFIED_ERROR_CODE, "internal server error: $ex")
    }

    static Response text(int httpCode, String content) {
        return new TextResponse(httpCode, content)
    }

    static Response text(String content) {
        return new TextResponse(HTTP_OK, content)
    }

}
