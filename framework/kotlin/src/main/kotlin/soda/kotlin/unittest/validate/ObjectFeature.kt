package soda.kotlin.unittest.validate

class ObjectFeature<in T>(private val hs: (T) -> Long, private val eq: (T, T) -> Boolean) {
    fun hash(obj: T): Long = hs(obj)
    fun isEqual(a: T, b: T): Boolean = eq(a, b)
}

class OFHandle(private val objFeat : ObjectFeature<Nothing>) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(): ObjectFeature<T> = objFeat as ObjectFeature<T>
}
