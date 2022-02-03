package unittest

import (
    "reflect"
)

type SliceValidateFunc func(a, b interface{}) bool

type ValidatorsType struct{}

var Validators ValidatorsType

func (v *ValidatorsType) ForSlice(objType reflect.Type, ordered bool) SliceValidateFunc {
    return v.forSlice(ordered, FeatureFactory.Create(objType))
}

func (v *ValidatorsType) ForSlice2d(objType reflect.Type, dim1Ordered, dim2Ordered bool) SliceValidateFunc {
    elemFeat := FeatureFactory.Create(objType)
    var f ObjectFeature
    if dim2Ordered {
        f = ListFeature{ElemFeat: elemFeat}
    } else {
        f = UnorderedListFeature{ElemFeat: elemFeat}
    }
    return v.forSlice(dim1Ordered, f)
}

func (v *ValidatorsType) forSlice(ordered bool, elemFeat ObjectFeature) SliceValidateFunc {
    if ordered {
        return ListFeature{ElemFeat: elemFeat}.IsEqual
    }
    return UnorderedListFeature{ElemFeat: elemFeat}.IsEqual
}
