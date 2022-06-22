package soda.web.resp;

import java.util.Map;

import soda.web.BusinessCode;
import soda.web.ServiceException;

public class ResponseFactory {

    private static final int HTTP_OK = 200;

    private static final int HTTP_INTERNAL_ERROR = 500;

    private static final int UNCLASSIFIED_ERROR_CODE = BusinessCode.COMMON_ERROR;
    
    public static Response success(String message) {
        var data = Map.of("code", BusinessCode.SUCCESS, "message", message);
        return new JsonResponse(HTTP_OK, data);
    }

    public static Response success() {
        return success("success");
    }

    public static Response error(int httpCode, int bizCode, String message) {
        var data = Map.of("code", bizCode, "message", message);
        return new JsonResponse(httpCode, data);
    }

    public static Response exception(ServiceException ex) {
        return error(ex.getHttpCode(), ex.getBizCode(), ex.getErrorMessage());
    }

    public static Response exception(Throwable ex) {
        return error(HTTP_INTERNAL_ERROR, UNCLASSIFIED_ERROR_CODE, "internal server error");
    }

    public static Response text(int httpCode, String content) {
        return new TextResponse(httpCode, content);
    }

    public static Response text(String content) {
        return new TextResponse(HTTP_OK, content);
    }

}
