import soda.groovy.unittest.*

class Solution {
    private int[] memo

    int integerBreak(int n) {
        memo = new int[59]
        solve(n)
    }

    private int solve(int n) {
        if (n == 1) {
            return 1
        }
        if (memo[n] > 0) {
            return memo[n]
        }
        def res = 0
        for (int i = 1; i < n; ++i) {
            res = Math.max(i * (n-i), res)
            res = Math.max(i * solve(n-i), res)
        }
        memo[n] = res
        res
    }
}

class IntbreakWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&integerBreak)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new IntbreakWork()(System.in.getText('UTF-8'))
