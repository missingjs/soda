package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func multiply(a []float64, b []float64) []float64 {
    res := make([]float64, len(a))
    for i := 0; i < len(a); i++ {
        res[i] = a[i] * b[i]
    }
    return res
}

func main() {
    // create tester by function
    work := unittest.CreateWork(multiply)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(unittest.Validators.ForSlice(reflect.TypeOf(float64(0)), true))
    // work.CompareSerial = true
    work.Run()
}
