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

class Chars1dWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&doubleList)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new Chars1dWork()(System.in.getText('UTF-8'))
