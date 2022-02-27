package main

import (
    "fmt"
    "math"
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
    quickSelect(nodes, 0, len(nodes)-1, k)
    res := make([]int, k)
    for i := 0; i < k; i++ {
        res[i] = nodes[i].Value
    }
    return res
}

func quickSelect(nodes []*Node, start int, end int, index int) {
    for start < end {
        mid := (start + end) / 2
        placeMedian3(nodes, start, mid, end)
        k := partition(nodes, start, end, mid)
        if k == index {
            return
        } else if k > index {
            end = k - 1
        } else {
            start = k + 1
        }
    }
}

func partition(nodes []*Node, start int, end int, pivot int) int {
    d := nodes[pivot].Diff
    swap(nodes, pivot, end)
    p := start
    for i := start; i <= end; i++ {
        if nodes[i].Diff < d {
            if p != i {
                swap(nodes, p, i)
            }
            p++
        }
    }
    swap(nodes, p, end)
    return p
}

func placeMedian3(nodes []*Node, start int, mid int, end int) {
    if nodes[start].Diff > nodes[mid].Diff {
        swap(nodes, start, mid)
    }
    if nodes[start].Diff > nodes[end].Diff {
        swap(nodes, start, end)
    }
    if nodes[mid].Diff > nodes[end].Diff {
        swap(nodes, mid, end)
    }
}

func collect(root *TreeNode, nodes *[]*Node, target float64) {
    if root == nil {
        return
    }
    *nodes = append(*nodes, withTarget(root.Val, target))
    collect(root.Left, nodes, target)
    collect(root.Right, nodes, target)
}

func swap(nodes []*Node, i, j int) {
    nodes[i], nodes[j] = nodes[j], nodes[i]
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
