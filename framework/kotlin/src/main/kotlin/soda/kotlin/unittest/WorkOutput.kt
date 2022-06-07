package soda.kotlin.unittest

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

@Serializable
class WorkOutput {
    var id: Int = 0
    @Required
    var success: Boolean = false
    var result: JsonElement = JsonNull
    var elapse: Double = 0.0

    fun toJsonString(): String = Json.encodeToString(this)
}
