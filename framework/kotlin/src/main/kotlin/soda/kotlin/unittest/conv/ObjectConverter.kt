package soda.kotlin.unittest.conv

import kotlinx.serialization.json.JsonElement

open class OCBase

class ObjectConverter<T>(private val parser: (JsonElement) -> T, private val serializer: (T) -> JsonElement) : OCBase() {

    fun fromJsonSerializable(js: JsonElement): T = parser(js)

    fun toJsonSerializable(element: T): JsonElement = serializer(element)

}

class OCHandle(private val objConv: OCBase) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(): ObjectConverter<T> = objConv as ObjectConverter<T>
}