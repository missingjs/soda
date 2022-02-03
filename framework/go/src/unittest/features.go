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
    va, vb := reflect.ValueOf(a), reflect.ValueOf(b)
    if va.Len() != vb.Len() {
        return false
    }
    for i := 0; i < va.Len(); i++ {
        obja := va.Index(i).Interface()
        objb := vb.Index(i).Interface()
        if !f.ElemFeat.IsEqual(obja, objb) {
            return false
        }
    }
    return true
}

type UnorderedListFeature struct {
    ElemFeat ObjectFeature
}

func (f UnorderedListFeature) Hash(obj interface{}) uint64 {
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

func (f UnorderedListFeature) IsEqual(a, b interface{}) bool {
    va, vb := reflect.ValueOf(a), reflect.ValueOf(b)
    if va.Len() != vb.Len() {
        return false
    }
    xmap := NewXMap(f.ElemFeat)
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

type FeatureFactoryFunc func() ObjectFeature
type ValidatorFunc func(interface{}, interface{}) bool
type ValidFactoryFunc func() ValidatorFunc

var featureFactoryMap = make(map[reflect.Type]FeatureFactoryFunc)

func init() {
    featureFactoryMap[reflect.TypeOf(float64(0))] = func() ObjectFeature { return Float64Feature{} }
    featureFactoryMap[reflect.TypeOf([]float64{})] = func() ObjectFeature {
        return ListFeature{ElemFeat: FeatureFactory.Create(reflect.TypeOf(float64(0)))}
    }
    featureFactoryMap[reflect.TypeOf([][]float64{})] = func() ObjectFeature {
        return ListFeature{ElemFeat: FeatureFactory.Create(reflect.TypeOf([]float64{}))}
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
