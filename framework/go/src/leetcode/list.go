package leetcode

type ListNode struct {
	Val  int
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

type ListFactoryType struct{}

var ListFactory ListFactoryType

func (f *ListFactoryType) Create(raw []int) *ListNode {
	return ListCreate(raw)
}

func (f *ListFactoryType) Dump(head *ListNode) []int {
	return ListDump(head)
}

func (f *ListFactoryType) CreateSlice(data [][]int) []*ListNode {
	res := make([]*ListNode, len(data))
	for i := range res {
		res[i] = f.Create(data[i])
	}
	return res
}

func (f *ListFactoryType) DumpSlice(nodes []*ListNode) [][]int {
	res := make([][]int, len(nodes))
	for i := range nodes {
		res[i] = f.Dump(nodes[i])
	}
	return res
}
