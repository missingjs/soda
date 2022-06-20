package soda.web.http;

import java.nio.charset.StandardCharsets;

public class Part {

    public ContentDisposition contentDisposition;

    public String contentType;

    public byte[] payload;

    public String getName() {
        return contentDisposition.name;
    }

    public String bodyString() {
        return new String(payload, StandardCharsets.UTF_8);
    }

}
