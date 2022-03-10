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

def work = TestWork.create(new Solution().&multiply)
// work.validator = { i, j -> i == j }
println work.run(System.in.getText('UTF-8'))
