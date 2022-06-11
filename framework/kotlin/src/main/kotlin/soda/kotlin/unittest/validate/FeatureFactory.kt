package soda.kotlin.unittest.validate

import kotlin.reflect.KType
import kotlin.reflect.typeOf

object FeatureFactory {

    private val factoryMap = mutableMapOf<String, () -> OFHandle>()

    fun <T> create(type: KType): ObjectFeature<T> {
        val typeName = type.toString()
        return factoryMap[typeName]?.invoke()?.get() ?: CommonFeatureFactory.forDefault()
    }

    inline fun <reified T> create(): ObjectFeature<T> {
        return create(typeOf<T>())
    }

    private inline fun <reified T> registerFactory(noinline factory: () -> ObjectFeature<T>) {
        factoryMap[typeOf<T>().toString()] = { OFHandle(factory()) }
    }

    init {
        registerFactory { CommonFeatureFactory.forDouble() }
        registerFactory { CommonFeatureFactory.forList(create<Double>()) }
        registerFactory { CommonFeatureFactory.forList(create<List<Double>>()) }
        registerFactory { CommonFeatureFactory.forDoubleArray() }
        registerFactory { CommonFeatureFactory.forDoubleArray2d() }
    }
}
