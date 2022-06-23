package soda.groovy.web.http

import java.nio.charset.StandardCharsets

class Part {

    ContentDisposition contentDisposition

    String contentType

    byte[] payload

    String getName() {
        return contentDisposition.name
    }

    String bodyString() {
        return new String(payload, StandardCharsets.UTF_8)
    }

}
