import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun permutation(chars: CharArray, n: Int): Array<String> {
        val res = mutableListOf<String>()
        val buf = CharArray(n) { ' ' }
        solve(chars, 0, buf, 0, res)
        return res.toTypedArray()
    }

    private fun solve(chars: CharArray, i: Int, buf: CharArray, j: Int, res: MutableList<String>) {
        if (j == buf.size) {
            res += String(buf)
            return
        }
        for (k in i until chars.size) {
            var temp = chars[i]
            chars[i] = chars[k]
            chars[k] = temp
            buf[j] = chars[i]
            solve(chars, i+1, buf, j+1, res)
            temp = chars[i]
            chars[i] = chars[k]
            chars[k] = temp
        }
    }
}

class Permute : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::permutation)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.validator = Validators.forArray<String>(false)
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Permute()(Utils.fromStdin()))
}

