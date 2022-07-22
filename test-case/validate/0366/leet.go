package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func findLeaves(root *TreeNode) [][]int {
    res := make([][]int, 100)
    r := solve2(root, res)
    return res[:r]
}

func solve2(root *TreeNode, res [][]int) int {
    if root == nil {
        return 0
    }
    R := solve2(root.Right, res)
    L := solve2(root.Left, res)
    index := R
    if L > index {
        index = L
    }
    res[index] = append(res[index], root.Val)
    return index + 1
}

func main() {
    // create tester by function
    work := unittest.CreateWork(findLeaves)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    work.SetValidator(unittest.Validators.ForSlice2d(nil, true, false))
    work.CompareSerial = true
    work.Run()
}
