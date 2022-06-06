package soda.kotlin.unittest

import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface TaskProxy<R> {

    val returnType: KType

    val argumentTypes: List<KType>

    val arguments: List<Any?>

    fun execute(input: WorkInput): R

    val elapseMillis: Double

}

abstract class TaskBase<R>(private val _resKType: KType) : TaskProxy<R> {

    private var _elapse: Double = -1.0
    private var _argTypes: List<KType> = emptyList()
    private var _args: List<Any?> = emptyList()

    protected fun run(argTypes: List<KType>, input: WorkInput, task: () -> R): R {
        _argTypes = argTypes
         _args = Utils.parseArguments(argTypes, input.arguments)
        val startNano = System.nanoTime()
        val result = task()
        val endNano = System.nanoTime()
        _elapse = (endNano - startNano) / 1e6
        return result
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <A> arg(index: Int): A = _args[index] as A

    override val elapseMillis: Double
        get() = _elapse

    override val returnType: KType
        get() = _resKType

    override val argumentTypes: List<KType>
        get() = _argTypes

    override val arguments: List<Any?>
        get() = _args

}

class Task1<P1, R>(private val func: (P1) -> R, private val argTypes: List<KType>, resKType: KType) :
    TaskBase<R>(resKType) {
    override fun execute(input: WorkInput): R {
        return run(argTypes, input) { func(arg(0)) }
    }

    companion object {
        inline operator fun <reified P1, reified R> invoke(noinline func: (P1) -> R): Task1<P1, R> {
            return Task1(func, listOf(typeOf<P1>()), typeOf<R>())
        }
    }
}

class Task2<P1, P2, R>(private val func: (P1, P2) -> R, private val argTypes: List<KType>, resKType: KType) :
    TaskBase<R>(resKType) {
    override fun execute(input: WorkInput): R {
        return run(argTypes, input) { func(arg(0), arg(1)) }
    }

    companion object {
        inline operator fun <reified P1, reified P2, reified R> invoke(noinline func: (P1, P2) -> R): Task2<P1, P2, R> {
            return Task2(func, listOf(typeOf<P1>(), typeOf<P2>()), typeOf<R>())
        }
    }
}
