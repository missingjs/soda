package main

import (
    "math/rand"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func groupByLength(strs []string) [][]string {
    rand.Shuffle(len(strs), func(i, j int){ strs[i], strs[j] = strs[j], strs[i]})

    var group = make(map[int][]string)
    for _, s := range strs {
        group[len(s)] = append(group[len(s)], s)
    }

    res := make([][]string, 0)
    for _, v := range group {
        res = append(res, v)
    }
    rand.Shuffle(len(res), func(i, j int){ res[i], res[j] = res[j], res[i] })
    return res
}

func main() {
    // create tester by function
    work := unittest.CreateWork(groupByLength)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    work.SetValidator(unittest.Validators.ForSlice2d(nil, false, false))
    work.CompareSerial = true
    work.Run()
}
