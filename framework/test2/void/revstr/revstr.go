package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func reverseString(s []byte) {
    i, j := 0, len(s) - 1
    for i < j {
        temp := s[i]
        s[i] = s[j]
        s[j] = temp
        i++
        j--
    }
}

func main() {
    // create tester by function
    work := unittest.CreateWork(reverseString)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
