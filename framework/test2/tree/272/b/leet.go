package main

import (
    "fmt"
    "math"
    "sort"
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
    var nodes []*Node
    collect(root, &nodes, target)
    sort.Slice(nodes, func (i, j int) bool { return nodes[i].Diff < nodes[j].Diff })
    res := make([]int, k)
    for i := 0; i < k; i++ {
        res[i] = nodes[i].Value
    }
    return res
}

func collect(root *TreeNode, nodes *[]*Node, target float64) {
    if root == nil {
        return
    }
    *nodes = append(*nodes, withTarget(root.Val, target))
    collect(root.Left, nodes, target)
    collect(root.Right, nodes, target)
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
