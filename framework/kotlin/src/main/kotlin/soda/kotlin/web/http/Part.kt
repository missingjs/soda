package soda.kotlin.web.http

import java.nio.charset.StandardCharsets

class Part {

    var contentDisposition: ContentDisposition? = null

    var contentType: String = ""

    var payload = ByteArray(0)

    fun getName(): String = contentDisposition?.name ?: ""

    fun bodyString(): String = String(payload, StandardCharsets.UTF_8)

}