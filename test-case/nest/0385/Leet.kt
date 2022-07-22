import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {

    private var p = 0

    fun deserialize(s: String): NestedInteger {
        p = 0
        return parse(s)
    }

    private fun parse(s: String): NestedInteger {
        if (s[p] == '[') {
            ++p
            val root = NestedInteger()
            while (s[p] != ']') {
                root.add(parse(s))
                if (s[p] == ',') {
                    ++p
                }
            }
            ++p
            return root
        }

        var negative = false
        if (s[p] == '-') {
            ++p
            negative = true
        }

        var value = 0
        while (p < s.length && s[p] >= '0' && s[p] <= '9') {
            value = value * 10 + s[p].code - '0'.code
            ++p
        }

        if (negative) {
            value = -value
        }
        return NestedInteger(value)
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::deserialize)

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
    print(Leet()(Utils.fromStdin()))
}

