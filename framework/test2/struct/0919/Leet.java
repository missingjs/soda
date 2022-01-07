import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class CBTInserter {

    private Deque<TreeNode> qu = new ArrayDeque<>();
    private TreeNode root;

    public CBTInserter(TreeNode root) {
        this.root = root;
        qu.offerLast(root);
        while (!qu.isEmpty()) {
            var node = qu.peekFirst();
            if (node.left == null) {
                break;
            }
            qu.offerLast(node.left);
            if (node.right == null) {
                break;
            }
            qu.offerLast(node.right);
            qu.pollFirst();
        }
    }
    
    public int insert(int val) {
        var node = new TreeNode(val);
        var head = qu.peekFirst();
        qu.offerLast(node);
        if (head.left == null) {
            head.left = node;
        } else {
            head.right = node;
            qu.pollFirst();
        }
        return head.val;
    }
    
    public TreeNode get_root() {
        return root;
    }
}

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        // var work = new TestWork(new Solution(), "METHOD");
        var work = TestWork.forStruct(CBTInserter.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

}
