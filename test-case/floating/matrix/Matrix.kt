import soda.kotlin.unittest.*

class Solution {
    fun matrixMultiply(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
        val rows = a.size
        val cols = b[0].size
        val res = Array(rows) { DoubleArray(cols) }
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                var c = 0.0
                for (k in b.indices) {
                    c += a[i][k] * b[k][j]
                }
                res[i][j] = c
            }
        }
        return res
    }
}

class Matrix : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::matrixMultiply)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        // work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Matrix()(Utils.fromStdin()))
}
