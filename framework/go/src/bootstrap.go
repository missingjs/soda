package main

import (
    "fmt"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger

func add(a int, b int) int {
    return a + b
}

func main() {
    // create tester by function
    work := unittest.CreateWork(add)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    fmt.Print(work.Exec(unittest.Utils.FromStdin()))
}

var _x Dummy
