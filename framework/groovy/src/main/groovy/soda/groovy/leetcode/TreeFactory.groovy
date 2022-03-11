package soda.groovy.leetcode

class TreeFactory {

    static TreeNode create(List<Integer> treeData) {
        if (treeData.size() == 0) {
            return null
        }

        TreeNode root = new TreeNode(treeData[0])
        def qu = new ArrayDeque<TreeNode>()
        qu.offerLast(root)
        for (int i = 1; i < treeData.size(); ) {
            def node = qu.pollFirst()
            if (treeData[i] != null) {
                node.left = new TreeNode(treeData[i])
                qu.offerLast(node.left)
            }
            ++i
            if (i == treeData.size()) {
                break
            }
            if (treeData[i] != null) {
                node.right = new TreeNode(treeData[i])
                qu.offerLast(node.right)
            }
            ++i
        }
        root
    }

    static List<Integer> dump(TreeNode root) {
        List<Integer> data = []
        if (root == null) {
            return data
        }

        List<TreeNode> curr = [], next = [], order = []
        curr << root
        while (curr.size() > 0) {
            next.clear()
            for (TreeNode node in curr) {
                order.add(node)
                if (node != null) {
                    next << node.left
                    next << node.right
                }
            }
            (curr, next) = [next, curr]
        }

        int i = order.size() - 1
        while (order[i] == null) {
            --i
        }
        for (int j = 0; j <= i; ++j) {
            if (order[j] != null) {
                data << order[j].val
            } else {
                data << null
            }
        }
        data
    }

}
