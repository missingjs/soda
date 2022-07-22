package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

var memo []int

func integerBreak(n int) int {
    memo = make([]int, 59)
    return solve(n)
}

func solve(n int) int {
    if n == 1 {
        return 1
    }
    if memo[n] > 0 {
        return memo[n]
    }
    res := 0
    for i := 1; i < n; i++ {
        res = max(res, i * (n-i))
        res = max(res, i * solve(n-i))
    }
    memo[n] = res
    return res
}

func max(a, b int) int {
    if a > b {
        return a
    }
    return b
}

func main() {
    // create tester by function
    work := unittest.CreateWork(integerBreak)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
