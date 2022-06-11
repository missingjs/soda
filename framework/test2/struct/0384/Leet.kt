import soda.kotlin.unittest.*
import kotlinx.serialization.json.*

class Solution(nums: IntArray) {

    private val original = nums

    fun reset(): IntArray {
        return original.copyOf()
    }

    fun shuffle(): IntArray {
        val res = reset()
        val rand = java.util.Random()
        for (s in res.size downTo 1) {
            val i = rand.nextInt(s)
            val j = s - 1
            if (i != j) {
                val temp = res[i]
                res[i] = res[j]
                res[j] = temp
            }
        }
        return res
    }

}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // val work = GenericTestWork.create(Solution()::add)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        val work = GenericTestWork.forStruct(Solution::class)


        // * setup validator
        work.validator = fun(expect: JsonElement, result: JsonElement): Boolean {
            @Suppress("UNCHECKED_CAST")
            val commands = work.arguments[0] as List<String>
            val listCmp = Validators.forList<Int>(false)
            for (i in commands.indices) {
                if (commands[i] == "shuffle") {
                    val evalues = Json.decodeFromJsonElement<List<Int>>(expect.jsonArray[i])
                    val rvalues = Json.decodeFromJsonElement<List<Int>>(result.jsonArray[i])
                    if (!listCmp(evalues, rvalues)) {
                        return false
                    }
                }
            }
            return true
        }
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Leet()(Utils.fromStdin()))
}
