package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

type CBTInserter struct {
    qu []*TreeNode
    root *TreeNode
}


func Constructor(root *TreeNode) CBTInserter {
    cbt := CBTInserter{ make([]*TreeNode,0), root }
    cbt.qu = append(cbt.qu, root)
    for len(cbt.qu) > 0 {
        node := cbt.qu[0]
        if node.Left == nil {
            break
        }
        cbt.qu = append(cbt.qu, node.Left)
        if node.Right == nil {
            break
        }
        cbt.qu = append(cbt.qu, node.Right)
        cbt.qu = cbt.qu[1:]
    }
    return cbt
}


func (this *CBTInserter) Insert(val int) int {
    node := &TreeNode {val,nil,nil}
    head := this.qu[0]
    this.qu = append(this.qu, node)
    if head.Left == nil {
        head.Left = node
    } else {
        head.Right = node
        this.qu = this.qu[1:]
    }
    return head.Val
}


func (this *CBTInserter) Get_root() *TreeNode {
    return this.root
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
