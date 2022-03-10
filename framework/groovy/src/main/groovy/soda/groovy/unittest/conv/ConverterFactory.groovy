package soda.groovy.unittest.conv

import soda.groovy.unittest.validate.ObjectFeature

import java.lang.reflect.Type

class ConverterFactory {

    private static Map<Type, Closure<ObjectConverter>> factoryMap = [:]

    private static registerFactory(Type type, Closure parser, Closure serializer) {
        registerFactory(type, { t -> new ObjectConverter(parser, serializer) })
    }

    private static registerFactory(Type type, Closure factory) {
        factoryMap[type] = factory
    }

    private static registerFactory(TypeRef ref, Closure parser, Closure serializer) {
        registerFactory(ref.refType, parser, serializer)
    }

    private static registerFactory(TypeRef ref, Closure factory) {
        registerFactory(ref.refType, factory)
    }

    static ObjectConverter create(Type type) {
        def factory = factoryMap[type] ?: { t ->
            new ObjectConverter({ it }, { it })
        }
        factory(type)
    }

    static {
        registerFactory(int[], ConvUtils::toIntArray, ConvUtils::fromIntArray)
        registerFactory(int[][], ConvUtils::toIntArray2d, ConvUtils::fromIntArray2d)
        registerFactory(double[], ConvUtils::toDoubleArray, ConvUtils::fromDoubleArray)
        registerFactory(double[][], ConvUtils::toDoubleArray2d, ConvUtils::fromDoubleArray2d)

        registerFactory(char, ConvUtils::toChar, ConvUtils::fromChar)
        registerFactory(Character, { t -> create(char) })
        registerFactory(char[], ConvUtils::toCharArray, ConvUtils::fromCharArray)
        registerFactory(char[][], ConvUtils::toCharArray2d, ConvUtils::fromCharArray2d)
        registerFactory(new TypeRef<List<Character>>() {}, ConvUtils::toCharList, ConvUtils::fromCharList)
        registerFactory(new TypeRef<List<List<Character>>>() {}, ConvUtils::toCharList2d, ConvUtils::fromCharList2d)
    }
}
