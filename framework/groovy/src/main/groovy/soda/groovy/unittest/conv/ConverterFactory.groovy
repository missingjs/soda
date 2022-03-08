package soda.groovy.unittest.conv

import soda.groovy.unittest.validate.ObjectFeature

import java.lang.reflect.Type

class ConverterFactory {

    private static Map<Type, Closure<ObjectConverter>> factoryMap = [:]

    private static register(Type type, Closure parser, Closure serializer) {
        register(type, { t -> new ObjectConverter(parser, serializer) })
    }

    private static register(Type type, Closure factory) {
        factoryMap[type] = factory
    }

    private static register(TypeRef ref, Closure parser, Closure serializer) {
        register(ref.refType, parser, serializer)
    }

    private static register(TypeRef ref, Closure factory) {
        register(ref.refType, factory)
    }

    static ObjectConverter create(Type type) {
        def factory = factoryMap[type] ?: { t ->
            new ObjectConverter({ it }, { it })
        }
        factory(type)
    }

    static {
        register(int[], ConvUtils::toIntArray, ConvUtils::fromIntArray)

        register(char, ConvUtils::toChar, ConvUtils::fromChar)
        register(Character, { t -> create(char) })
        register(char[], ConvUtils::toCharArray, ConvUtils::fromCharArray)
        register(char[][], ConvUtils::toCharArray2d, ConvUtils::fromCharArray2d)
        register(new TypeRef<List<Character>>() {}, ConvUtils::toCharList, ConvUtils::fromCharList)
        register(new TypeRef<List<List<Character>>>() {}, ConvUtils::toCharList2d, ConvUtils::fromCharList2d)
    }
}
