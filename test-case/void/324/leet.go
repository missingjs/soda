package main

import (
    "fmt"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger

type VirIndex struct {
    nums []int
}

func (v *VirIndex) Get(index int) int {
    return v.nums[v.mapIndex(index)]
}

func (v *VirIndex) Set(index int, value int) {
    v.nums[v.mapIndex(index)] = value
}

func (v *VirIndex) mapIndex(i int) int {
    N := len(v.nums)
    if (N&1) == 1 || i > ((N-1) >> 1) {
        return (((N-i) << 1) - 1) % N
    } else {
        return N - 2 - (i << 1)
    }
}

func wiggleSort(nums []int)  {
    vi := &VirIndex{ nums }
    quickSelect(vi, 0, len(nums)-1, (len(nums)-1) / 2)
}

func quickSelect(vi *VirIndex, start, end, k int) {
    for start < end {
        p0, p1 := partition(vi, start, end)
        if k >= p0 && k <= p1 {
            return
        }
        if k > p1 {
            start = p1 + 1
        } else {
            end = p0 - 1
        }
    }
}

func partition(vi *VirIndex, start, end int) (int, int) {
    mid := (start + end) / 2
    pivot := getMedian(vi.Get(start), vi.Get(mid), vi.Get(end))
    p := start
    z := end + 1
    q := start
    for q < z {
        if vi.Get(q) < pivot {
            swap(vi, p, q)
            p++
            q++
        } else if vi.Get(q) == pivot {
            q++
        } else {
            z--
            swap(vi, z, q)
        }
    }
    return p, z-1
}

func getMedian(a, b, c int) int {
    if a >= b {
        if b >= c {
            return b
        }
        return min(a, c)
    } else {
        if a >= c {
            return a
        }
        return min(b, c)
    }
}

func swap(vi *VirIndex, i, j int) {
    temp := vi.Get(i)
    vi.Set(i, vi.Get(j))
    vi.Set(j, temp)
}

func min(a, b int) int {
    if a < b {
        return a
    }
    return b
}

func main() {
    // create tester by function
    work := unittest.CreateWork(wiggleSort)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    validator := func (_, nums []int) bool {
        for i := 1; i < len(nums); i++ {
            if i % 2 != 0 && nums[i] <= nums[i-1] || i % 2 == 0 && nums[i] >= nums[i-1] {
                return false
            }
        }
        return true
    }
    work.SetValidator(validator)
    work.CompareSerial = true
    fmt.Print(work.Exec(unittest.Utils.FromStdin()))
}

var _x Dummy
