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
        var queue = new PriorityQueue<Node, double>();
        solve(root, target, k, queue);
        var res = new List<int>();
        while (queue.Count > 0) {
            res.Add(queue.Dequeue().Value);
        }
        return res;
    }

    private void solve(TreeNode root, double target, int k, PriorityQueue<Node, double> queue) {
        if (root == null) {
            return;
        }

        var node = Node.withTarget(root.val, target);
        if (queue.Count == k) {
            if (node.Diff < queue.Peek().Diff) {
                queue.Dequeue();
                queue.Enqueue(node, -node.Diff);
            }
        } else {
            queue.Enqueue(node, -node.Diff);
        }

        solve(root.left, target, k, queue);
        solve(root.right, target, k, queue);
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
