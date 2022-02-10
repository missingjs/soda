using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public TreeNode Mirror(TreeNode root)
    {
        if (root == null) {
            return null;
        }
        Mirror(root.left);
        Mirror(root.right);
        var temp = root.left;
        root.left = root.right;
        root.right = temp;
        return root;
    }
}

public class Mirror
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().Mirror));
        // var work = WorkFactory.ForStruct<STRUCT>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
