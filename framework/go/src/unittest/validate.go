package unittest

import (
	"reflect"
)

//type ValidatorFunc func(interface{}, interface{}) bool
//
//type SliceEqual struct {
//	ElementEqualFunc ValidatorFunc
//}
//
//func (s *SliceEqual) Validate(s1 interface{}, s2 interface{}) bool {
//	v1 := reflect.ValueOf(s1)
//	v2 := reflect.ValueOf(s2)
//
//	if v1.Len() != v2.Len() {
//		return false
//	}
//
//	N := v1.Len()
//	visited := make([]bool, N)
//	for i := 0; i < N; i++ {
//		e1 := v1.Index(i)
//		exist := false
//		for j := 0; j < N; j++ {
//			e2 := v2.Index(j)
//			if !visited[j] && s.ElementEqualFunc(e1.Interface(), e2.Interface()) {
//				visited[j] = true
//				exist = true
//				break
//			}
//		}
//		if !exist {
//			return false
//		}
//	}
//	return true
//}

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
	if objType != nil && objType.Name() == "float64" {
		return Float64Feature{}
	}
	return GenericFeature{}
}

//func (v *ValidatorsType) Slice1d() ValidatorFunc {
//	se := &SliceEqual{
//		ElementEqualFunc: func(i interface{}, j interface{}) bool {
//			//fmt.Fprintf(os.Stderr, "%v, %v\n", i, j)
//			return reflect.DeepEqual(i, j)
//		},
//	}
//	return se.Validate
//}
//
//func (v *ValidatorsType) Slice2d() ValidatorFunc {
//	se := &SliceEqual{
//		ElementEqualFunc: v.Slice1d(),
//	}
//	return se.Validate
//}
