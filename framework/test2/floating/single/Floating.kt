import soda.kotlin.unittest.*

class Solution {
    fun divide(a: Double, b: Double): Double {
        return a / b
    }
}

class Floating : (String) -> String {
    override fun invoke(text: String): String {
        val work = GenericTestWork.create(Solution()::divide)

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
    print(Floating()(Utils.fromStdin()))
}
