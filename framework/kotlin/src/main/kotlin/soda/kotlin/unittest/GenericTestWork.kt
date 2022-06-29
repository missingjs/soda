package soda.kotlin.unittest

import kotlinx.serialization.json.JsonElement
import soda.kotlin.unittest.conv.ConverterFactory
import soda.kotlin.unittest.validate.FeatureFactory
import kotlin.reflect.KClass

class GenericTestWork<T>(private val proxy: TaskProxy<T>) {

    var compareSerial: Boolean = false

    var validator: ((T, T) -> Boolean)? = null

    private var _arguments: List<Any?> = emptyList()

    val arguments: List<Any?>
        get() = _arguments

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
        inline fun <
                reified P1, reified R
                > create(noinline func: (P1) -> R): GenericTestWork<R> {
            return GenericTestWork(Task1(func))
        }

        inline fun <
                reified P1
                > createVoid(noinline func: (P1) -> Unit): GenericTestWork<P1> {
            return GenericTestWork(Task1 { p: P1 -> func(p); p })
        }

        inline fun <
                reified P1, reified P2, reified R
                > create(noinline func: (P1, P2) -> R): GenericTestWork<R> {
            return GenericTestWork(Task2(func))
        }

        inline fun <
                reified P1, reified P2, reified P3, reified R
                > create(noinline func: (P1, P2, P3) -> R): GenericTestWork<R> {
            return GenericTestWork(Task3(func))
        }

        inline fun <
                reified P1, reified P2, reified P3, reified P4,
                reified R
                > create(noinline func: (P1, P2, P3, P4) -> R): GenericTestWork<R> {
            return GenericTestWork(Task4(func))
        }

        inline fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified R
                > create(noinline func: (P1, P2, P3, P4, P5) -> R): GenericTestWork<R> {
            return GenericTestWork(Task5(func))
        }

        inline fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified P6, reified R
                > create(noinline func: (P1, P2, P3, P4, P5, P6) -> R): GenericTestWork<R> {
            return GenericTestWork(Task6(func))
        }

        inline fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified P6, reified P7, reified R
                > create(noinline func: (P1, P2, P3, P4, P5, P6, P7) -> R): GenericTestWork<R> {
            return GenericTestWork(Task7(func))
        }

        inline fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified P6, reified P7, reified P8,
                reified R
                > create(noinline func: (P1, P2, P3, P4, P5, P6, P7, P8) -> R): GenericTestWork<R> {
            return GenericTestWork(Task8(func))
        }

        inline fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified P6, reified P7, reified P8,
                reified P9, reified R
                > create(noinline func: (P1, P2, P3, P4, P5, P6, P7, P8, P9) -> R): GenericTestWork<R> {
            return GenericTestWork(Task9(func))
        }

        fun forStruct(structClass: KClass<*>): GenericTestWork<JsonElement> {
            return create(StructTester(structClass)::test)
        }
    }

}
