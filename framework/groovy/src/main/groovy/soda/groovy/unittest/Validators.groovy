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

}
