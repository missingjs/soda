package soda.kotlin.web.http

class MultipartFormData(private val partData: Map<String, List<Part>>) {

    fun firstValue(key: String): String? {
        return partData.getOrDefault(key, listOf()).firstOrNull()?.bodyString()
    }

    fun values(key: String): List<String> {
        return partData.getOrDefault(key, listOf()).map { it.bodyString() }
    }

    fun firstFile(key: String): ByteArray? {
        return partData.getOrDefault(key, listOf()).firstOrNull()?.payload
    }

    fun files(key: String): List<ByteArray> {
        return partData.getOrDefault(key, listOf()).map { it.payload }
    }

}