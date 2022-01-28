package soda.unittest.validate;

import soda.unittest.conv.TypeRef;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FeatureFactory {

    private static final Map<Type, Supplier<ObjectFeature<?>>> factoryMap = new HashMap<>();

    private static void registerFactory(Type type, Supplier<ObjectFeature<?>> fact) {
        factoryMap.put(type, fact);
    }

    private static void registerFactory(TypeRef<?> typeRef, Supplier<ObjectFeature<?>> fact) {
        factoryMap.put(typeRef.getRefType(), fact);
    }

    static {
        registerFactory(Double.class, DoubleFeature::new);
        registerFactory(double.class, DoubleFeature::new);
    }

    @SuppressWarnings("unchecked")
    public static <T> ObjectFeature<T> create(Type type) {
        var fact = factoryMap.get(type);
        if (fact != null) {
            return (ObjectFeature<T>) fact.get();
        }
        return new GenericFeature<>();
    }

}
