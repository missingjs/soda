package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

type TopVotedCandidate struct {
    N      int
    times  []int
    winner []int
}


func Constructor(persons []int, times []int) TopVotedCandidate {
    var tvc = TopVotedCandidate {
        N: len(persons),
        times: times,
        winner: make([]int, len(persons)),
    }
    counter := make([]int, tvc.N+1)
    win := 0
    for i := 0; i < tvc.N; i++ {
        counter[persons[i]]++
        if counter[persons[i]] >= counter[win] {
            win = persons[i]
        }
        tvc.winner[i] = win
    }
    return tvc
}


func (this *TopVotedCandidate) Q(t int) int {
    if t >= this.times[len(this.times)-1] {
        return this.winner[this.N-1]
    }
    low, high := 0, this.N-1
    for low < high {
        mid := (low + high) / 2
        if t <= this.times[mid] {
            high = mid
        } else {
            low = mid + 1
        }
    }
    if t == this.times[low] {
        return this.winner[low]
    } else {
        return this.winner[low-1]
    }
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
