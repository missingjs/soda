package soda.web.resp;

import com.sun.net.httpserver.Headers;

abstract class BaseResponse implements Response {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    
    private final int httpCode;

    private Headers headers = new Headers();

    public BaseResponse(int httpCode, String contentType) {
        this.httpCode = httpCode;
        headers.set(HEADER_CONTENT_TYPE, contentType);
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    @Override
    public String getContentType() {
        return headers.getFirst(HEADER_CONTENT_TYPE);
    }

}
