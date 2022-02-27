import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        var stk = new ArrayList<TreeNode>();
        stk.add(root);
        var last = root;
        var foundOne = false;
        var index = -1;

        if (root == p || root == q) {
            foundOne = true;
            index = 0;
        }

        while (!stk.isEmpty()) {
            var node = stk.get(stk.size()-1);
            if (node.left != null && last != node.left && last != node.right) {
                if (node.left == p || node.left == q) {
                    if (!foundOne) {
                        index = stk.size();
                        foundOne = true;
                    } else {
                        return stk.get(index);
                    }
                }
                stk.add(node.left);
            } else if (node.right != null && last != node.right) {
                if (node.right == p || node.right == q) {
                    if (!foundOne) {
                        index = stk.size();
                        foundOne = true;
                    } else {
                        return stk.get(index);
                    }
                }
                stk.add(node.right);
            } else {
                last = node;
                if (index == stk.size() - 1) {
                    --index;
                }
                stk.remove(stk.size()-1);
            }
        }
        return null;
    }
}

class Driver {
    public int exec(TreeNode root, int p, int q) {
        var pNode = findNode(root, p);
        var qNode = findNode(root, q);
        return new Solution().lowestCommonAncestor(root, pNode, qNode).val;
    }

    private TreeNode findNode(TreeNode root, int val) {
        if (root == null) {
            return null;
        }
        if (root.val == val) {
            return root;
        }
        var L = findNode(root.left, val);
        return L != null ? L : findNode(root.right, val);
    }
}

public class Leet implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create3(new Driver()::exec);
        // var work = GenericTestWork.forStruct(Solution.class);
        // var work = TestWork.forObject(new Solution(), "METHOD");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Leet().apply(Utils.fromStdin()));
    }

}
