package soda.kotlin.unittest.conv

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.json.*
import soda.kotlin.leetcode.ListFactory
import soda.kotlin.leetcode.ListNode
import kotlin.reflect.KType
import kotlin.reflect.typeOf

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

    private inline fun <reified T> registerFactory(noinline factory: () -> ObjectConverter<T>) {
        factoryMap[typeOf<T>().toString()] = { OCHandle(factory()) }
    }

    private inline fun <reified Std, reified Cust> registerFactory(
        noinline fromStd: (Std) -> Cust,
        noinline toStd: (Cust) -> Std
    ) {
        registerFactory {
            ObjectConverter(
                { fromStd(Json.decodeFromJsonElement(it)) },
                { Json.encodeToJsonElement(toStd(it)) }
            )
        }
    }

    init {
        // ListNode?
        registerFactory(ListFactory::create, ListFactory::dump)

        // List<ListNode?>
        registerFactory(
            { data: List<List<Int>> -> data.map(ListFactory::create) },
            { nodes: List<ListNode?> -> nodes.map(ListFactory::dump) }
        )

        // Array<ListNode?>
        registerFactory(
            { data: List<List<Int>> -> data.map(ListFactory::create).toTypedArray() },
            { nodes: Array<ListNode?> -> nodes.map(ListFactory::dump).toList() }
        )
    }

}
