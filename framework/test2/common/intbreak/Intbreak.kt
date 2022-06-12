import soda.kotlin.unittest.*

class Solution {
    private var memo: IntArray = IntArray(0)

    fun integerBreak(n: Int): Int {
        memo = IntArray(59) {0}
        return solve(n)
    }

    private fun solve(n: Int): Int {
        if (n == 1) return 1
        if (memo[n] > 0) return memo[n]
        var res = 0
        for (i in 1 until n) {
            res = maxOf(res, (i * (n-i)))
            res = maxOf(res, (i * solve(n-i)))
        }
        memo[n] = res
        return res
    }
}

class Intbreak : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::integerBreak)

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
    print(Intbreak()(Utils.fromStdin()))
}
