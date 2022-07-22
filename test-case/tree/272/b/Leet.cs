using Soda.Unittest;
using Soda.Leetcode;

class Node {
    public double Diff { get; private set; }
    public int Value { get; private set; }

    public Node(double d, int v) {
        Diff = d;
        Value = v;
    }

    public static Node withTarget(int value, double target) {
        return new Node(Math.Abs(target - value), value);
    }
}

public class Solution {
    public IList<int> ClosestKValues(TreeNode root, double target, int k) {
        var nodes = new List<Node>();
        collect(root, nodes, target);
        nodes = nodes.OrderBy(n => n.Diff).ToList();
        var res = new List<int>();
        for (int i = 0; i < k; ++i) {
            res.Add(nodes[i].Value);
        }
        return res;
    }

    private void collect(TreeNode root, List<Node> nodes, double target) {
        if (root == null) {
            return;
        }
        nodes.Add(Node.withTarget(root.val, target));
        collect(root.left, nodes, target);
        collect(root.right, nodes, target);
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().ClosestKValues));
        // var work = WorkFactory.ForStruct<STRUCT>();
        work.SetValidator(Validators.ForList<int>(false));
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
