package main

import (
    "math/rand"
    "reflect"

    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

type Solution struct {
    Original []int
}


func Constructor(nums []int) Solution {
    cpy := make([]int, 0)
    cpy = append(cpy, nums...)
    return Solution {
        Original: cpy,
    }
}


func (this *Solution) Reset() []int {
    cpy := make([]int, 0)
    cpy = append(cpy, this.Original...)
    return cpy
}


func (this *Solution) Shuffle() []int {
    res := this.Reset()
    for s := len(res); s > 0; s-- {
        i := rand.Intn(s)
        j := s - 1
        if i != j {
            res[i], res[j] = res[j], res[i]
        }
    }
    return res
}

func main() {
    // create tester by function
    // work := unittest.CreateWork(FUNCTION)
    // OR create by struct factory
    work := unittest.CreateWorkForStruct(Constructor)
    validator := func (expect, result []interface{}) bool {
        arguments := work.Arguments
        commands := arguments[0].([]string)
        sliceCmp := unittest.Validators.ForSlice(nil, false)
        for i := 1; i < len(commands); i++ {
            cmd := commands[i]
            if cmd == "shuffle" {
                evalues := reflect.ValueOf(expect[i])
                var arr []int
                for i := 0; i < evalues.Len(); i++ {
                    arr = append(arr, int(evalues.Index(i).Interface().(float64)))
                }
                if !sliceCmp(arr, result[i]) {
                    return false
                }
            }
        }
        return true
    }
    work.SetValidator(validator)
    work.CompareSerial = true
    work.Run()
}
