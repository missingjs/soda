import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    static class Node {
        public double diff;
        public int value;
        public Node(double d, int v) {
            diff = d;
            value = v;
        }
        public static Node withTarget(int value, double target) {
            return new Node(Math.abs(target - value), value);
        }
    }

    public List<Integer> closestKValues(TreeNode root, double target, int k) {
        // largest node.diff first
        var queue = new PriorityQueue<Node>((n1, n2) -> Double.compare(n2.diff, n1.diff));
        solve(root, target, k, queue);
        
        var res = new ArrayList<Integer>();
        while (!queue.isEmpty()) {
            res.add(queue.poll().value);
        }
        return res;
    }

    private void solve(TreeNode root, double target, int k, PriorityQueue<Node> queue) {
        if (root == null) {
            return;
        }

        var node = Node.withTarget(root.val, target);
        if (queue.size() == k) {
            if (node.diff < queue.peek().diff) {
                queue.poll();
                queue.offer(node);
            }
        } else {
            queue.offer(node);
        }

        solve(root.left, target, k, queue);
        solve(root.right, target, k, queue);
    }
}

public class Leet implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create(new Solution()::closestKValues);
        // var work = GenericTestWork.forStruct(Solution.class);
        // var work = TestWork.forObject(new Solution(), "METHOD");
        // var work = TestWork.forStruct(Struct.class);
        work.setValidator(Validators.forList(Integer.class, false));
        work.setCompareSerial(true);
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Leet().apply(Utils.fromStdin()));
    }

}
