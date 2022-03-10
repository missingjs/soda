import soda.groovy.unittest.*

class Solution {
    public double divide(double a, double b) {
        return a / b;
    }
}

def work = TestWork.create(new Solution().&divide)
// work.validator = { i, j -> i == j }
println work.run(System.in.getText('UTF-8'))
