package soda.kotlin.web.resp

import kotlinx.serialization.Serializable
import soda.kotlin.web.BusinessCode
import soda.kotlin.web.exception.ServiceException

object ResponseFactory {

    private const val HTTP_OK = 200

    private const val HTTP_INTERNAL_SERVER_ERROR = 500

    @Serializable
    class SimpleResp(val code: Int, val message: String)

    fun success(message: String): Response {
        return JsonResponse.create(HTTP_OK, SimpleResp(BusinessCode.SUCCESS, message))
    }

    fun success(): Response {
        return success("success")
    }

    fun error(httpCode: Int, bizCode: Int, message: String): Response {
        return JsonResponse.create(httpCode, SimpleResp(bizCode, message))
    }

    fun exception(ex: ServiceException): Response {
        return error(ex.httpCode, ex.bizCode, ex.errMessage)
    }

    fun exception(ex: Throwable): Response {
        return error(HTTP_INTERNAL_SERVER_ERROR, BusinessCode.COMMON_ERROR, "internal server error: $ex")
    }

    fun text(httpCode: Int, content: String): Response {
        return TextResponse(httpCode, content)
    }

    fun text(content: String): Response {
        return TextResponse(HTTP_OK, content)
    }

}