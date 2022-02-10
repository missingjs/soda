namespace Soda.Leetcode;

public static class TreeFactory
{
    public static TreeNode Create(IList<int?> treeData)
    {
        if (treeData.Count == 0 || !treeData[0].HasValue) {
			return null;
		}
		
		TreeNode root = new TreeNode(treeData[0].Value);
		var qu = new Queue<TreeNode>();
		qu.Enqueue(root);
		for (int i = 1; i < treeData.Count; ) {
			var node = qu.Dequeue();
            if (treeData[i] is int leftVal) {
                node.left = new TreeNode(leftVal);
				qu.Enqueue(node.left);
            }
			++i;
			if (i == treeData.Count) {
				break;
			}
            if (treeData[i] is int rightVal) {
                node.right = new TreeNode(rightVal);
				qu.Enqueue(node.right);
            }
			++i;
		}
		return root;
    }

    public static IList<int?> Dump(TreeNode root) {
		var data = new List<int?>();
		if (root == null) {
			return data;
		}
		
		var curr = new List<TreeNode>();
        var next = new List<TreeNode>();
        var order = new List<TreeNode>();
		curr.Add(root);
		while (curr.Count > 0) {
			next.Clear();
			foreach (TreeNode node in curr) {
				order.Add(node);
				if (node != null) {
					next.Add(node.left);
					next.Add(node.right);
				}
			}
			var temp = curr;
			curr = next;
			next = temp;
		}
		
		int i = order.Count - 1;
		while (order[i] == null) {
			--i;
		}
		for (int j = 0; j <= i; ++j) {
			if (order[j] != null) {
				data.Add(order[j].val);
			} else {
				data.Add(null);
			}
		}
		return data;
	}
}