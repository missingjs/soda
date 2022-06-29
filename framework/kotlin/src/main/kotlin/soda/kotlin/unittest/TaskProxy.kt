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

abstract class TaskBase<R>(private val argTypes: List<KType>, private val resKType: KType): TaskProxy<R> {

    private var args: List<Any?> = emptyList()

    private var elapse: Double = 0.0

    protected abstract fun run(): R

    override fun execute(input: WorkInput): R {
        args = Utils.parseArguments(argTypes, input.arguments)
        val startNano = System.nanoTime()
        val result = run()
        val endNano = System.nanoTime()
        elapse = (endNano - startNano) / 1e6
        return result
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <A> arg(index: Int): A = args[index] as A

    override val elapseMillis: Double
        get() = elapse

    override val returnType: KType
        get() = resKType

    override val argumentTypes: List<KType>
        get() = argTypes

    override val arguments: List<Any?>
        get() = args

}

class Task1<P1, R>(
    private val func: (P1) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified R
                > invoke(noinline func: (P1) -> R):
                Task1<P1, R> {
            val argTypes = listOf(
                typeOf<P1>()
            )
            return Task1(func, argTypes, typeOf<R>())
        }
    }
}

class Task2<P1, P2, R>(
    private val func: (P1, P2) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0), arg(1)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified P2, reified R
                > invoke(noinline func: (P1, P2) -> R):
                Task2<P1, P2, R> {
            val argTypes = listOf(
                typeOf<P1>(), typeOf<P2>()
            )
            return Task2(func, argTypes, typeOf<R>())
        }
    }
}

class Task3<P1, P2, P3, R>(
    private val func: (P1, P2, P3) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0), arg(1), arg(2)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified P2, reified P3, reified R
                > invoke(noinline func: (P1, P2, P3) -> R):
                Task3<P1, P2, P3, R> {
            val argTypes = listOf(
                typeOf<P1>(), typeOf<P2>(), typeOf<P3>()
            )
            return Task3(func, argTypes, typeOf<R>())
        }
    }
}

class Task4<P1, P2, P3, P4, R>(
    private val func: (P1, P2, P3, P4) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0), arg(1), arg(2), arg(3)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified P2, reified P3, reified P4,
                reified R
                > invoke(noinline func: (P1, P2, P3, P4) -> R):
                Task4<P1, P2, P3, P4, R> {
            val argTypes = listOf(
                typeOf<P1>(), typeOf<P2>(), typeOf<P3>(), typeOf<P4>()
            )
            return Task4(func, argTypes, typeOf<R>())
        }
    }
}

class Task5<P1, P2, P3, P4, P5, R>(
    private val func: (P1, P2, P3, P4, P5) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0), arg(1), arg(2), arg(3),
            arg(4)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified R
                > invoke(noinline func: (P1, P2, P3, P4, P5) -> R):
                Task5<P1, P2, P3, P4, P5, R> {
            val argTypes = listOf(
                typeOf<P1>(), typeOf<P2>(), typeOf<P3>(), typeOf<P4>(),
                typeOf<P5>()
            )
            return Task5(func, argTypes, typeOf<R>())
        }
    }
}

class Task6<P1, P2, P3, P4, P5, P6, R>(
    private val func: (P1, P2, P3, P4, P5, P6) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0), arg(1), arg(2), arg(3),
            arg(4), arg(5)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified P6, reified R
                > invoke(noinline func: (P1, P2, P3, P4, P5, P6) -> R):
                Task6<P1, P2, P3, P4, P5, P6, R> {
            val argTypes = listOf(
                typeOf<P1>(), typeOf<P2>(), typeOf<P3>(), typeOf<P4>(),
                typeOf<P5>(), typeOf<P6>()
            )
            return Task6(func, argTypes, typeOf<R>())
        }
    }
}

class Task7<P1, P2, P3, P4, P5, P6, P7, R>(
    private val func: (P1, P2, P3, P4, P5, P6, P7) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0), arg(1), arg(2), arg(3),
            arg(4), arg(5), arg(6)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified P6, reified P7, reified R
                > invoke(noinline func: (P1, P2, P3, P4, P5, P6, P7) -> R):
                Task7<P1, P2, P3, P4, P5, P6, P7, R> {
            val argTypes = listOf(
                typeOf<P1>(), typeOf<P2>(), typeOf<P3>(), typeOf<P4>(),
                typeOf<P5>(), typeOf<P6>(), typeOf<P7>()
            )
            return Task7(func, argTypes, typeOf<R>())
        }
    }
}

class Task8<P1, P2, P3, P4, P5, P6, P7, P8, R>(
    private val func: (P1, P2, P3, P4, P5, P6, P7, P8) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0), arg(1), arg(2), arg(3),
            arg(4), arg(5), arg(6), arg(7)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified P6, reified P7, reified P8,
                reified R
                > invoke(noinline func: (P1, P2, P3, P4, P5, P6, P7, P8) -> R):
                Task8<P1, P2, P3, P4, P5, P6, P7, P8, R> {
            val argTypes = listOf(
                typeOf<P1>(), typeOf<P2>(), typeOf<P3>(), typeOf<P4>(),
                typeOf<P5>(), typeOf<P6>(), typeOf<P7>(), typeOf<P8>()
            )
            return Task8(func, argTypes, typeOf<R>())
        }
    }
}

class Task9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R>(
    private val func: (P1, P2, P3, P4, P5, P6, P7, P8, P9) -> R,
    argTypes: List<KType>,
    resKType: KType
): TaskBase<R>(argTypes, resKType) {
    override fun run(): R {
        return func(
            arg(0), arg(1), arg(2), arg(3),
            arg(4), arg(5), arg(6), arg(7),
            arg(8)
        )
    }

    companion object {
        inline operator fun <
                reified P1, reified P2, reified P3, reified P4,
                reified P5, reified P6, reified P7, reified P8,
                reified P9, reified R
                > invoke(noinline func: (P1, P2, P3, P4, P5, P6, P7, P8, P9) -> R):
                Task9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> {
            val argTypes = listOf(
                typeOf<P1>(), typeOf<P2>(), typeOf<P3>(), typeOf<P4>(),
                typeOf<P5>(), typeOf<P6>(), typeOf<P7>(), typeOf<P8>(),
                typeOf<P9>()
            )
            return Task9(func, argTypes, typeOf<R>())
        }
    }
}
