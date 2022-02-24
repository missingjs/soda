package main

import (
    "fmt"
    . "missingjs.com/soda/ds"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger

func mergeKLists(lists []*ListNode) *ListNode {
    pq := NewMinPriorityQueue()
    for _, node := range lists {
        if node != nil {
            pq.Push(node, node.Val)
        }
    }

    var head ListNode
    tail := &head
    for !pq.Empty() {
        t := pq.Pop().(*ListNode)
        L := t.Next
        if L != nil {
            pq.Push(L, L.Val)
        }
        tail.Next = t
        tail = t
    }
    return head.Next
}

func main() {
    // create tester by function
    work := unittest.CreateWork(mergeKLists)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    fmt.Print(work.Exec(unittest.Utils.FromStdin()))
}

var _x Dummy
