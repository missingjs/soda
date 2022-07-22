import soda.groovy.unittest.*

class Solution {
    char nextChar(char ch) {
        (ch + 1) as char
    }
}

class SingleWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&nextChar)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new SingleWork()(System.in.getText('UTF-8'))
