import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    int add(int a, int b) {
        a + b
    }
}

def work = TestWork.create(new Solution().&add)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))