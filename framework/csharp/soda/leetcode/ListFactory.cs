namespace Soda.Leetcode;

public static class ListFactory {
	
	public static ListNode Create(IList<int> values) {
		ListNode head = new ListNode(-1);
		ListNode tail = head;
		foreach (int value in values) {
			ListNode node = new ListNode(value);
			tail.next = node;
			tail = node;
		}
		return head.next;
	}
	
	public static IList<int> Dump(ListNode head) {
		var list = new List<int>();
		while (head != null) {
			list.Add(head.val);
			head = head.next;
		}
		return list;
	}
	
}
