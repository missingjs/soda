package ds

import "container/heap"

type genericHeap[T any] struct {
    Heap     []T
    LessFunc func(T, T)bool
}

func (h *genericHeap[T]) Len() int {
    return len(h.Heap)
}

func (h *genericHeap[T]) Less(i, j int) bool {
    return h.LessFunc(h.Heap[i], h.Heap[j])
}

func (h *genericHeap[T]) Swap(i, j int) {
    h.Heap[i], h.Heap[j] = h.Heap[j], h.Heap[i]
}

func (h *genericHeap[T]) Push(data interface{}) {
    h.Heap = append(h.Heap, data.(T))
}

func (h *genericHeap[T]) Pop() interface{} {
    old := h.Heap
    n := len(old)
    item := old[n-1]

    var noop T
    old[n-1] = noop
    h.Heap = old[:n-1]
    return item
}

type GenericPriorityQueue[T any] struct {
    heap *genericHeap[T]
}

func NewGenericPriorityQueue[T any](less func(T, T) bool) *GenericPriorityQueue[T] {
    return &GenericPriorityQueue[T] {
        heap: &genericHeap[T] {
            Heap: make([]T, 0),
            LessFunc: less,
        },
    }
}

func (q *GenericPriorityQueue[T]) Size() int {
    return q.heap.Len()
}

func (q *GenericPriorityQueue[T]) Push(data T) {
    heap.Push(q.heap, data)
}

func (q *GenericPriorityQueue[T]) Pop() T {
    return heap.Pop(q.heap).(T)
}

func (q *GenericPriorityQueue[T]) Top() T {
    return q.heap.Heap[0]
}

func (q *GenericPriorityQueue[T]) Empty() bool {
    return q.Size() == 0
}

