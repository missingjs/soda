import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class NumMatrix {

    private int[][] matrix

    private int[][] bit

    private int rows, cols

    NumMatrix(int[][] matrix) {
        this.matrix = matrix
        rows = matrix.length + 1
        cols = matrix[0].length + 1
        bit = new int[rows][cols]
        for (int i = 0; i < rows - 1; ++i) {
            for (int j = 0; j < cols - 1; ++j) {
                int val = matrix[i][j]
                matrix[i][j] = 0
                update(i, j, val)
            }
        }
    }
    
    void update(int row, int col, int val) {
        int diff = val - this.matrix[row][col]
        this.matrix[row][col] = val
        for (int i = row+1; i < this.rows; i += (i & -i)) {
            for (int j = col+1; j < this.cols; j += (j & -j)) {
                this.bit[i][j] += diff
            }
        }
    }
    
    int sumRegion(int row1, int col1, int row2, int col2) {
        query(row1, col1) - query(row1, col2+1) - query(row2+1, col1) + query(row2+1, col2+1)
    }

    private int query(int r, int c) {
        int res = 0
        for (int i = r; i > 0; i -= (i & -i)) {
            for (int j = c; j > 0; j -= (j & -j)) {
                res += bit[i][j]
            }
        }
        res
    }
}

// def work = TestWork.create(new Solution().&add)
def work = TestWork.forStruct(NumMatrix)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
