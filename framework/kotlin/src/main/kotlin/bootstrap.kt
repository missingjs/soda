import soda.kotlin.unittest.*

class Solution {
    fun add(a: Int, b: Int): Int {
        return a + b
    }
}

class __Bootstrap__ : (String) -> String {
    override fun invoke(text: String): String {
        val work = GenericTestWork.create(Solution()::add)

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
    print(__Bootstrap__()(Utils.fromStdin()))
}
