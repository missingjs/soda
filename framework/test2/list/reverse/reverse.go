package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

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
    work := unittest.CreateWork(reverse)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
