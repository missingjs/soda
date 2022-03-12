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
        quickSelect(nodes, 0, nodes.size()-1, k)
        nodes[0..k-1].collect { it.value }
    }

    private void quickSelect(List<Node> nodes, int start, int end, int index) {
        while (start < end) {
            int mid = (start + end) / 2
            placeMedian3(nodes, start, mid, end)
            int k = partition(nodes, start, end, mid)
            if (k == index) {
                return
            } else if (k > index) {
                end = k - 1
            } else {
                start = k + 1
            }
        }
    }

    private int partition(List<Node> nodes, int start, int end, int pivot) {
        def d = nodes[pivot].diff
        swap(nodes, pivot, end)
        int p = start
        for (int i = start; i <= end; ++i) {
            if (nodes[i].diff < d) {
                if (p != i) {
                    swap(nodes, p, i)
                }
                ++p
            }
        }
        swap(nodes, p, end)
        p
    }

    private void placeMedian3(List<Node> nodes, int start, int mid, int end) {
        if (nodes[start].diff > nodes[mid].diff) {
            swap(nodes, start, mid)
        }
        if (nodes[start].diff > nodes[end].diff) {
            swap(nodes, start, end)
        }
        if (nodes[mid].diff > nodes[end].diff) {
            swap(nodes, mid, end)
        }
    }

    private void swap(List<Node> nodes, int i, int j) {
        def temp = nodes[i]
        nodes[i] = nodes[j]
        nodes[j] = temp
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

class LeetWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&closestKValues)
        // def work = TestWork.forStruct(STRUCT)
        work.validator = Validators.forList(Integer, false)
        work.compareSerial = true
        work.run(input)
    }
}

println new LeetWork()(System.in.getText('UTF-8'))
