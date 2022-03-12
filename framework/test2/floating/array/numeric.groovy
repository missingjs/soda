import soda.groovy.unittest.*

class Solution {
    public List<Double> multiply(double[] a, double[] b) {
        def res = new double[a.length]
        for (int i = 0; i < res.length; ++i) {
            res[i] = a[i] * b[i]
        }
        res
    }
}

class NumericWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&multiply)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        // work.compareSerial = true
        work.run(input)
    }
}

println new NumericWork()(System.in.getText('UTF-8'))
