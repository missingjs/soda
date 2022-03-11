import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        def stk = []
        stk << root
        def last = root
        def foundOne = false
        def index = -1

        if (root == p || root == q) {
            foundOne = true
            index = 0
        }

        while (!stk.isEmpty()) {
            def node = stk.get(stk.size()-1)
            if (node.left != null && last != node.left && last != node.right) {
                if (node.left == p || node.left == q) {
                    if (!foundOne) {
                        index = stk.size()
                        foundOne = true
                    } else {
                        return stk[index]
                    }
                }
                stk << node.left
            } else if (node.right != null && last != node.right) {
                if (node.right == p || node.right == q) {
                    if (!foundOne) {
                        index = stk.size()
                        foundOne = true
                    } else {
                        return stk[index]
                    }
                }
                stk << node.right
            } else {
                last = node
                if (index == stk.size() - 1) {
                    --index
                }
                stk.remove(stk.size()-1)
            }
        }
        null
    }
}

class Driver {
    int exec(TreeNode root, int p, int q) {
        def pNode = findNode(root, p)
        def qNode = findNode(root, q)
        return new Solution().lowestCommonAncestor(root, pNode, qNode).val
    }

    private TreeNode findNode(TreeNode root, int val) {
        if (!root) {
            return null
        }
        if (root.val == val) {
            return root
        }
        def L = findNode(root.left, val)
        L != null ? L : findNode(root.right, val)
    }
}

def work = TestWork.create(new Driver().&exec)
// def work = TestWork.forStruct(STRUCT)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
