import soda.groovy.unittest.*

class Solution {
    public double[][] matrixMultiply(double[][] a, double[][] b) {
        int rows = a.length, cols = b[0].length
        def res = new double[rows][cols]
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                double c = 0.0
                for (int k = 0; k < b.length; ++k) {
                    c += a[i][k] * b[k][j]
                }
                res[i][j] = c
            }
        }
        res
    }
}

def work = TestWork.create(new Solution().&matrixMultiply)
// work.validator = { i, j -> i == j }
println work.run(System.in.getText('UTF-8'))
