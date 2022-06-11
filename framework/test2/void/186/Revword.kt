import soda.kotlin.unittest.*

class Solution {
    fun reverseWords(s: CharArray): Unit {
        if (s.size == 0) {
            return
        }

        reverse(s, 0, s.size - 1)

        val N = s.size
        var i = 0
        var j = 0
        while (j < N) {
            if (s[j] == ' ') {
                reverse(s, i, j-1)
                i = j + 1
            }
            ++j
        }
        if (i < j) {
            reverse(s, i, j-1)
        }
    }

    private fun reverse(s: CharArray, ii: Int, jj: Int) {
        var i = ii
        var j = jj
        while (i < j) {
            val tmp = s[i]
            s[i] = s[j]
            s[j] = tmp
            ++i
            --j
        }
    }
}

class Revword : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        // val work = GenericTestWork.create(Solution()::add)

        // * by method that has not return value
        val work = GenericTestWork.createVoid(Solution()::reverseWords)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Revword()(Utils.fromStdin()))
}
