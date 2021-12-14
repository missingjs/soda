package leetcode

import "missingjs.com/soda/util"

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func TreeCreate(data util.OptionalIntSlice) *TreeNode {
	if len(data) == 0 {
		return nil
	}

	root := new(TreeNode)
	root.Val = data[0].Get()
	qu := make([]*TreeNode, 0)
	qu = append(qu, root)

	index := 1
	for index < len(data) {
		node := qu[0]
		qu = qu[1:]
		if !data[index].IsNull() {
			node.Left = new(TreeNode)
			node.Left.Val = data[index].Get()
			qu = append(qu, node.Left)
		}
		index++
		if index == len(data) {
			break
		}
		if !data[index].IsNull() {
			node.Right = new(TreeNode)
			node.Right.Val = data[index].Get()
			qu = append(qu, node.Right)
		}
		index++
	}
	return root
}

func TreeDump(root *TreeNode) util.OptionalIntSlice {
	var data util.OptionalIntSlice
	if root == nil {
		return data
	}

	curr := make([]*TreeNode, 0)
	next := make([]*TreeNode, 0)
	order := make([]*TreeNode, 0)

	curr = append(curr, root)
	for len(curr) > 0 {
		next = next[:0]
		for _, node := range curr {
			order = append(order, node)
			if node != nil {
				next = append(next, node.Left)
				next = append(next, node.Right)
			}
		}
		curr, next = next, curr
	}

	i := len(order) - 1
	for order[i] == nil {
		i--
	}
	for j := 0; j <= i; j++ {
		var v util.OptionalInt
		if order[j] != nil {
			v.Set(order[j].Val)
		}
		data = append(data, v)
	}
	return data
}
