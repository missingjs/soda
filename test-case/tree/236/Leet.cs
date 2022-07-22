using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public TreeNode LowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        var stk = new List<TreeNode>();
        stk.Add(root);
        var last = root;
        var foundOne = false;
        var index = -1;

        if (root == p || root == q) {
            foundOne = true;
            index = 0;
        }

        while (stk.Count > 0) {
            var node = stk[stk.Count-1];
            if (node.left != null && last != node.left && last != node.right) {
                if (node.left == p || node.left == q) {
                    if (!foundOne) {
                        index = stk.Count;
                        foundOne = true;
                    } else {
                        return stk[index];
                    }
                }
                stk.Add(node.left);
            } else if (node.right != null && last != node.right) {
                if (node.right == p || node.right == q) {
                    if (!foundOne) {
                        index = stk.Count;
                        foundOne = true;
                    } else {
                        return stk[index];
                    }
                }
                stk.Add(node.right);
            } else {
                last = node;
                if (index == stk.Count - 1) {
                    --index;
                }
                stk.RemoveAt(stk.Count-1);
            }
        }
        return null;
    }
}

class Driver {
    public int exec(TreeNode root, int p, int q) {
        var pNode = findNode(root, p);
        var qNode = findNode(root, q);
        return new Solution().LowestCommonAncestor(root, pNode, qNode).val;
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

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Driver().exec));
        // var work = WorkFactory.ForStruct<STRUCT>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
