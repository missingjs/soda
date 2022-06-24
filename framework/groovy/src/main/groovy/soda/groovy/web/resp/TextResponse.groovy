package soda.groovy.web.resp

import java.nio.charset.StandardCharsets

class TextResponse extends BaseResponse {

    private String text

    TextResponse(int httpCode, String content) {
        super(httpCode, "text/plain; charset=utf-8")
        text = content
    }

    @Override
    byte[] getBody() {
        return text.getBytes(StandardCharsets.UTF_8)
    }

    void setContent(String content) {
        text = content
    }

}
