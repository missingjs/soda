package soda.kotlin.unittest.conv

import kotlinx.serialization.json.*
import soda.kotlin.leetcode.NestedInteger

object NestedIntegerConverter {
    fun parse(obj: JsonElement): NestedInteger {
        return when (obj) {
            is JsonPrimitive -> NestedInteger(obj.int)
            is JsonArray -> {
                val ni = NestedInteger()
                for (elem in obj.jsonArray) {
                    ni.add(parse(elem))
                }
                ni
            }
            else -> throw RuntimeException("[NestedInteger] error type of object: ${obj::class}")
        }
    }

    fun serialize(ni: NestedInteger): JsonElement {
        return if (ni.isInteger()) {
            Json.encodeToJsonElement(ni.getInteger())
        } else {
            Json.encodeToJsonElement(ni.getList()?.map(this::serialize))
        }
    }

    fun create(): ObjectConverter<NestedInteger> {
        return ObjectConverter(this::parse, this::serialize)
    }
}