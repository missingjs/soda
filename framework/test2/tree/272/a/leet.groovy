import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {

    static class Node {
        double diff
        int value
        Node(double d, int v) {
            diff = d
            value = v
        }
        static Node withTarget(int value, double target) {
            new Node(Math.abs(target - value), value)
        }
    }

    List<Integer> closestKValues(TreeNode root, double target, int k) {
        // largest node.diff first
        def queue = new PriorityQueue<Node>((n1, n2) -> Double.compare(n2.diff, n1.diff))
        solve(root, target, k, queue)
        
        def res = []
        while (!queue.isEmpty()) {
            res << queue.poll().value
        }
        res
    }

    private void solve(TreeNode root, double target, int k, PriorityQueue<Node> queue) {
        if (!root) {
            return
        }

        def node = Node.withTarget(root.val, target)
        if (queue.size() == k) {
            if (node.diff < queue.peek().diff) {
                queue.poll()
                queue.offer(node)
            }
        } else {
            queue.offer(node)
        }

        solve(root.left, target, k, queue)
        solve(root.right, target, k, queue)
    }
}

def work = TestWork.create(new Solution().&closestKValues)
// def work = TestWork.forStruct(STRUCT)
work.validator = Validators.forList(Integer, false)
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
