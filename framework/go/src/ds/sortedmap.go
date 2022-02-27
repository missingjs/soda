package ds

type SortedMap struct {
    skipList *SkipList
}

func NewSortedMap(cmpFunc interface{}) *SortedMap {
    return &SortedMap{
        skipList: NewSkipList(10, 0.5, cmpFunc),
    }
}

func (sm *SortedMap) Has(key interface{}) bool {
    return sm.skipList.Query(key) != nil
}

func (sm *SortedMap) GetOrDefault(key interface{}, defaultVal interface{}) interface{} {
    node := sm.skipList.Query(key)
    if node != nil {
        return node.Value
    } else {
        return defaultVal
    }
}

func (sm *SortedMap) Get(key interface{}) interface{} {
    return sm.GetOrDefault(key, nil)
}

func (sm *SortedMap) Size() int {
    return sm.skipList.Size()
}

func (sm *SortedMap) Set(key interface{}, value interface{}) {
    sm.skipList.Update(key, value)
}

func (sm *SortedMap) Del(key interface{}) {
    sm.skipList.Remove(key)
}

func (sm *SortedMap) LowerBoundKey(key interface{}) interface{} {
    node := sm.skipList.LowerBound(key)
    if node != nil {
        return node.Key
    }
    return nil
}

func (sm *SortedMap) Items() [][]interface{} {
    var items [][]interface{}
    for _, node := range sm.skipList.Items() {
        items = append(items, []interface{}{node.Key, node.Value})
    }
    return items
}
