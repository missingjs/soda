package soda.unittest.conv;

import soda.leetcode.*;
import soda.unittest.Utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConverterFactory {

	private static final Map<Type, Supplier<ObjConv>> factoryMap = new HashMap<>();

	private static <R,J> void registerFactory(Class<R> klass, Supplier<ObjectConverter<R,J>> factory) {
		regFact(klass, factory);
	}

	private static <R,J> void registerFactory(TypeRef<R> ref, Supplier<ObjectConverter<R,J>> factory) {
		regFact(ref.getRefType(), factory);
	}

	private static <R,J> void regFact(Type type, Supplier<ObjectConverter<R,J>> factory) {
		factoryMap.put(type, factory::get);
	}

	private static <R,J> void registerFactory(Class<R> klass, Function<J,R> parser, Function<R,J> serializer) {
		regFact(klass, parser, serializer);
	}

	private static <R,J> void registerFactory(TypeRef<R> ref, Function<J,R> parser, Function<R,J> serializer) {
		regFact(ref.getRefType(), parser, serializer);
	}

	private static <R,J> void regFact(Type type, Function<J,R> parser, Function<R,J> serializer) {
		regFact(type, () -> new ObjectConverter<R, J>() {
			@Override public R fromJsonSerializable(J j) {
				return parser.apply(j);
			}
			@Override public J toJsonSerializable(R r) {
				return serializer.apply(r);
			}
		});
	}
	
	static {
		registerFactory(int[].class, ConvUtils::toIntArray, ConvUtils::toList);
		registerFactory(int[][].class, ConvUtils::toIntArray2d, ConvUtils::toList2d);
		registerFactory(long[].class, ConvUtils::toLongArray, ConvUtils::toList);
		registerFactory(long[][].class, ConvUtils::toLongArray2d, ConvUtils::toList2d);
		registerFactory(double[].class, ConvUtils::toDoubleArray, ConvUtils::toList);
		registerFactory(double[][].class, ConvUtils::toDoubleArray2d, ConvUtils::toList2d);

		registerFactory(char.class, ConvUtils::toChar, ConvUtils::toStr);
		registerFactory(Character.class, ConvUtils::toChar, ConvUtils::toStr);
		registerFactory(char[].class, ConvUtils::toCharArray, ConvUtils::toStrList);
		registerFactory(char[][].class, ConvUtils::toCharArray2d, ConvUtils::toStrList2d);
		registerFactory(new TypeRef<>() {}, ConvUtils::toCharList, ConvUtils::fromCharList);
		registerFactory(new TypeRef<>() {}, ConvUtils::toCharList2d, ConvUtils::fromCharList2d);

		registerFactory(String[].class, ConvUtils::toStrArray, ConvUtils::toStrList);
		registerFactory(String[][].class, ConvUtils::toStrArray2d, ConvUtils::toStrList2d);

		registerFactory(ListNode.class, ListHelper::create, ListHelper::dump);
		registerFactory(new TypeRef<>() {}, ListNode1dConverter::new);
		registerFactory(ListNode[].class, ListNode1dConverter::forArray);

		registerFactory(TreeNode.class, TreeFactory::create, TreeFactory::dump);
		registerFactory(NestedInteger.class, NestedIntegerConverter::new);
		registerFactory(new TypeRef<>() {}, NestedIntegerListConverter::new);
	}

	public static <T> ObjectConverter<T, Object> create(Type type) {
		var factory = factoryMap.get(type);
		if (factory != null) {
			return factory.get().as();
		}
		return new ObjectConverter<>() {
			@Override public T fromJsonSerializable(Object j) {
				return Utils.cast(j);
			}
			@Override public Object toJsonSerializable(T r) {
				return r;
			}
		};
	}
	
}
