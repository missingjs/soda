import soda.groovy.unittest.*

class Solution {
    int add(int a, int b) {
        a + b
    }
}

def work = TestWork.create(new Solution().&add)
// work.validator = { i, j -> i == j }
println work.run(System.in.getText('UTF-8'))