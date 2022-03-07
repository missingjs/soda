package soda.groovy.unittest.conv

import java.lang.reflect.Type

class ConverterFactory {

    private static Map<Type, Closure> factoryMap = [:]

    private static register(Type type, Closure parser, Closure serializer) {
        factoryMap[type] = { new ObjectConverter(parser, serializer) }
    }

    static ObjectConverter create(Type type) {
        def factory = factoryMap[type] ?: {
            new ObjectConverter({ it }, { it })
        }
        factory()
    }
}
