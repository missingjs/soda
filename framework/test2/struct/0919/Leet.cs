using Soda.Unittest;
using Soda.Leetcode;

public class CBTInserter {

    private Queue<TreeNode> qu = new Queue<TreeNode>();

    private TreeNode root;

    public CBTInserter(TreeNode root) {
        this.root = root;
        qu.Enqueue(root);
        while (qu.Count > 0) {
            var node = qu.Peek();
            if (node.left == null) {
                break;
            }
            qu.Enqueue(node.left);
            if (node.right == null) {
                break;
            }
            qu.Enqueue(node.right);
            qu.Dequeue();
        }
    }
    
    public int Insert(int val) {
        var node = new TreeNode(val);
        var head = qu.Peek();
        qu.Enqueue(node);
        if (head.left == null) {
            head.left = node;
        } else {
            head.right = node;
            qu.Dequeue();
        }
        return head.val;
    }
    
    public TreeNode Get_root() {
        return root;
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        // var work = WorkFactory.Create(Utils.Fn(new Solution().Add));
        var work = WorkFactory.ForStruct<CBTInserter>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
