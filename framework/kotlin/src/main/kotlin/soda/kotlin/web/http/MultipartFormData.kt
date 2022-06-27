package soda.kotlin.web.http

import soda.kotlin.web.exception.ParameterMissingException

class MultipartFormData(private val partData: Map<String, List<Part>>) {

    fun firstValueOpt(key: String): String? {
        return elementOpt(key) { it.bodyString() }
    }

    fun firstValue(key: String): String {
        return firstElement(key) { it.bodyString() }
    }

    fun values(key: String): List<String> {
        return elements(key) { it.bodyString() }
    }

    fun firstFileOpt(key: String): ByteArray? {
        return elementOpt(key) { it.payload }
    }

    fun firstFile(key: String): ByteArray {
        return firstElement(key) { it.payload }
    }

    fun files(key: String): List<ByteArray> {
        return elements(key) { it.payload }
    }

    private fun <T> elements(key: String, mapper: (Part) -> T): List<T> {
        return partData.getOrElse(key) { listOf() }.map(mapper)
    }

    private fun <T> elementOpt(key: String, mapper: (Part) -> T): T? {
        return elements(key, mapper).firstOrNull()
    }

    private fun <T> firstElement(key: String, mapper: (Part) -> T): T {
        return elementOpt(key, mapper) ?: throw ParameterMissingException(key)
    }

}