import soda.groovy.unittest.*

class Solution {
    public double divide(double a, double b) {
        return a / b;
    }
}

class FloatingWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&divide)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        // work.compareSerial = true
        work.run(input)
    }
}

println new FloatingWork()(System.in.getText('UTF-8'))
