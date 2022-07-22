import soda.kotlin.unittest.*

class Solution {
    fun toUpper(matrix: List<List<Char>>): Array<CharArray> {
        val diff = 'a' - 'A'
        val mx = Array(matrix.size) { CharArray(matrix[0].size) }
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                mx[i][j] = (matrix[i][j].code.toInt() - diff).toChar()
            }
        }
        return mx
    }
}

class Chars2d : (String) -> String {
    override fun invoke(text: String): String {
        val work = GenericTestWork.create(Solution()::toUpper)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Chars2d()(Utils.fromStdin()))
}
