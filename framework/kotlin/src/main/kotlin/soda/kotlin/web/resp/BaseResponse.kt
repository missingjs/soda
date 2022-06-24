package soda.kotlin.web.resp

import com.sun.net.httpserver.Headers

abstract class BaseResponse(private val httpCode: Int, contentType: String): Response {

    private val _headers = Headers()

    init {
        _headers.set("Content-Type", contentType)
    }

    override fun getHttpCode(): Int = httpCode

    override fun getHeaders(): Headers = _headers

    override fun getContentType(): String = getHeaders().getFirst("Content-Type")

}