import soda.kotlin.unittest.*

class Solution {
    fun multiply(a: DoubleArray, b: DoubleArray): List<Double> {
        val res = MutableList(a.size) {0.0}
        for (i in res.indices) {
            res[i] = a[i] * b[i]
        }
        return res
    }
}

class Numeric : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::multiply)

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
    print(Numeric()(Utils.fromStdin()))
}
