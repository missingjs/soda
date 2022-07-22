import soda.kotlin.unittest.*

class Solution {
    fun reverseVowels(s: String): String {
        val isv = MutableList(128) {false}
        val vs = "aeiouAEIOU"
        for (i in vs.indices) {
            isv[vs[i].code] = true
        }
        val buf = s.toCharArray()
        var i = 0
        var j = s.length - 1
        while (i < j) {
            while (i < j && !isv[buf[i].code]) {
                ++i
            }
            while (i < j && !isv[buf[j].code]) {
                --j
            }
            if (i < j) {
                val temp = buf[i]
                buf[i] = buf[j]
                buf[j] = temp
                ++i
                --j
            }
        }
        return String(buf)
    }
}

class Reverse : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::reverseVowels)

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
    print(Reverse()(Utils.fromStdin()))
}
