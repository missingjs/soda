package soda.groovy.unittest.validate

import java.lang.reflect.Type

class FeatureFactory {

    private static Map<Type, Closure<ObjectFeature>> factoryMap = [:]

    private static register(Type type, Closure<Integer> h, Closure<Boolean> eq) {
        factoryMap[type] = { new ObjectFeature(h, eq) }
    }

    static ObjectFeature create(Type type) {
        def factory = factoryMap[type] ?: {
            new ObjectFeature({ it.hashCode }, { x, y -> x == y})
        }
        factory()
    }

}
