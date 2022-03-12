import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    public void reverseWords(char[] s) {
        if (s.length == 0) {
            return
        }

        reverse(s, 0, s.length - 1)

        int N = s.length
        int i = 0, j = 0
        while (j < N) {
            if (s[j] == ' ') {
                reverse(s, i, j-1)
                i = j + 1
            }
            ++j
        }
        if (i < j) {
            reverse(s, i, j-1)
        }
    }

    void reverse(char[] s, int i, int j) {
        while (i < j) {
            char temp = s[i]
            s[i] = s[j]
            s[j] = temp
            ++i
            --j
        }
    }
}

class RevwordWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&reverseWords)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new RevwordWork()(System.in.getText('UTF-8'))
