package soda.groovy.unittest

import soda.groovy.unittest.validate.FeatureFactory
import soda.groovy.unittest.validate.ListFeatureFactory
import soda.groovy.unittest.validate.ObjectFeature

import java.lang.reflect.Type

class Validators {

    private static Closure<Boolean> _forList(boolean ordered, ObjectFeature elemFeat) {
        ordered
                ? ListFeatureFactory.ordered(elemFeat)::isEqual
                : ListFeatureFactory.unordered(elemFeat)::isEqual
    }

    static Closure<Boolean> forList(Type elemType, boolean ordered) {
        _forList(ordered, FeatureFactory.create(elemType))
    }

    static Closure<Boolean> forList2d(Type elemType, boolean dim1Ordered, boolean dim2Ordered) {
        def elemFeat = FeatureFactory.create(elemType)
        def listFeat = dim2Ordered
                ? ListFeatureFactory.ordered(elemFeat)
                : ListFeatureFactory.unordered(elemFeat)
        _forList(dim1Ordered, listFeat)
    }

    static <T> Closure<Boolean> forArray(Class<T> klass, boolean ordered) {
        { T[] x, T[] y ->
            def clo = forList(klass, ordered)
            clo.call(x.toList(), y.toList())
        }
    }

    static <T> Closure<Boolean> forArray2d(Class<T> klass, boolean dim1Ordered, boolean dim2Ordered) {
        { T[][] x, T[][] y ->
            def clo = forList2d(klass, dim1Ordered, dim2Ordered)
            clo.call(
                    x.collect({it.toList()}).toList(),
                    y.collect({it.toList()}).toList()
            )
        }
    }

    static Closure<Boolean> forIntArray(boolean ordered) {
        { int[] x, int[] y ->
            def clo = forList(Integer, ordered)
            clo(x.toList(), y.toList())
        }
    }

    static Closure<Boolean> forIntArray2d(boolean dim1Ordered, boolean dim2Ordered) {
        { int[][] x, int[][] y ->
            def clo = forList2d(Integer, dim1Ordered, dim2Ordered)
            clo(
                    x.collect({it.toList()}).toList(),
                    y.collect({it.toList()}).toList()
            )
        }
    }

}
