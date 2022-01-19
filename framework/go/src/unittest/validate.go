package unittest

import (
	"reflect"
)

type SliceValidateFunc func(a, b interface{}) bool

type StrategyFactoryType struct{}

var StrategyFactory StrategyFactoryType

func (sf *StrategyFactoryType) UnorderList(feature ObjectFeature) SliceValidateFunc {
	return func(a, b interface{}) bool {
		va, vb := reflect.ValueOf(a), reflect.ValueOf(b)
		if va.Len() != vb.Len() {
			return false
		}
		xmap := NewXMap(feature)
		for i := 0; i < va.Len(); i++ {
			key := va.Index(i).Interface()
			c := xmap.GetOrDefault(key, 0).(int)
			xmap.Put(key, c+1)
		}

		for i := 0; i < vb.Len(); i++ {
			key := vb.Index(i).Interface()
			if !xmap.Contains(key) {
				return false
			}
			c := xmap.Get(key).(int)
			if c > 1 {
				xmap.Put(key, c-1)
			} else {
				xmap.Remove(key)
			}
		}
		return true
	}
}

func (sf *StrategyFactoryType) List(feature ObjectFeature) SliceValidateFunc {
	return func(a, b interface{}) bool {
		va, vb := reflect.ValueOf(a), reflect.ValueOf(b)
		if va.Len() != vb.Len() {
			return false
		}
		for i := 0; i < va.Len(); i++ {
			obja := va.Index(i).Interface()
			objb := vb.Index(i).Interface()
			if !feature.IsEqual(obja, objb) {
				return false
			}
		}
		return true
	}
}

type ValidatorsType struct{}

var Validators ValidatorsType

func (v *ValidatorsType) ForSlice(objType reflect.Type, ordered bool) SliceValidateFunc {
	feat := createFeature(objType)
	if ordered {
		return StrategyFactory.List(feat)
	}
	return StrategyFactory.UnorderList(feat)
}

func (v *ValidatorsType) ForSlice2d(objType reflect.Type, dim1Ordered, dim2Ordered bool) SliceValidateFunc {
	elemFeat := createFeature(objType)
	var f ObjectFeature
	if dim2Ordered {
		f = ListFeature{ElemFeat: elemFeat}
	} else {
		f = UnorderListFeature{ElemFeat: elemFeat}
	}
	if dim1Ordered {
		return StrategyFactory.List(f)
	}
	return StrategyFactory.UnorderList(f)
}

func createFeature(objType reflect.Type) ObjectFeature {
	return FeatureFactory.Create(objType)
}
