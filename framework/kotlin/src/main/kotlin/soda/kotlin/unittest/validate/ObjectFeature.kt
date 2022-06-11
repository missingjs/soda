package soda.kotlin.unittest.validate

import kotlin.math.absoluteValue

open class OFBase

class ObjectFeature<in T>(private val hs: (T) -> Long, private val eq: (T, T) -> Boolean): OFBase() {
    fun hash(obj: T): Long = hs(obj)
    fun isEqual(a: T, b: T): Boolean = eq(a, b)
}

class OFHandle(private val objFeat: OFBase) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(): ObjectFeature<T> = objFeat as ObjectFeature<T>
}

object CommonFeatureFactory {

    private fun <T> hashFunc(): (T?) -> Long {
        return { obj: T? -> obj?.hashCode()?.toLong() ?: 0 }
    }

    private fun <T> equalFunc(): (T?, T?) -> Boolean {
        return { a: T?, b: T? -> a == b }
    }

    fun <T> forDefault(): ObjectFeature<T> {
        return ObjectFeature(hashFunc(), equalFunc())
    }

    fun forDouble(): ObjectFeature<Double> {
        return ObjectFeature(hashFunc()) { a, b -> (a - b).absoluteValue < 1e-6 }
    }

    fun forDoubleArray(): ObjectFeature<DoubleArray> {
        val proxy = FeatureFactory.create<List<Double>>()
        return ObjectFeature(
            { proxy.hash(it.toList()) },
            { a, b -> proxy.isEqual(a.toList(), b.toList()) }
        )
    }

    fun forDoubleArray2d(): ObjectFeature<Array<DoubleArray>> {
        val proxy = FeatureFactory.create<List<List<Double>>>()
        val toList2d = { arr: Array<DoubleArray> ->
            arr.map { it.toList() }.toList()
        }
        return ObjectFeature(
            { proxy.hash(toList2d(it)) },
            { a, b -> proxy.isEqual(toList2d(a), toList2d(b)) }
        )
    }

    fun <T> forList(elemFeat: ObjectFeature<T>): ObjectFeature<List<T>> {
        return ObjectFeature(
            { it.map(elemFeat::hash).reduce { a, b -> a * 133 + b } },
            { a, b -> a.size == b.size && a.zip(b).all { elemFeat.isEqual(it.first, it.second) } }
        )
    }

    fun <T> forUnorderedList(elemFeat: ObjectFeature<T>): ObjectFeature<List<T>> {
        return ObjectFeature(
            { it.map(elemFeat::hash).sorted().reduce { a, b -> a * 133 + b } },
            { a, b -> unorderedListEqual(elemFeat, a, b) }
        )
    }

    private fun <T> unorderedListEqual(elemFeat: ObjectFeature<T>, a: List<T>, b: List<T>): Boolean {
        if (a.size != b.size) {
            return false
        }
        val xmap = XMap<T, Int>(elemFeat)
        for (e in a) {
            xmap[e] = xmap.getOrDefault(e, 0) + 1
        }
        for (e in b) {
            if (e !in xmap) {
                return false
            }
            val c = xmap[e]!! - 1
            if (c == 0) {
                xmap -= e
            } else {
                xmap[e] = c
            }
        }
        return true
    }

}
