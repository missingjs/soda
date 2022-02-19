package main

import (
    "fmt"
    "container/heap"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
type PriorityQueue []*ListNode

func (pq PriorityQueue) Len() int { return len(pq) }

func (pq PriorityQueue) Less(i, j int) bool {
	return pq[i].Val < pq[j].Val
}

func (pq PriorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
}

func (pq *PriorityQueue) Push(x interface{}) {
	*pq = append(*pq, x.(*ListNode))
}

func (pq *PriorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	old[n-1] = nil  // avoid memory leak
	*pq = old[0 : n-1]
	return item
}

func mergeKLists(lists []*ListNode) *ListNode {
    pq := make(PriorityQueue, 0)
    for _, node := range lists {
        if node != nil {
            heap.Push(&pq, node)
        }
    }

    var head ListNode
    tail := &head
    for len(pq) > 0 {
        t := heap.Pop(&pq).(*ListNode)
        L := t.Next
        if L != nil {
            heap.Push(&pq, L)
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
