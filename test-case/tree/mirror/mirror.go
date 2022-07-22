package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func mirror(root *TreeNode) *TreeNode {
    if root == nil {
        return nil
    }
    mirror(root.Left)
    mirror(root.Right)
    temp := root.Left
    root.Left = root.Right
    root.Right = temp
    return root
}

func main() {
    // create tester by function
    work := unittest.CreateWork(mirror)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
