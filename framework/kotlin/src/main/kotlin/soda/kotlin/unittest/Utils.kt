package soda.kotlin.unittest

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import soda.kotlin.unittest.conv.ConverterFactory
import kotlin.reflect.KType

object Utils {
    fun fromStdin(): String {
        return generateSequence(::readLine).joinToString("\n")
    }

    fun parseArguments(types: List<KType>, rawParams: JsonElement): List<Any?> {
        return types.mapIndexed { index, type ->
            ConverterFactory.create<Any>(type).fromJsonSerializable(rawParams.jsonArray[index])
        }
    }
}