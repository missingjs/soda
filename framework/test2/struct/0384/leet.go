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
        for i := 1; i < len(commands); i++ {
            cmd := commands[i]
            if cmd == "shuffle" {
                evalues := reflect.ValueOf(expect[i])
                rvalues := reflect.ValueOf(result[i])
                counts := make(map[int]int)
                for i := 0; i < evalues.Len(); i++ {
                    a := int(evalues.Index(i).Interface().(float64))
                    counts[a]++
                }
                for i := 0; i < rvalues.Len(); i++ {
                    b := rvalues.Index(i).Interface().(int)
                    c := counts[b] - 1
                    if c < 0 {
                        return false
                    }
                }
            }
        }
        return true
    }
    work.SetValidator(validator)
    work.CompareSerial = true
    work.Run()
}
