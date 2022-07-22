import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun groupByLength(strs: Array<String>): Array<Array<String>> {
        val group = mutableMapOf<Int, MutableList<String>>()
        strs.shuffle()
        for (s in strs) {
            group.getOrPut(s.length, { mutableListOf<String>() }) += s
        }
        val keys = group.keys.toTypedArray()
        keys.shuffle()
        return keys.map { group[it]!!.toTypedArray() }.toTypedArray()
    }
}

class List2d : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::groupByLength)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.validator = Validators.forArray2d<String>(false, false)
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(List2d()(Utils.fromStdin()))
}

