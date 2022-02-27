package ds

import (
    "math/rand"
    "reflect"
)

type SkipNode struct {
    Level int
    Links []*SkipNode
    Key   interface{}
    Value interface{}
}

func NewSkipNode(level int, key interface{}, value interface{}) *SkipNode {
    return &SkipNode{
        Level: level,
        Links: make([]*SkipNode, level),
        Key:   key,
        Value: value,
    }
}

func CreateSkipNodeByProb(maxLevel int, p float64, key interface{}, value interface{}) *SkipNode {
    level := 1
    for rand.Float64() < p && level < maxLevel {
        level++
    }
    return NewSkipNode(level, key, value)
}

type SkipList struct {
    maxLevel int
    prob     float64
    head     *SkipNode
    footBuf  []*SkipNode
    cmpFunc  reflect.Value
    size     int
}

func NewSkipList(maxLevel int, prob float64, cmpFunc interface{}) *SkipList {
    return &SkipList{
        maxLevel: maxLevel,
        prob:     prob,
        cmpFunc:  reflect.ValueOf(cmpFunc),
        head:     NewSkipNode(maxLevel, nil, nil),
        footBuf:  make([]*SkipNode, maxLevel),
        size:     0,
    }
}

func (s *SkipList) Size() int {
    return s.size
}

func (s *SkipList) Query(key interface{}) *SkipNode {
    return s.find(key, s.footBuf)
}

func (s *SkipList) QueryValue(key interface{}) interface{} {
    node := s.find(key, s.footBuf)
    if node != nil && s.cmp(node.Key, key) == 0 {
        return node.Value
    }
    return nil
}

func (s *SkipList) LowerBound(key interface{}) *SkipNode {
    return s.find(key, s.footBuf)
}

func (s *SkipList) Insert(key interface{}, value interface{}) {
    s.Update(key, value)
}

func (s *SkipList) Update(key interface{}, value interface{}) {
    node := s.find(key, s.footBuf)
    if node == nil || s.cmp(node.Key, key) != 0 {
        node = s.doInsert(key, s.footBuf)
    }
    node.Value = value
}

func (s *SkipList) Remove(key interface{}) {
    node := s.find(key, s.footBuf)
    if node != nil && s.cmp(node.Key, key) == 0 {
        for lv := 0; lv < node.Level; lv++ {
            s.footBuf[lv].Links[lv] = node.Links[lv]
        }
        s.size--
    }
}

func (s *SkipList) Items() []*SkipNode {
    var nodes []*SkipNode
    p := s.head.Links[0]
    for p != nil {
        nodes = append(nodes, p)
        p = p.Links[0]
    }
    return nodes
}

func (s *SkipList) doInsert(key interface{}, footprint []*SkipNode) *SkipNode {
    node := CreateSkipNodeByProb(s.maxLevel, s.prob, key, nil)
    for lv := 0; lv < node.Level; lv++ {
        next := footprint[lv].Links[lv]
        footprint[lv].Links[lv] = node
        node.Links[lv] = next
    }
    s.size++
    return node
}

func (s *SkipList) find(key interface{}, footprint []*SkipNode) *SkipNode {
    node := s.head
    lv := s.maxLevel - 1
    for lv >= 0 {
        if node.Links[lv] != nil && s.cmp(node.Links[lv].Key, key) < 0 {
            node = node.Links[lv]
        } else {
            footprint[lv] = node
            lv--
        }
    }
    return node.Links[0]
}

func (s *SkipList) cmp(a, b interface{}) int {
    args := []reflect.Value{reflect.ValueOf(a), reflect.ValueOf(b)}
    return s.cmpFunc.Call(args)[0].Interface().(int)
}
