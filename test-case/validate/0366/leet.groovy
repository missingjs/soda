import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    int[][] findLeaves(TreeNode root) {
        def res = []
        for (int i = 0; i < 100; ++i) {
            res << []
        }
        int r = solve2(root, res)
        res[0..r-1].collect({ it.toArray() }).toArray()
    }

    private int solve2(TreeNode root, List<List<Integer>> res) {
        if (!root) {
            return 0
        }
        int R = solve2(root.right, res)
        int L = solve2(root.left, res)
        int index = Math.max(L, R)
        res[index].add(root.val)
        index + 1
    }
}

class LeetWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&findLeaves)
        // def work = TestWork.forStruct(STRUCT)
        work.validator = Validators.forIntArray2d(true, false)
        work.compareSerial = true
        work.run(input)
    }
}

println new LeetWork()(System.in.getText('UTF-8'))
