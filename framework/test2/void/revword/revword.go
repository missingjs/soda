package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func reverseWords(s []byte) {
    if len(s) == 0 {
        return
    }
    reverse(s, 0, len(s) - 1)
    N := len(s)
    i, j := 0, 0
    for j < N {
        if s[j] == ' ' {
            reverse(s, i, j-1)
            i = j + 1
        }
        j++
    }
    if i < j {
        reverse(s, i, j-1)
    }
}

func reverse(s []byte, i, j int) {
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
    work := unittest.CreateWork(reverseWords)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
