package leetcode

type ListNode struct {
    Val int
    Next *ListNode
}

func ListCreate(listData []int) *ListNode {
    head := ListNode{}
    tail := &head
    for _, val := range listData {
        node := new(ListNode)
        node.Val = val
        tail.Next = node
        tail = node
    }
    return head.Next
}

func ListDump(head *ListNode) []int {
    data := make([]int, 0)
    for ; head != nil; head = head.Next {
        data = append(data, head.Val)
    }
    return data
}
