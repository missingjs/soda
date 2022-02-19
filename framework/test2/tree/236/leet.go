package main

import (
    "fmt"
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger

func lowestCommonAncestor(root, p, q *TreeNode) *TreeNode {
    stk := []*TreeNode { root }
    last := root
    foundOne := false
    // where ancestor is in stk
    index := -1

    if root == p || root == q {
        foundOne = true
        index = 0
    }

    for len(stk) > 0 {
        node := stk[len(stk)-1]
        if node.Left != nil && last != node.Left && last != node.Right {
            if node.Left == p || node.Left == q {
                if !foundOne {
                    index = len(stk)
                    foundOne = true
                } else {
                    return stk[index]
                }
            }
            stk = append(stk, node.Left)
        } else if node.Right != nil && last != node.Right {
            if node.Right == p || node.Right == q {
                if !foundOne {
                    index = len(stk)
                    foundOne = true
                } else {
                    return stk[index]
                }
            }
            stk = append(stk, node.Right)
        } else {
            last = node
            if index == len(stk) - 1 {
                index--
            }
            stk = stk[:len(stk)-1]
        }
    }
    return nil
}

func driver(root *TreeNode, p, q int) int {
    pNode := findNode(root, p)
    qNode := findNode(root, q)
    return lowestCommonAncestor(root, pNode, qNode).Val
}

func findNode(root *TreeNode, val int) *TreeNode {
    if root == nil {
        return nil
    }
    if root.Val == val {
        return root
    }
    L := findNode(root.Left, val)
    if L != nil {
        return L
    }
    return findNode(root.Right, val)
}

func main() {
    // create tester by function
    work := unittest.CreateWork(driver)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    fmt.Print(work.Exec(unittest.Utils.FromStdin()))
}

var _x Dummy
