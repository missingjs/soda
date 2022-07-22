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
        val queue = java.util.PriorityQueue<Node>(compareBy {-it.diff})
        solve(root, target, k, queue)

        val res = mutableListOf<Int>()
        while (!queue.isEmpty()) {
            res += queue.poll().value
        }
        return res
    }

    fun solve(root: TreeNode?, target: Double, k: Int, queue: java.util.PriorityQueue<Node>) {
        if (root == null) {
            return
        }

        val node = Node.withTarget(root.`val`, target)
        if (queue.size == k) {
            if (node.diff < queue.peek().diff) {
                queue.poll()
                queue.offer(node)
            }
        } else {
            queue.offer(node)
        }

        solve(root.left, target, k, queue)
        solve(root.right, target, k, queue)
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

