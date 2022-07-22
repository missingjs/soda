package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

type Info struct {
    sum      int
    product  int
    maxDepth int
}

func depthSumInverse(nestedList []*NestedInteger) int {
    info := getInfo(nestedList, 1)
    return (info.maxDepth + 1) * info.sum - info.product
}

func getInfo(nestedList []*NestedInteger, depth int) Info {
    sum, product, maxDepth := 0, 0, 0
    for _, ni := range nestedList {
        if ni.IsInteger() {
            val := ni.GetInteger()
            sum += val
            product += val * depth
            maxDepth = max(maxDepth, depth)
        } else {
            res := getInfo(ni.GetList(), depth+1)
            sum += res.sum
            product += res.product
            maxDepth = max(maxDepth, res.maxDepth)
        }
    }
    return Info{sum, product, maxDepth}
}

func max(a, b int) int {
    if a > b {
        return a
    }
    return b
}

func main() {
    // create tester by function
    work := unittest.CreateWork(depthSumInverse)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
