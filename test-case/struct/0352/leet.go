package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"

    "sort"
)

var logger = util.Logger
var _used ListNode

type SummaryRanges struct {
    parent []int
    ancestorSet map[int]bool
}


func Constructor() SummaryRanges {
    return SummaryRanges { make([]int, 10003), make(map[int]bool) }
}


func (this *SummaryRanges) AddNum(val int)  {
    val++
    if this.parent[val] != 0 {
        return
    }

    this.parent[val] = -1
    this.ancestorSet[val] = true

    left, right := val - 1, val + 1
    if left > 0 && this.parent[left] != 0 {
        this.merge(left, val)
    }
    if this.parent[right] != 0 {
        this.merge(val, right)
    }
}


func (this *SummaryRanges) GetIntervals() [][]int {
    ans := make([]int, 0)
    for k := range this.ancestorSet {
        ans = append(ans, k)
    }
    sort.Ints(ans)
    res := make([][]int, len(ans))
    for i := range res {
        start := ans[i]
        end := start - this.parent[start] - 1
        res[i] = append(res[i], start-1, end-1)
    }
    return res
}


func (this *SummaryRanges) merge(x, y int) {
    ax := this.getAncestor(x)
    ay := this.getAncestor(y)
    if ax < ay {
        this.mergeAncestor(ax, ay)
    } else {
        this.mergeAncestor(ay, ax)
    }
}


func (this *SummaryRanges) mergeAncestor(ax, ay int) {
    this.parent[ax] += this.parent[ay]
    this.parent[ay] = ax
    delete(this.ancestorSet, ay)
}


func (this *SummaryRanges) getAncestor(x int) int {
    if this.parent[x] < 0 {
        return x
    }
    p := this.getAncestor(this.parent[x])
    this.parent[x] = p
    return p
}

func main() {
    // create tester by function
    // work := unittest.CreateWork(FUNCTION)
    // OR create by struct factory
    work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
