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
        var nodes = new ArrayList<Node>();
        collect(root, nodes, target);
        quickSelect(nodes, 0, nodes.size()-1, k);
        var res = new int[k];
        for (int i = 0; i < k; ++i) {
            res[i] = nodes.get(i).value;
        }
        return Arrays.stream(res).boxed().collect(Collectors.toList());
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
        var d = nodes.get(pivot).diff;
        swap(nodes, pivot, end);
        int p = start;
        for (int i = start; i <= end; ++i) {
            if (nodes.get(i).diff < d) {
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
        if (nodes.get(start).diff > nodes.get(mid).diff) {
            swap(nodes, start, mid);
        }
        if (nodes.get(start).diff > nodes.get(end).diff) {
            swap(nodes, start, end);
        }
        if (nodes.get(mid).diff > nodes.get(end).diff) {
            swap(nodes, mid, end);
        }
    }

    private void swap(List<Node> nodes, int i, int j) {
        var temp = nodes.get(i);
        nodes.set(i, nodes.get(j));
        nodes.set(j, temp);
    }

    private void collect(TreeNode root, List<Node> nodes, double target) {
        if (root == null) {
            return;
        }
        nodes.add(Node.withTarget(root.val, target));
        collect(root.left, nodes, target);
        collect(root.right, nodes, target);
    }
}

public class Leet implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create3(new Solution()::closestKValues);
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
