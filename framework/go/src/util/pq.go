package util

type PriorityQueue struct {
    array []interface{}
    compare func(i, j interface{})bool
}

func NewPriorityQueue(cmp func(i, j interface{})bool) *PriorityQueue {
    return &PriorityQueue {array: make([]interface{}, 1), compare: cmp}
}

func (h *PriorityQueue) Push(x interface{}) {
    h.array = append(h.array, x)
    h.adjustAsc()
}

func (h *PriorityQueue) Size() int {
    return len(h.array) - 1
}

func (h *PriorityQueue) Empty() bool {
    return len(h.array) == 1
}

func (h *PriorityQueue) Top() interface{} {
    return h.array[1]
}

func (h *PriorityQueue) Pop() interface{} {
    top := h.array[1]
    sz := h.Size()
    h.array[1] = h.array[sz]
    h.array = h.array[:sz]
    h.adjustDesc()
    return top
}

func (h *PriorityQueue) doCompare(i, j int) bool {
    return h.compare(h.array[i], h.array[j])
}

func (h *PriorityQueue) adjustAsc() {
    i := h.Size()
    for i > 1 {
        p := i / 2
        if h.doCompare(i, p) {
            h.array[i], h.array[p] = h.array[p], h.array[i]
            i = p
        } else {
            break
        }
    }
}

func (h *PriorityQueue) adjustDesc() {
    end := h.Size() / 2
    i := 1
    for i <= end {
        L, R := i*2, i*2+1
        T := 0
        if R > h.Size() || h.doCompare(L, R) {
            T = L
        } else {
            T = R
        }
        if h.doCompare(T, i) {
            h.array[T], h.array[i] = h.array[i], h.array[T]
            i = T
        } else {
            break
        }
    }
}
