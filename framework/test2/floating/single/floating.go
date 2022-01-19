package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func divide(a, b float64) float64 {
    return a / b
}

func main() {
    // create tester by function
    work := unittest.CreateWork(divide)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    // work.CompareSerial = true
    work.Run()
}
