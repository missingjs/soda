package soda.kotlin.web.resp

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf
import java.nio.charset.StandardCharsets

class JsonResponse(httpCode: Int, private val body: String):
    BaseResponse(httpCode, "application/json; charset=utf-8") {

    override fun getBody(): ByteArray {
        return body.toByteArray(StandardCharsets.UTF_8)
    }

    companion object {

        inline fun <reified T> create(httpCode: Int, body: T): JsonResponse {
            @Suppress("UNCHECKED_CAST")
            val ks = serializer(typeOf<T>()) as KSerializer<T>
            return JsonResponse(httpCode, Json.encodeToString(ks, body))
        }

    }

}