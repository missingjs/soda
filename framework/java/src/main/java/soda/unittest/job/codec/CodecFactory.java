package soda.unittest.job.codec;

import java.util.HashMap;
import java.util.Map;

public class CodecFactory {
	
	private static Map<Class<?>, Class<? extends ICodec<?,?>>> codecMap;
	
	static {
		codecMap = new HashMap<>();
//		codecMap.put(int[].class, IntArrayCodec.class);
//		codecMap.put(int[][].class, IntArray2dCodec.class);
//        codecMap.put(char.class, CharCodec.class);
//        codecMap.put(Character.class, CharCodec.class);
//		codecMap.put(char[].class, CharArrayCodec.class);
//		codecMap.put(char[][].class, CharArray2dCodec.class);
	}

	public static ICodec<?,?> create(Class<?> objClass) throws Exception {
		Class<? extends ICodec<?,?>> cls = codecMap.get(objClass);
		if (cls != null) {
			return cls.getDeclaredConstructor().newInstance();
		}
		
		if (objClass.isArray()) {
			return new ObjectArrayCodec(getElementType(objClass), getDimension(objClass));
		}
		
		return new DefaultCodec();
	}
	
	private static Class<?> getElementType(Class<?> cls) {
		return cls.isArray() ? getElementType(cls.getComponentType()) : cls;
	}
	
	private static int getDimension(Class<?> cls) {
		return cls.isArray() ? 1 + getDimension(cls.getComponentType()) : 0;
	}
	
}
