package soda.kotlin.unittest.conv

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KType

object ConverterFactory {

    private val factoryMap = mutableMapOf<String, () -> OCHandle>()

    fun <T> create(elemType: KType): ObjectConverter<T> {
        return factoryMap[elemType.toString()]?.invoke()?.get() ?: newDefaultConverter(elemType)
    }

    private fun <T> newDefaultConverter(elemType: KType): ObjectConverter<T> {
        @Suppress("UNCHECKED_CAST")
        val ks = serializer(elemType) as KSerializer<T>
        val parser = { it: JsonElement -> Json.decodeFromJsonElement(ks, it) }
        val serial = { e: T -> Json.encodeToJsonElement(ks, e) }
        return ObjectConverter(parser, serial)
    }

}