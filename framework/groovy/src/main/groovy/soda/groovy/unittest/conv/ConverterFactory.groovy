package soda.groovy.unittest.conv

import soda.groovy.leetcode.ListFactory
import soda.groovy.leetcode.ListNode
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

        registerFactory(ListNode, ListFactory::create, ListFactory::dump)
        registerFactory(ListNode[],
                { List<List<Integer>> data -> data.collect({ListFactory.create(it)}).toArray(ListNode[]::new) },
                { ListNode[] lists -> lists.collect({ListFactory.dump(it)}) }
        )
        registerFactory(new TypeRef<List<ListNode>>() {},
                { List<List<Integer>> data -> data.collect({ListFactory.create(it)}) },
                { List<ListNode> lists -> lists.collect({ListFactory.dump(it)}) }
        )
    }
}
