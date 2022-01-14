package leetcode

import (
	"encoding/json"
	"fmt"
	"strconv"
)

// NestedInteger This is the interface that allows for creating nested lists.
// You should not implement it, or speculate about its implementation
type NestedInteger struct {
	isAtomic bool
	value    int
	list     []*NestedInteger
}

func NewNestedIntegerWithInt(val int) *NestedInteger {
	return &NestedInteger{
		isAtomic: true,
		value:    val,
	}
}

func NewNestedInteger() *NestedInteger {
	return &NestedInteger{}
}

// IsInteger Return true if this NestedInteger holds a single integer, rather than a nested list.
func (n *NestedInteger) IsInteger() bool {
	return n.isAtomic
}

// GetInteger Return the single integer that this NestedInteger holds, if it holds a single integer
// The result is undefined if this NestedInteger holds a nested list
// So before calling this method, you should have a check
func (n *NestedInteger) GetInteger() int {
	return n.value
}

// SetInteger Set this NestedInteger to hold a single integer.
func (n *NestedInteger) SetInteger(value int) {
	n.value = value
}

// Add Set this NestedInteger to hold a nested list and adds a nested integer to it.
func (n *NestedInteger) Add(elem *NestedInteger) {
	n.list = append(n.list, elem)
}

// GetList Return the nested list that this NestedInteger holds, if it holds a nested list
// The list length is zero if this NestedInteger holds a single integer
// You can access NestedInteger's list element directly if you want to modify it
func (n *NestedInteger) GetList() []*NestedInteger {
	return n.list
}

func ParseNestedIntegers(raw []json.RawMessage) []*NestedInteger {
	res := make([]*NestedInteger, 0)
	for _, r := range raw {
		res = append(res, UnmarshalNestedInteger(r))
	}
	return res
}

func UnmarshalNestedInteger(raw json.RawMessage) *NestedInteger {
	if raw[0] != '[' {
		val, err := strconv.Atoi(string(raw))
		if err != nil {
			panic(fmt.Sprintf("failed to parse int from string: %s", string(raw)))
		}
		return NewNestedIntegerWithInt(val)
	}

	var rawList []json.RawMessage
	if err := json.Unmarshal(raw, &rawList); err != nil {
		panic(fmt.Sprintf("failed to unmarshal json: %v", err))
	}
	ni := NewNestedInteger()
	for _, r := range rawList {
		ni.Add(UnmarshalNestedInteger(r))
	}
	return ni
}

func SerializeNestedIntegers(ns []*NestedInteger) []interface{} {
	res := make([]interface{}, 0)
	for _, ni := range ns {
		res = append(res, MarshalNestedInteger(ni))
	}
	return res
}

func MarshalNestedInteger(ni *NestedInteger) interface{} {
	if ni.IsInteger() {
		return ni.GetInteger()
	}
	list := make([]interface{}, 0)
	for _, i := range ni.GetList() {
		list = append(list, MarshalNestedInteger(i))
	}
	return list
}
