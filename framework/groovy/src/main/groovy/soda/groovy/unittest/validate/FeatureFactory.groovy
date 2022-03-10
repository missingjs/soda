package soda.groovy.unittest.validate

import java.lang.reflect.Type

class FeatureFactory {

    private static Map<Type, Closure<ObjectFeature>> factoryMap = [:]

    private static registerFactory(Type type, Closure<Long> h, Closure<Boolean> eq) {
        registerFactory(type, { new ObjectFeature(h, eq) })
    }

    private static registerFactory(Type type, Closure<ObjectFeature> fact) {
        factoryMap[type] = fact
    }

    static ObjectFeature create(Type type) {
        def factory = factoryMap[type] ?: {
            new ObjectFeature({ it.hashCode() }, { x, y -> x == y})
        }
        factory()
    }

    static {
        registerFactory(double, { it.hashCode() }, { a, b -> Math.abs(a - b) < 1e-6 })
        registerFactory(Double, { create(double) })
    }

}
