import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun lowestCommonAncestor(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode? {
        if (root == null) {
            return null
        }

        val stk = mutableListOf<TreeNode>()
        stk.add(root)

        var last = root
        var foundOne = false
        var index = -1

        if (root == p || root == q) {
            foundOne = true
            index = 0
        }

        while (!stk.isEmpty()) {
            val node = stk.last()
            if (node.left != null && last != node.left && last != node.right) {
                if (node.left == p || node.left == q) {
                    if (!foundOne) {
                        index = stk.size
                        foundOne = true
                    } else {
                        return stk[index]
                    }
                }
                stk.add(node.left!!)
            } else if (node.right != null && last != node.right) {
                if (node.right == p || node.right == q) {
                    if (!foundOne) {
                        index = stk.size
                        foundOne = true
                    } else {
                        return stk[index]
                    }
                }
                stk.add(node.right!!)
            } else {
                last = node
                if (index == stk.size - 1) {
                    --index
                }
                stk.removeLast()
            }
        }
        return null
    }
}

class Driver {
    fun exec(root: TreeNode?, p: Int, q: Int): Int {
        val pNode = findNode(root, p)
        val qNode = findNode(root, q)
        return Solution().lowestCommonAncestor(root, pNode, qNode)?.`val` ?: -1
    }

    fun findNode(root: TreeNode?, value: Int): TreeNode? {
        if (root == null) {
            return null
        }
        if (root.`val` == value) {
            return root
        }
        return findNode(root.left, value) ?: findNode(root.right, value)
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Driver()::exec)

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

