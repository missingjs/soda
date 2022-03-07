import soda.groovy.unittest.*

class Solution {
    char nextChar(char ch) {
        (ch + 1) as char
    }
}

def work = TestWork.create(new Solution().&nextChar)
// work.setValidator { i, j -> i == j }
println work.run(System.in.getText('UTF-8'))
