package soda.kotlin.unittest

import kotlin.reflect.full.createType
import kotlin.reflect.typeOf

class GenericTestWork<Return>(private val proxy: TaskProxy<Return>) {

    companion object {
        inline fun <reified P1, reified R> create(noinline func: (P1) -> R): GenericTestWork<R> {
            return GenericTestWork(Task1(func))
        }

        inline fun <reified P1, reified P2, reified R> create(noinline func: (P1, P2) -> R): GenericTestWork<R> {
            return GenericTestWork(Task2(func))
        }
    }

}

class Solution {
    fun add(a: Int, b: Double): String {
        return "$a + $b"
    }

    fun add1(a: Int): Int {
        return a + 1024
    }
}

fun main() {
    GenericTestWork.create(Solution()::add)

    // val su = Solution()
    val m = Solution()::add
    for (p in m.parameters) {
        println(p.type)
    }

    println(m.returnType)
//    val types = listOf(String::class, Int::class)

    val t = Solution::class.createType()
    val t2 = Solution::class.createType()
    println(t == t2)
}
