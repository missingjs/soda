package main

import (
    "fmt"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger

func reorderList(head *ListNode)  {
    slow := head
    fast := head
    for fast.Next != nil && fast.Next.Next != nil {
        slow = slow.Next
        fast = fast.Next.Next
    }

    if slow == fast {
        return
    }

    r := reverse(slow.Next)
    slow.Next = nil
    merge(head, r)
}

func reverse(head *ListNode) *ListNode {
    var q *ListNode
    for head != nil {
        next := head.Next
        head.Next = q
        q = head
        head = next
    }
    return q
}

func merge(L1 *ListNode, L2 *ListNode) {
    t := &ListNode{}
    for L1 != nil && L2 != nil {
        t.Next = L1
        t = L1
        L1 = L1.Next
        t.Next = L2
        t = L2
        L2 = L2.Next
    }
    if L1 != nil {
        t.Next = L1
    } else {
        t.Next = L2
    }
}

func main() {
    // create tester by function
    work := unittest.CreateWork(reorderList)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    fmt.Print(work.Exec(unittest.Utils.FromStdin()))
}

var _x Dummy
