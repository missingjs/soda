package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

var res []string

func permutation(chars []byte, n int) []string {
    res = make([]string, 0)
    buf := make([]byte, n)
    solve(chars, 0, buf, 0)
    return res
}

func solve(chars []byte, i int, buf []byte, j int) {
    if j == len(buf) {
        res = append(res, string(buf))
        return
    }
    for k := i; k < len(chars); k++ {
        chars[i], chars[k] = chars[k], chars[i]
        buf[j] = chars[i]
        solve(chars, i+1, buf, j+1)
        chars[i], chars[k] = chars[k], chars[i]
    }
}

func main() {
    // create tester by function
    work := unittest.CreateWork(permutation)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    work.SetValidator(unittest.Validators.ForSlice(nil, false))
    work.CompareSerial = true
    work.Run()
}
