package main

import (
    "fmt"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger

func moveZeroes(nums []int)  {
    p := 0
    for i := 0; i < len(nums); i++ {
        if nums[i] != 0 {
            if i != p {
                nums[i], nums[p] = nums[p], nums[i]
            }
            p++
        }
    }
    for p < len(nums) {
        nums[p] = 0
        p++
    }
}

func main() {
    // create tester by function
    work := unittest.CreateWork(moveZeroes)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    fmt.Print(work.Exec(unittest.Utils.FromStdin()))
}

var _x Dummy
