package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func reverseVowels(s string) string {
    isv := make([]bool, 128)
    vs := "aeiouAEIOU"
    for _, ch := range vs {
        isv[ch] = true
    }

    buf := []byte(s)
    i, j := 0, len(s) - 1
    for i < j {
        for i < j && !isv[buf[i]] {
            i++
        }
        for i < j && !isv[buf[j]] {
            j--
        }
        if i < j {
            temp := buf[i]
            buf[i] = buf[j]
            buf[j] = temp
            i++
            j--
        }
    }
    return string(buf)
}

func main() {
    // create tester by function
    work := unittest.CreateWork(reverseVowels)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
