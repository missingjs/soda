package ds

import (
    "container/heap"
)

type pqItem struct {
    payload  interface{}
    priority int
}

type intPQ []*pqItem

func (p intPQ) Len() int {
    return len(p)
}

func (p intPQ) Less(i, j int) bool {
    return p[i].priority < p[j].priority
}

func (p intPQ) Swap(i, j int) {
    p[i], p[j] = p[j], p[i]
}

func (p *intPQ) Push(x interface{}) {
    *p = append(*p, x.(*pqItem))
}

func (p *intPQ) Pop() interface{} {
    old := *p
    n := len(old)
    item := old[n-1]
    old[n-1] = nil
    *p = old[:n-1]
    return item
}

type MinPriorityQueue struct {
    pq intPQ
}

func NewMinPriorityQueue() *MinPriorityQueue {
    return &MinPriorityQueue {}
}

func (ip *MinPriorityQueue) Size() int {
    return ip.pq.Len()
}

func (ip *MinPriorityQueue) Push(payload interface{}, priority int) {
    heap.Push(&ip.pq, &pqItem{ payload, priority })
}

func (ip *MinPriorityQueue) Pop() interface{} {
    payload, _ := ip.PopItem()
    return payload
}

func (ip *MinPriorityQueue) PopItem() (interface{}, int) {
    item := heap.Pop(&ip.pq).(*pqItem)
    return item.payload, item.priority
}

func (ip *MinPriorityQueue) TopItem() (interface{}, int) {
    return ip.pq[0].payload, ip.pq[0].priority
}

func (ip *MinPriorityQueue) Top() interface{} {
    payload, _ := ip.TopItem()
    return payload
}

func (ip *MinPriorityQueue) Empty() bool {
    return ip.Size() == 0
}

