package main

import (
    "math"
    "fmt"
    . "missingjs.com/soda/ds"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger

type Node struct {
    Diff  float64
    Value int
}

func withTarget(value int, target float64) *Node {
    return &Node {
        Diff: math.Abs(target - float64(value)),
        Value: value,
    }
}

func closestKValues(root *TreeNode, target float64, k int) []int {
    queue := NewGenericPriorityQueue(func(a, b *Node)bool { return a.Diff > b.Diff })
    solve(root, target, k, queue)

    var res []int
    for !queue.Empty() {
        res = append(res, queue.Pop().Value)
    }
    return res
}

func solve(root *TreeNode, target float64, k int, queue *GenericPriorityQueue[*Node]) {
    if root == nil {
        return;
    }

    node := withTarget(root.Val, target)
    if (queue.Size() == k) {
        if (node.Diff < queue.Top().Diff) {
            queue.Pop()
            queue.Push(node)
        }
    } else {
        queue.Push(node)
    }

    solve(root.Left, target, k, queue)
    solve(root.Right, target, k, queue)
}

func main() {
    // create tester by function
    work := unittest.CreateWork(closestKValues)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    work.SetValidator(unittest.Validators.ForSlice(nil, false))
    work.CompareSerial = true
    fmt.Print(work.Exec(unittest.Utils.FromStdin()))
}

var _x Dummy
