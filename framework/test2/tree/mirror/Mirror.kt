import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun mirror(root: TreeNode?): TreeNode? {
        if (root == null) {
            return null
        }
        mirror(root.left)
        mirror(root.right)
        val temp = root.left
        root.left = root.right
        root.right = temp
        return root
    }
}

class Mirror : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::mirror)

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
    print(Mirror()(Utils.fromStdin()))
}
