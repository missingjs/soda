package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

var p int

func deserialize(s string) *NestedInteger {
    p = 0
    return parse(s)
}

func parse(s string) *NestedInteger {
    if (s[p] == '[') {
        p++
        var root = NewNestedInteger()
        for s[p] != ']' {
            root.Add(parse(s))
            if s[p] == ',' {
                p++
            }
        }
        p++
        return root
    }

    var negative = false
    if s[p] == '-' {
        p++
        negative = true
    }

    var value = 0
    for p < len(s) && s[p] >= '0' && s[p] <= '9' {
        value = value * 10 + int(s[p]) - '0'
        p++
    }

    if negative {
        value = 0 - value
    }
    return NewNestedIntegerWithInt(value)
}

func main() {
    // create tester by function
    work := unittest.CreateWork(deserialize)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
