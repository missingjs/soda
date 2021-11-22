package main

import _ "missingjs.com/soda/leetcode"
import "missingjs.com/soda/unittest"
import "missingjs.com/soda/util"

var logger = util.Logger

func main() {
    work := unittest.CreateWork(FUNCTION)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    // work.SetArgParser(index, func(s)a)
    // work.SetResultParser(func(s)w)
    // work.SetResultSerializer(func(w)s)
    work.Run()
}
