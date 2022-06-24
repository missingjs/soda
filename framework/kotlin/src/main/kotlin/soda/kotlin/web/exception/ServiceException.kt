package soda.kotlin.web.exception

open class ServiceException(
    val bizCode: Int,
    val errMessage: String,
    val httpCode: Int,
    cause: Throwable?
) : RuntimeException(errMessage, cause) {

    constructor(bizCode: Int, errMessage: String, httpCode: Int):
            this(bizCode, errMessage, httpCode, null) {}

    constructor(bizCode: Int, errMessage: String):
            this(bizCode, errMessage, 500) {}

}