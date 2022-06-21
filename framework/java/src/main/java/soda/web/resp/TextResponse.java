package soda.web.resp;

import java.nio.charset.StandardCharsets;

public class TextResponse extends BaseResponse {
    
    private String text;

    public TextResponse(int httpCode, String content) {
        super(httpCode, "text/plain; charset=utf-8");
        text = content;
    }

    @Override
    public byte[] getBody() {
        return text.getBytes(StandardCharsets.UTF_8);
    }

    public void setContent(String content) {
        text = content;
    }

}
