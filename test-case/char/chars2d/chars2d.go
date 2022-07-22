package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func toUpper(matrix [][]byte) [][]byte {
    diff := 'a' - 'A'
    for i := 0; i < len(matrix); i++ {
        for j := 0; j < len(matrix[i]); j++ {
            matrix[i][j] -= byte(diff)
        }
    }
    return matrix
}

func main() {
    // create tester by function
    work := unittest.CreateWork(toUpper)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
