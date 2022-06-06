package soda.kotlin.unittest

import kotlinx.serialization.json.*

class WorkInput(jstr: String) {

    private val root = Json.parseToJsonElement(jstr)

    val id: Int?
        get() = root.jsonObject["id"]?.jsonPrimitive?.int

    val hasExpected: Boolean
        get() = root.jsonObject["expected"]?.jsonPrimitive?.boolean ?: false

    val expected: JsonElement
        get() = root.jsonObject["expected"]!!

    fun arg(index: Int): JsonElement? = root.jsonObject["args"]?.jsonArray?.get(index)

    val arguments: JsonElement?
        get() = root.jsonObject["args"]

}