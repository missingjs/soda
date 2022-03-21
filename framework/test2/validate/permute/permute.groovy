import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    String[] permutation(char[] chars, int n) {
        def res = []
        char[] buf = new char[n]
        solve(chars, 0, buf, 0, res)
        res.toArray(String[]::new)
    }

    private void solve(char[] chars, int i, char[] buf, int j, java.util.List<String> res) {
        if (j == buf.length) {
            res << new String(buf)
            return
        }
        for (int k = i; k < chars.length; ++k) {
            char temp = chars[i]
            chars[i] = chars[k]
            chars[k] = temp
            buf[j] = chars[i]
            solve(chars, i+1, buf, j+1, res)
            temp = chars[i]
            chars[i] = chars[k]
            chars[k] = temp
        }
    }
}

class PermuteWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&permutation)
        // def work = TestWork.forStruct(STRUCT)
        work.validator = Validators.forArray(String, false)
        work.compareSerial = true
        work.run(input)
    }
}

println new PermuteWork()(System.in.getText('UTF-8'))
