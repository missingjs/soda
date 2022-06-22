package soda.scala.web

class ServiceException(
    val bizCode: Int, 
    val errMessage: String,
    val httpCode: Int,
    cause: Throwable
) extends RuntimeException(errMessage, cause) {

    def this(bizCode: Int, errMessage: String, httpCode: Int) = {
        this(bizCode, errMessage, httpCode, null)
    }

    def this(bizCode: Int, errMessage: String) = {
        this(bizCode, errMessage, 500)
    }

}