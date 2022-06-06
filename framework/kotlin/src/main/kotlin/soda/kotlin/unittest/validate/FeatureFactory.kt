package soda.kotlin.unittest.validate

import kotlin.reflect.KType

object FeatureFactory {

    private val factoryMap = mutableMapOf<String, () -> OFHandle>()

    fun <T> create(type: KType): ObjectFeature<T> {
        val typeName = type.toString()
        return factoryMap[typeName]?.invoke()?.get() ?: newDefaultFeature()
    }

    private fun <T> newDefaultFeature(): ObjectFeature<T> {
        return ObjectFeature({ it.hashCode().toLong() }, { a, b -> a == b })
    }
}
