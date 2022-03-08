import soda.groovy.unittest.*

class Solution {
    List<Character> doubleList(char[] chars) {
        def res = []
        for (char ch in chars) {
            res << ch
        }
        for (char ch in chars) {
            res << ch
        }
        res
    }
}

def work = TestWork.create(new Solution().&doubleList)
// work.setValidator { i, j -> i == j }
println work.run(System.in.getText('UTF-8'))
