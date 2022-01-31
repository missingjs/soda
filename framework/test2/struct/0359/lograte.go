package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

type Logger struct {
    MsgMap map[string]int
    Limit  int
}


func Constructor() Logger {
    return Logger { make(map[string]int), 10 }
}


func (this *Logger) ShouldPrintMessage(timestamp int, message string) bool {
    if last, ok := this.MsgMap[message]; ok && timestamp - last < this.Limit {
        return false
    }
    this.MsgMap[message] = timestamp
    return true
}

func main() {
    // create tester by function
    // work := unittest.CreateWork(FUNCTION)
    // OR create by struct factory
    work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
