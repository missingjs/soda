package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func doubleList(chars []byte) []byte {
    res := make([]byte, 0)
    res = append(res, chars...)
    res = append(res, chars...)
    return res
}

func main() {
    // create tester by function
    work := unittest.CreateWork(doubleList)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
