import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Solution {
    public int[][] findLeaves(TreeNode root) {
        var res = new ArrayList<List<Integer>>();
        for (int i = 0; i < 100; ++i) {
            res.add(new ArrayList<>());
        }
        int r = solve2(root, res);
        int[][] ans = new int[r][];
        for (int i = 0; i < r; ++i) {
            ans[i] = res.get(i).stream().mapToInt(k -> k).toArray();
        }
        return ans;
    }

    private int solve2(TreeNode root, List<List<Integer>> res) {
        if (root == null) {
            return 0;
        }
        int R = solve2(root.right, res);
        int L = solve2(root.left, res);
        int index = Math.max(L, R);
        res.get(index).add(root.val);
        return index + 1;
    }
}

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "findLeaves");
        // var work = TestWork.forStruct(Struct.class);
        work.setValidator(Validators.forIntArray2d(true, false));
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

}
