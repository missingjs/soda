package soda.unittest.validate;

import soda.unittest.Utils;
import soda.unittest.conv.ConvUtils;
import soda.unittest.conv.TypeRef;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FeatureFactory {

    private static final Map<Type, Supplier<ObjectFeature<?>>> factoryMap = new HashMap<>();

    private static <T> void registerFactory(Type type, Supplier<ObjectFeature<T>> fact) {
        factoryMap.put(type, fact::get);
    }

    private static <T> void registerFactory(TypeRef<T> typeRef, Supplier<ObjectFeature<T>> fact) {
        registerFactory(typeRef.getRefType(), fact);
    }

    static {
        registerFactory(double.class, DoubleFeature::new);
        registerFactory(Double.class, DoubleFeature::new);
        registerFactory(double[].class, DoubleArrayFeature::new);
        registerFactory(
                new TypeRef<List<Double>>() {},
                () -> new ListFeature<>(create(Double.class))
        );
        registerFactory(double[][].class, DoubleArray2dFeature::new);
        registerFactory(
                new TypeRef<List<List<Double>>>() {},
                () -> new ListFeature<>(create(new TypeRef<>() {}))
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> ObjectFeature<T> create(Type type) {
        var fact = factoryMap.get(type);
        if (fact != null) {
            return Utils.cast(fact.get());
        }
        return new GenericFeature<>();
    }

    public static <T> ObjectFeature<T> create(TypeRef<T> typeRef) {
        return create(typeRef.getRefType());
    }

    private static class DoubleArrayFeature implements ObjectFeature<double[]> {

        private ObjectFeature<List<Double>> proxy = create(new TypeRef<>() {});

        @Override
        public long hash(double[] obj) {
            return proxy.hash(ConvUtils.toList(obj));
        }

        @Override
        public boolean isEqual(double[] a, double[] b) {
            return proxy.isEqual(ConvUtils.toList(a), ConvUtils.toList(b));
        }
    }

    private static class DoubleArray2dFeature implements ObjectFeature<double[][]> {

        private ObjectFeature<List<List<Double>>> proxy = create(new TypeRef<>() {});

        @Override
        public long hash(double[][] obj) {
            return proxy.hash(ConvUtils.toList2d(obj));
        }

        @Override
        public boolean isEqual(double[][] a, double[][] b) {
            return proxy.isEqual(ConvUtils.toList2d(a), ConvUtils.toList2d(b));
        }
    }

}
