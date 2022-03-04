package main

import (
    "fmt"
    . "missingjs.com/soda/ds"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger

func containsNearbyAlmostDuplicate(nums []int, k int, t int) bool {
    imap := NewSortedMap(func(i, j int)int { return i - j; })
    i, j := 0, 0
    for j < len(nums) {
        if j - i <= k {
            val := nums[j]
            j++
            lower := val - t
            upper := val + t
            key := imap.LowerBoundKey(lower)
            if key != nil && key.(int) <= upper {
                return true
            }
            imap.Set(val, imap.GetOrDefault(val, 0).(int) + 1)
        } else {
            val := nums[i]
            i++
            c := imap.Get(val).(int) - 1
            if c == 0 {
                imap.Del(val)
            } else {
                imap.Set(val, c)
            }
        }
    }
    return false
}

func main() {
    // create tester by function
    work := unittest.CreateWork(containsNearbyAlmostDuplicate)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    fmt.Print(work.Exec(unittest.Utils.FromStdin()))
}

var _x Dummy