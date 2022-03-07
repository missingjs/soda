package soda.groovy.unittest.conv

import soda.groovy.unittest.validate.ObjectFeature

import java.lang.reflect.Type

class ConverterFactory {

    private static Map<Type, Closure<ObjectConverter>> factoryMap = [:]

    private static register(Type type, Closure parser, Closure serializer) {
        factoryMap[type] = { t -> new ObjectConverter(parser, serializer) }
    }

    private static register(Type type, Closure factory) {
        factoryMap[type] = factory
    }

    static ObjectConverter create(Type type) {
        def factory = factoryMap[type] ?: { t ->
            new ObjectConverter({ it }, { it })
        }
        factory(type)
    }

    static {
        register(char, { String s -> s[0] as Character }, { char ch -> ch as String })
        register(Character, { t -> create(char) })
    }
}
