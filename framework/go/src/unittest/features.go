package unittest

import (
	"fmt"
	"hash/fnv"
	"math"
	"reflect"
	"sort"
)

type ObjectFeature interface {
	Hash(obj interface{}) uint64
	IsEqual(a interface{}, b interface{}) bool
}

type GenericFeature struct{}

func (f GenericFeature) Hash(obj interface{}) uint64 {
	hash := fnv.New64a()
	s := fmt.Sprintf("%v", obj)
	_, _ = hash.Write([]byte(s))
	return hash.Sum64()
}

func (f GenericFeature) IsEqual(a, b interface{}) bool {
	return reflect.DeepEqual(a, b)
}

type Float64Feature struct {
	GenericFeature
}

func (f Float64Feature) IsEqual(a, b interface{}) bool {
	fa, fb := a.(float64), b.(float64)
	return math.Abs(fa-fb) < 1e-6
}

type ListFeature struct {
	ElemFeat ObjectFeature
}

func (f ListFeature) Hash(obj interface{}) uint64 {
	var res uint64
	vobj := reflect.ValueOf(obj)
	for i := 0; i < vobj.Len(); i++ {
		h := f.ElemFeat.Hash(vobj.Index(i).Interface())
		res = res*133 + h
	}
	return res
}

func (f ListFeature) IsEqual(a, b interface{}) bool {
	return StrategyFactory.List(f.ElemFeat)(a, b)
}

type UnorderListFeature struct {
	ElemFeat ObjectFeature
}

func (f UnorderListFeature) Hash(obj interface{}) uint64 {
	vobj := reflect.ValueOf(obj)
	hashes := make([]uint64, 0)
	for i := 0; i < vobj.Len(); i++ {
		hashes = append(hashes, f.ElemFeat.Hash(vobj.Index(i).Interface()))
	}
	sort.Slice(hashes, func(i, j int) bool { return hashes[i] < hashes[j] })
	var res uint64
	for _, h := range hashes {
		res = res*133 + h
	}
	return res
}

func (f UnorderListFeature) IsEqual(a, b interface{}) bool {
	return StrategyFactory.UnorderList(f.ElemFeat)(a, b)
}

type FeatureFactoryFunc func() ObjectFeature
type ValidatorFunc func(interface{}, interface{}) bool
type ValidFactoryFunc func() ValidatorFunc

var featureFactoryMap = make(map[reflect.Type]FeatureFactoryFunc)
var validFactoryMap = make(map[reflect.Type]ValidFactoryFunc)

func init() {
	featureFactoryMap[reflect.TypeOf(float64(0))] = func() ObjectFeature { return Float64Feature{} }

	validFactoryMap[reflect.TypeOf([]float64{})] = func() ValidatorFunc {
		return ValidatorFunc(Validators.ForSlice(reflect.TypeOf(float64(0)), true))
	}
	validFactoryMap[reflect.TypeOf([][]float64{})] = func() ValidatorFunc {
		return ValidatorFunc(Validators.ForSlice2d(reflect.TypeOf(float64(0)), true, true))
	}
}

type FeatureFactoryType struct{}

var FeatureFactory FeatureFactoryType

func (f *FeatureFactoryType) Create(objType reflect.Type) ObjectFeature {
	if fact, ok := featureFactoryMap[objType]; ok {
		return fact()
	}
	return GenericFeature{}
}

type ValidatorFactoryType struct{}

var ValidatorFactory ValidatorFactoryType

func (v *ValidatorFactoryType) Create(objType reflect.Type) ValidatorFunc {
	if fact, ok := validFactoryMap[objType]; ok {
		return fact()
	}
	return func(a, b interface{}) bool {
		return FeatureFactory.Create(objType).IsEqual(a, b)
	}
}
