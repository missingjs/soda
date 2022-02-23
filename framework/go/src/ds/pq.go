package ds

import (
    "container/heap"
    "reflect"
)

type _heap struct {
    heap     []interface{}
    lessFunc reflect.Value
}

func (pq *_heap) Len() int {
    return len(pq.heap)
}

func (pq *_heap) Less(i, j int) bool {
    args := []reflect.Value{
        reflect.ValueOf(pq.heap[i]),
        reflect.ValueOf(pq.heap[j]),
    }
    return pq.lessFunc.Call(args)[0].Interface().(bool)
}

func (pq *_heap) Swap(i, j int) {
    pq.heap[i], pq.heap[j] = pq.heap[j], pq.heap[i]
}

func (pq *_heap) Push(x interface{}) {
    pq.heap = append(pq.heap, x)
}

func (pq *_heap) Pop() interface{} {
    old := pq.heap
    n := len(old)
    item := old[n-1]
    old[n-1] = nil
    pq.heap = old[:n-1]
    return item
}

type PriorityQueue struct {
    hp *_heap
}

func NewPriorityQueue(lessFunc interface{}) *PriorityQueue {
    return &PriorityQueue{
        hp: &_heap{
            lessFunc: reflect.ValueOf(lessFunc),
        },
    }
}

func (pq *PriorityQueue) Size() int {
    return pq.hp.Len()
}

func (pq *PriorityQueue) Push(item interface{}) {
    heap.Push(pq.hp, item)
}

func (pq *PriorityQueue) Pop() interface{} {
    return heap.Pop(pq.hp)
}

func (pq *PriorityQueue) Top() interface{} {
    return pq.hp.heap[0]
}

func (pq *PriorityQueue) Empty() bool {
    return pq.Size() == 0
}
