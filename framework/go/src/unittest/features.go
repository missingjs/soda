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
