package soda.unittest.work.parse;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import soda.leetcode.*;
import soda.unittest.job.codec.CodecFactory;

public class ConverterFactory {

	private static final Map<Type, Supplier<ObjectConverter<?,?>>> factoryMap = new HashMap<>();

	private static void registerFactory(TypeRef<?> ref, Supplier<ObjectConverter<?,?>> factory) {
		registerFactory(ref.getRefType(), factory);
	}

	private static void registerFactory(Type type, Supplier<ObjectConverter<?,?>> factory) {
		factoryMap.put(type, factory);
	}

	private static <R,J> void registerFactory(TypeRef<R> ref, Function<J,R> parser, Function<R,J> unparser) {
		registerFactory(ref.getRefType(), parser, unparser);
	}

	private static <R,J> void registerFactory(Type type, Function<J,R> parser, Function<R,J> unparser) {
		registerFactory(type, () -> new ObjectConverter<R, J>() {
			@Override
			public R fromJsonSerializable(J j) {
				return parser.apply(j);
			}

			@Override
			public J toJsonSerializable(R r) {
				return unparser.apply(r);
			}
		});
	}

	private static Supplier<ObjectConverter<?,?>> getFactory(Type type) {
		return factoryMap.get(type);
	}
	
	static {
		registerFactory(ListNode.class, ListHelper::create, ListHelper::dump);
		registerFactory(TreeNode.class, TreeFactory::create, TreeFactory::dump);
		registerFactory(new TypeRef<List<NestedInteger>>() {}, NestedIntegerListConverter::new);
		registerFactory(new TypeRef<List<Character>>() {}, CharListConverter::new);
		registerFactory(new TypeRef<List<List<Character>>>() {}, CharList2dConverter::new);
		registerFactory(new TypeRef<List<ListNode>>() {}, ListNode1dConverter::new);
	}

	public static ObjectConverter<?,?> createConverter(Type type) {
		var factory = getFactory(type);
		if (factory != null) {
			return factory.get();
		}
		try {
			if (type instanceof Class) {
				return new CodecWrapConverter(CodecFactory.create((Class<?>) type));
			}
			return new DefaultObjectConverter();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
}
