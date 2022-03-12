import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    int add(int a, int b) {
        a + b
    }
}

class __Bootstrap__ {
    String call(String input) {
        def work = TestWork.create(new Solution().&add)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new __Bootstrap__().call(System.in.getText('UTF-8'))