using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public IList<IList<int>> FindLeaves(TreeNode root) {
        var res = new List<IList<int>>();
        for (int i = 0; i < 100; ++i) {
            res.Add(new List<int>());
        }
        int r = solve2(root, res);
        return res.GetRange(0, r);
    }

    private int solve2(TreeNode root, IList<IList<int>> res)
    {
        if (root == null) {
            return 0;
        }
        int R = solve2(root.right, res);
        int L = solve2(root.left, res);
        int index = Math.Max(L, R);
        res[index].Add(root.val);
        return index + 1;
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().FindLeaves));
        // var work = WorkFactory.ForStruct<STRUCT>();
        work.SetValidator(Validators.ForList2d<int>(true, false));
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
