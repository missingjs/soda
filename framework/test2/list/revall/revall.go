package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func reverseAll(lists []*ListNode) []*ListNode {
    for i := range lists {
        lists[i] = reverse(lists[i])
    }
    i, j := 0, len(lists) - 1
    for i < j {
        temp := lists[i]
        lists[i] = lists[j]
        lists[j] = temp
        i++
        j--
    }
    return lists
}

func reverse(head *ListNode) *ListNode {
    var h *ListNode
    for head != nil {
        next := head.Next
        head.Next = h
        h = head
        head = next
    }
    return h
}

func main() {
    // create tester by function
    work := unittest.CreateWork(reverseAll)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
