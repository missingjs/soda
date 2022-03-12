import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {

    private final int[] original

    Solution(int[] nums) {
        original = nums
    }
    
    int[] reset() {
        def res = new int[original.length]
        System.arraycopy(original, 0, res, 0, original.length)
        res
    }
    
    int[] shuffle() {
        def res = reset()
        for (int s = res.length; s > 0; --s) {
            int i = (int) (Math.random() * s)
            int j = s - 1
            if (i != j) {
                int temp = res[i]
                res[i] = res[j]
                res[j] = temp
            }
        }
        res
    }
}

class LeetWork {
    String call(String input) {
        // def work = TestWork.create(new Solution().&add)
        def work = TestWork.forStruct(Solution)
        work.validator = { List expect, List result ->
            Object[] arguments = work.arguments
            def commands = arguments[0] as List
            def listCmp = Validators.forList(Integer, false)
            for (int i = 1; i < commands.size(); ++i) {
                String cmd = commands[i]
                if (cmd == "shuffle") {
                    if (!listCmp(expect[i], result[i])) {
                        return false
                    }
                }
            }
            return true
        }
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new LeetWork()(System.in.getText('UTF-8'))
