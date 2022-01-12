package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func intersection(nums1 []int, nums2 []int) []int {
    if len(nums1) > len(nums2) {
        return intersection(nums2, nums1)
    }
    mset := make(map[int]bool)
    res := make(map[int]bool)
    for _, n := range nums1 {
        mset[n] = true
    }
    for _, b := range nums2 {
        if mset[b] {
            res[b] = true
        }
    }
    r := make([]int, 0)
    for k := range res {
        r = append(r, k)
    }
    return r
}

func main() {
    // create tester by function
    work := unittest.CreateWork(intersection)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    work.SetValidator(unittest.Validators.ForSlice(nil, false))
    work.CompareSerial = true
    work.Run()
}
