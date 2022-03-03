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
        quickSelect(nodes, 0, nodes.Count-1, k);
        var res = new List<int>();
        for (int i = 0; i < k; ++i) {
            res.Add(nodes[i].Value);
        }
        return res;
    }

    private void quickSelect(List<Node> nodes, int start, int end, int index) {
        while (start < end) {
            int mid = (start + end) / 2;
            placeMedian3(nodes, start, mid, end);
            int k = partition(nodes, start, end, mid);
            if (k == index) {
                return;
            } else if (k > index) {
                end = k - 1;
            } else {
                start = k + 1;
            }
        }
    }

    private int partition(List<Node> nodes, int start, int end, int pivot) {
        var d = nodes[pivot].Diff;
        swap(nodes, pivot, end);
        var p = start;
        for (int i = start; i <= end; ++i) {
            if (nodes[i].Diff < d) {
                if (p != i) {
                    swap(nodes, p, i);
                }
                ++p;
            }
        }
        swap(nodes, p, end);
        return p;
    }

    private void placeMedian3(List<Node> nodes, int start, int mid, int end) {
        if (nodes[start].Diff > nodes[mid].Diff) {
            swap(nodes, start, mid);
        }
        if (nodes[start].Diff > nodes[end].Diff) {
            swap(nodes, start, end);
        }
        if (nodes[mid].Diff > nodes[end].Diff) {
            swap(nodes, mid, end);
        }
    }

    private void swap(List<Node> nodes, int i, int j) {
        (nodes[i], nodes[j]) = (nodes[j], nodes[i]);
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
