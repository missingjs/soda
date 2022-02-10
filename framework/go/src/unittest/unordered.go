package unittest

import (
    "reflect"
    "sort"
)

type XEntry struct {
    Key   interface{}
    Value interface{}
}

func newXEntry(key, val interface{}) *XEntry {
    return &XEntry{
        Key:   key,
        Value: val,
    }
}

type XMap struct {
    Feat ObjectFeature
    Dict map[uint64][]*XEntry
    size int
}

func NewXMap(feature ObjectFeature) *XMap {
    return &XMap{
        Feat: feature,
        Dict: make(map[uint64][]*XEntry),
        size: 0,
    }
}

func (x *XMap) Contains(key interface{}) bool {
    return x._entry(key) != nil
}

func (x *XMap) Get(key interface{}) interface{} {
    return x.GetOrDefault(key, nil)
}

func (x *XMap) GetOrDefault(key interface{}, defaultValue interface{}) interface{} {
    e := x._entry(key)
    if e != nil {
        return e.Value
    }
    return defaultValue
}

func (x *XMap) Put(key interface{}, value interface{}) {
    e := x._entry(key)
    if e == nil {
        e = newXEntry(key, value)
        h := x._hash(key)
        x.Dict[h] = append(x.Dict[h], e)
        x.size++
    }
    e.Value = value
}

func (x *XMap) Remove(key interface{}) {
    h := x._hash(key)
    if _, ok := x.Dict[h]; !ok {
        return
    }
    index := -1
    for i := 0; i < len(x.Dict[h]); i++ {
        if x.Feat.IsEqual(key, x.Dict[h][i].Key) {
            index = i
            break
        }
    }
    if index >= 0 {
        x.Dict[h] = remove(x.Dict[h], index)
        x.size--
    }
}

func remove(entries []*XEntry, i int) []*XEntry {
    if i == 0 {
        return entries[1:]
    }
    if i == len(entries)-1 {
        return entries[:len(entries)-1]
    }
    res := entries[:i]
    i++
    for i < len(entries) {
        res = append(res, entries[i])
        i++
    }
    return res
}

func (x *XMap) Size() int {
    return x.size
}

func (x *XMap) _hash(key interface{}) uint64 {
    return x.Feat.Hash(key)
}

func (x *XMap) _entry(key interface{}) *XEntry {
    h := x._hash(key)
    if _, ok := x.Dict[h]; !ok {
        return nil
    }
    for _, entry := range x.Dict[h] {
        if x.Feat.IsEqual(key, entry.Key) {
            return entry
        }
    }
    return nil
}

func (x *XMap) GetEntries() []*XEntry {
    var res []*XEntry
    for _, v := range x.Dict {
        res = append(res, v...)
    }
    return res
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
