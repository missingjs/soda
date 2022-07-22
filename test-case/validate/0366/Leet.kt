import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun findLeaves(root: TreeNode?): List<List<Int>> {
        val res = mutableListOf<MutableList<Int>>()
        for (i in 0 until 100) {
            res.add(mutableListOf<Int>())
        }
        val r = solve(root, res)
        return res.take(r)
    }

    private fun solve(root: TreeNode?, res: MutableList<MutableList<Int>>): Int {
        if (root == null) {
            return 0
        }
        val R = solve(root.right, res)
        val L = solve(root.left, res)
        val index = max(L, R)
        res[index] += root.`val`
        return index + 1
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::findLeaves)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.validator = Validators.forList2d<Int>(true, false)
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Leet()(Utils.fromStdin()))
}

