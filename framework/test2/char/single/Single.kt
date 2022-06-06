import soda.kotlin.unittest.*

class Solution {
    fun nextChar(ch: Char): Char = (ch + 1).toChar()
}

class Single : (String) -> String {
    override fun invoke(text: String): String {
        val work = GenericTestWork.create(Solution()::nextChar)
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Single()(Utils.fromStdin()))
}
