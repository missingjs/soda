package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func flatNested(niList []*NestedInteger) []int {
    res := make([]int, 0)
    iter := Constructor(niList)
    for iter.HasNext() {
        res = append(res, iter.Next())
    }
    return res
}

type Node struct {
    List  []*NestedInteger
    Index int
}

func (n *Node) IsEnd() bool {
    return n.Index >= len(n.List)
}

func (n *Node) Value() int {
    return n.Current().GetInteger()
}

func (n *Node) Current() *NestedInteger {
    return n.List[n.Index]
}

type Stack []*Node

func (s *Stack) Push(node *Node) {
    (*s) = append(*s, node)
}

func (s *Stack) Pop() *Node {
    top := s.Top()
    (*s) = (*s)[:s.Size()-1]
    return top
}

func (s *Stack) Size() int {
    return len(*s)
}

func (s *Stack) Top() *Node {
    return (*s)[s.Size()-1]
}

type NestedIterator struct {
    Stack Stack
}

func Constructor(nestedList []*NestedInteger) *NestedIterator {
    iter := &NestedIterator{}
    iter.Stack.Push(&Node{nestedList, 0})
    iter.locate()
    return iter
}

func (this *NestedIterator) locate() {
    for this.Stack.Size() > 0 {
        if this.Stack.Top().IsEnd() {
            this.Stack.Pop()
            if this.Stack.Size() > 0 {
                this.Stack.Top().Index++
            }
        } else if this.Stack.Top().Current().IsInteger() {
            break
        } else {
            this.Stack.Push(&Node{this.Stack.Top().Current().GetList(),0})
        }
    }
}

func (this *NestedIterator) Next() int {
    value := this.Stack.Top().Value()
    this.Stack.Top().Index++
    this.locate()
    return value
}

func (this *NestedIterator) HasNext() bool {
    return this.Stack.Size() > 0 && !this.Stack.Top().IsEnd()
}

func main() {
    // create tester by function
    work := unittest.CreateWork(flatNested)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
