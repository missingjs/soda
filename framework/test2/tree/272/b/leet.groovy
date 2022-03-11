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
        def nodes = []
        collect(root, nodes, target)
        Collections.sort(nodes, (n1, n2) -> Double.compare(n1.diff, n2.diff))
        nodes[0..k-1].collect { it.value }
    }

    private void collect(TreeNode root, List<Node> nodes, double target) {
        if (!root) {
            return
        }
        nodes << Node.withTarget(root.val, target)
        collect(root.left, nodes, target)
        collect(root.right, nodes, target)
    }
}

def work = TestWork.create(new Solution().&closestKValues)
// def work = TestWork.forStruct(STRUCT)
work.validator = Validators.forList(Integer, false)
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
