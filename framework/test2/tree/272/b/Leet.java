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

        Collections.sort(nodes, (n1, n2) -> Double.compare(n1.diff, n2.diff));
        var res = new int[k];
        for (int i = 0; i < k; ++i) {
            res[i] = nodes.get(i).value;
        }
        return Arrays.stream(res).boxed().collect(Collectors.toList());
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
