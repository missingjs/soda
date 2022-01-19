package soda.unittest.validate;

import soda.unittest.Validators;
import soda.unittest.work.parse.TypeRef;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class ValidatorFactory {

    private static final Map<Type, Supplier<Validator<?>>> factoryMap = new HashMap<>();

    private static void registerFactory(Type type, Supplier<Validator<?>> fact) {
        factoryMap.put(type, fact);
    }

    private static void registerFactory(TypeRef<?> typeRef, Supplier<Validator<?>> fact) {
        factoryMap.put(typeRef.getRefType(), fact);
    }

    private static <T> Validator<T> wrap(BiPredicate<T, T> b) {
        return b::test;
    }

    static {
        registerFactory(double[].class, () -> wrap(Validators.forDoubleArray(true)));
        registerFactory(new TypeRef<List<Double>>() {}, () -> wrap(Validators.forList(Double.class, true)));
        registerFactory(double[][].class, () -> wrap(Validators.forDoubleArray2d(true, true)));
        registerFactory(new TypeRef<List<List<Double>>>() {}, () -> wrap(Validators.forList2d(Double.class, true, true)));
    }

    @SuppressWarnings("unchecked")
    public static <T> Validator<T> create(Type type) {
        var fact = factoryMap.get(type);
        if (fact != null) {
            return (Validator<T>) fact.get();
        }
        return (a, b) -> FeatureFactory.create(type).isEqual(a, b);
    }

}
