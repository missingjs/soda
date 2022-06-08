import soda.kotlin.unittest.*

class Solution {
    fun doubleList(chars: CharArray): List<Char> {
        val res = mutableListOf<Char>()
        for (ch in chars) {
            res.add(ch)
        }
        for (ch in chars) {
            res.add(ch)
        }
        return res
    }
}

class Chars1d : (String) -> String {
    override fun invoke(text: String): String {
        val work = GenericTestWork.create(Solution()::doubleList)

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
    print(Chars1d()(Utils.fromStdin()))
}
