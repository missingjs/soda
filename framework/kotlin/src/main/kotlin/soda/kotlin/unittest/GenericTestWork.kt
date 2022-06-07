package soda.kotlin.unittest

import kotlinx.serialization.json.JsonElement
import soda.kotlin.unittest.conv.ConverterFactory
import soda.kotlin.unittest.validate.FeatureFactory
import kotlin.reflect.KClass

class GenericTestWork<T>(private val proxy: TaskProxy<T>) {

    var compareSerial: Boolean = false

    var validator: ((T, T) -> Boolean)? = null

    private var _arguments: List<Any?> = emptyList()

    fun run(text: String): String {
        val input = WorkInput(text)
        val result = proxy.execute(input)
        _arguments = proxy.arguments

        val retType = proxy.returnType
        val resConv = ConverterFactory.create<T>(retType)
        val serialResult = resConv.toJsonSerializable(result)
        val output = WorkOutput()
        output.id = input.id ?: -1
        output.result = serialResult
        output.elapse = proxy.elapseMillis

        var success = true
        if (input.hasExpected) {
            if (compareSerial && validator == null) {
                success = input.expected.toString() == serialResult.toString()
            } else {
                val expect = resConv.fromJsonSerializable(input.expected)
                success = validator?.invoke(expect, result) ?: FeatureFactory.create<T>(retType).isEqual(expect, result)
            }
        }
        output.success = success
        return output.toJsonString()
    }

    companion object {
        inline fun <reified P1, reified R> create(noinline func: (P1) -> R): GenericTestWork<R> {
            return GenericTestWork(Task1(func))
        }

        inline fun <reified P1> createVoid(noinline func: (P1) -> Unit): GenericTestWork<P1> {
            return GenericTestWork(Task1 { p: P1 -> func(p); p })
        }

        inline fun <reified P1, reified P2, reified R> create(noinline func: (P1, P2) -> R): GenericTestWork<R> {
            return GenericTestWork(Task2(func))
        }

        fun forStruct(structClass: KClass<*>): GenericTestWork<JsonElement> {
            return create(StructTester(structClass)::test)
        }
    }

}
