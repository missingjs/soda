import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Node(val diff: Double, val value: Int) {
    companion object {
        fun withTarget(value: Int, target: Double): Node {
            return Node(abs(target - value), value)
        }
    }
}

class Solution {
    fun closestKValues(root: TreeNode?, target: Double, k: Int): List<Int> {
        val nodes = mutableListOf<Node>()
        collect(root, nodes, target)
        return nodes.sortedBy{it.diff}.slice(0 until k).map {it.value}
    }

    fun collect(root: TreeNode?, nodes: MutableList<Node>, target: Double) {
        if (root == null) {
            return
        }
        nodes += Node.withTarget(root.`val`, target)
        collect(root.left, nodes, target)
        collect(root.right, nodes, target)
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::closestKValues)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.validator = Validators.forList<Int>(false)
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Leet()(Utils.fromStdin()))
}

