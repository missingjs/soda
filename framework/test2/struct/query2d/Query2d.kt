import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class NumMatrix(private val matrix: Array<IntArray>) {

    private val bit: Array<IntArray>
    private val rows: Int
    private val cols: Int

    init {
        rows = matrix.size + 1
        cols = matrix[0].size + 1
        bit = Array<IntArray>(rows) { IntArray(cols) }
        for (i in 0 until rows-1) {
            for (j in 0 until cols-1) {
                val value = matrix[i][j]
                matrix[i][j] = 0
                update(i, j, value)
            }
        }
    }

    fun update(row: Int, col: Int, value: Int) {
        val diff = value - matrix[row][col]
        matrix[row][col] = value
        var i = row + 1
        while (i < rows) {
            var j = col + 1
            while (j < cols) {
                bit[i][j] += diff
                j += (j and -j)
            }
            i += (i and -i)
        }
    }

    fun sumRegion(row1: Int, col1: Int, row2: Int, col2: Int): Int {
        return (query(row1, col1)
            - query(row1, col2 + 1)
            - query(row2 + 1, col1)
            + query(row2 + 1, col2 + 1))
    }

    fun query(r: Int, c: Int): Int {
        var res = 0
        var i = r
        while (i > 0) {
            var j = c
            while (j > 0) {
                res += bit[i][j]
                j -= (j and -j)
            }
            i -= (i and -i)
        }
        return res
    }

}

class Query2d : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        // val work = GenericTestWork.create(Solution()::add)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        val work = GenericTestWork.forStruct(NumMatrix::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Query2d()(Utils.fromStdin()))
}

