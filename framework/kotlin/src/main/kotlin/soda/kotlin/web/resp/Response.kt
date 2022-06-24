package soda.kotlin.web.resp

import com.sun.net.httpserver.Headers

interface Response {

    fun getHttpCode(): Int

    fun getHeaders(): Headers

    fun getContentType(): String

    fun getBody(): ByteArray

}