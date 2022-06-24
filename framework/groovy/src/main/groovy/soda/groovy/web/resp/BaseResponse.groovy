package soda.groovy.web.resp

import com.sun.net.httpserver.Headers

abstract class BaseResponse implements Response {

    private int httpCode

    private Headers headers = new Headers()

    BaseResponse(int httpCode, String contentType) {
        this.httpCode = httpCode
        headers.set("Content-Type", contentType)
    }

    @Override
    int getHttpCode() {
        return httpCode
    }

    @Override
    Headers getHeaders() {
        return headers
    }

    @Override
    String getContentType() {
        return headers.getFirst("Content-Type")
    }

}
