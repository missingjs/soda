package soda.kotlin.web.resp

import java.nio.charset.StandardCharsets

class TextResponse(httpCode: Int, var content: String):
    BaseResponse(httpCode, "text/plain; charset=utf-8") {

    override fun getBody(): ByteArray {
        return content.toByteArray(StandardCharsets.UTF_8)
    }

}