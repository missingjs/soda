package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func matrixMultiply(a, b [][]float64) [][]float64 {
    var rows = len(a)
    var cols = len(b[0])
    var res = make([][]float64, rows)
    for i := range res {
        res[i] = make([]float64, cols)
    }

    for i := 0; i < rows; i++ {
        for j := 0; j < cols; j++ {
            var c = 0.0
            for k := 0; k < len(b); k++ {
                c += a[i][k] * b[k][j]
            }
            res[i][j] = c
        }
    }
    return res
}

func main() {
    // create tester by function
    work := unittest.CreateWork(matrixMultiply)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    // work.CompareSerial = true
    work.Run()
}
