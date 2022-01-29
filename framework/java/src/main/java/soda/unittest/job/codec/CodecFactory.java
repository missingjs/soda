package soda.unittest.job.codec;

import java.util.HashMap;
import java.util.Map;

public class CodecFactory {

	public static ICodec<?,?> create(Class<?> objClass) throws Exception {
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
