package soda.kotlin.unittest

import soda.kotlin.unittest.validate.CommonFeatureFactory
import soda.kotlin.unittest.validate.FeatureFactory
import soda.kotlin.unittest.validate.ObjectFeature
import kotlin.reflect.typeOf

object Validators {

    inline fun <reified T> forList(ordered: Boolean): (List<T>, List<T>) -> Boolean {
        return forList(ordered, FeatureFactory.create(typeOf<T>()))
    }

    inline fun <reified T> forList2d(
        dim1Ordered: Boolean,
        dim2Ordered: Boolean
    ): (List<List<T>>, List<List<T>>) -> Boolean {
        val elemFeat = FeatureFactory.create<T>(typeOf<T>())
        val listFeat =
            if (dim2Ordered) CommonFeatureFactory.forList(elemFeat) else CommonFeatureFactory.forUnorderedList(elemFeat)
        return forList(dim1Ordered, listFeat)
    }

    inline fun <reified T> forArray(ordered: Boolean): (Array<T>, Array<T>) -> Boolean {
        return { a: Array<T>, b: Array<T> -> forList<T>(ordered)(a.toList(), b.toList()) }
    }

    inline fun <reified T> forArray2d(
        dim1Ordered: Boolean,
        dim2Ordered: Boolean
    ): (Array<Array<T>>, Array<Array<T>>) -> Boolean {
        return { a: Array<Array<T>>, b: Array<Array<T>> ->
            forList2d<T>(dim1Ordered, dim2Ordered)(a.map { it.toList() }.toList(), b.map { it.toList() }.toList())
        }
    }

    fun <T> forList(ordered: Boolean, elemFeat: ObjectFeature<T>): (List<T>, List<T>) -> Boolean {
        return if (ordered) {
            CommonFeatureFactory.forList(elemFeat)::isEqual
        } else {
            CommonFeatureFactory.forUnorderedList(elemFeat)::isEqual
        }
    }

}