package soda.groovy.unittest.validate

import java.lang.reflect.Type

import soda.groovy.unittest.conv.TypeRef

class FeatureFactory {

    private static Map<Type, Closure<ObjectFeature>> factoryMap = [:]

    private static registerFactory(Type type, Closure<Long> h, Closure<Boolean> eq) {
        registerFactory(type, { new ObjectFeature(h, eq) })
    }

    private static registerFactory(Type type, Closure<ObjectFeature> fact) {
        factoryMap[type] = fact
    }

    private static registerFactory(TypeRef ref, Closure<ObjectFeature> fact) {
        registerFactory(ref.getRefType(), fact)
    }

    static ObjectFeature create(Type type) {
        def factory = factoryMap[type] ?: {
            new ObjectFeature({ it.hashCode() }, { x, y -> x == y})
        }
        factory()
    }

    static ObjectFeature create(TypeRef ref) {
        create(ref.getRefType())
    }

    static {
        // double
        registerFactory(double, { it.hashCode() }, { a, b -> Math.abs(a - b) < 1e-6 })
        registerFactory(Double, { create(double) })

        // double array
        def doubleArrayFactory = { ->
            def proxy = create(new TypeRef<List<Double>>() {})
            def hashF = { double[] obj -> proxy.hash(obj.toList()) }
            def equalF = { double[] x, double[] y -> proxy.isEqual(x.toList(), y.toList()) }
            new ObjectFeature(hashF, equalF)
        }
        registerFactory(double[], doubleArrayFactory)
        registerFactory(new TypeRef<List<Double>>() {}, { ListFeatureFactory.ordered(create(Double)) })

        // 2d double array 
        def toList2d = { double[][] obj -> obj.collect({ it.toList() }).toList() }
        def doubleArray2dFactory = { ->
            def proxy = create(new TypeRef<List<List<Double>>>() {})
            def hashF = { double[][] obj -> proxy.hash(toList2d(obj)) }
            def equalF = { double[][] x, double[][] y -> proxy.isEqual(toList2d(x), toList2d(y)) }
            new ObjectFeature(hashF, equalF)
        }
        registerFactory(double[][], doubleArray2dFactory)
        registerFactory(new TypeRef<List<List<Double>>>() {}, { ListFeatureFactory.ordered(create(new TypeRef<List<Double>>() {})) })
    }

}
