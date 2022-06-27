package soda.kotlin.web.resp

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import soda.kotlin.web.BusinessCode
import soda.kotlin.web.exception.ServiceException
import kotlin.reflect.typeOf

object ResponseFactory {

    private const val HTTP_OK = 200

    private const val HTTP_INTERNAL_SERVER_ERROR = 500

    @Serializable
    class CommonResp(val code: Int, val message: String, val detail: JsonElement)

    fun response(httpCode: Int, bizCode: Int, message: String, detail: JsonElement?): Response {
        val d = detail ?: JsonObject(mapOf())
        return JsonResponse.create(httpCode, CommonResp(bizCode, message, d))
    }

    inline fun <reified T> response(httpCode: Int, bizCode: Int, message: String, detail: T): Response {
        @Suppress("UNCHECKED_CAST")
        val ks = serializer(typeOf<T>()) as KSerializer<T>
        return response(httpCode, bizCode, message, Json.encodeToJsonElement(ks, detail))
    }

    fun response(httpCode: Int, bizCode: Int, message: String): Response {
        return response(httpCode, bizCode, message, null)
    }

    fun success(message: String, detail: JsonElement?): Response {
        return response(HTTP_OK, BusinessCode.SUCCESS, message, detail)
    }

    inline fun <reified T> success(message: String, detail: T): Response {
        @Suppress("UNCHECKED_CAST")
        val ks = serializer(typeOf<T>()) as KSerializer<T>
        return success(message, Json.encodeToJsonElement(ks, detail))
    }

    fun success(message: String): Response {
        return success(message, null)
    }

    fun success(): Response {
        return success("success")
    }

    fun exception(ex: ServiceException): Response {
        return response(ex.httpCode, ex.bizCode, ex.errMessage)
    }

    fun exception(ex: Throwable): Response {
        return response(
            HTTP_INTERNAL_SERVER_ERROR,
            BusinessCode.COMMON_ERROR,
            "internal server error: $ex"
        )
    }

    fun text(httpCode: Int, content: String): Response {
        return TextResponse(httpCode, content)
    }

    fun text(content: String): Response {
        return TextResponse(HTTP_OK, content)
    }

}