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
        quickSelect(nodes, 0, nodes.size - 1, k)
        return nodes.slice(0 until k).map {it.value}
    }

    private fun quickSelect(nodes: MutableList<Node>, _start: Int, _end: Int, index: Int) {
        var (start, end) = listOf(_start, _end)
        while (start < end) {
            val mid = (start + end) / 2
            placeMedian3(nodes, start, mid, end)
            val k = partition(nodes, start, end, mid)
            if (k == index) {
                return
            } else if (k > index) {
                end = k - 1
            } else {
                start = k + 1
            }
        }
    }

    private fun partition(nodes: MutableList<Node>, start: Int, end: Int, pivot: Int): Int {
        val d = nodes[pivot].diff
        swap(nodes, pivot, end)
        var p = start
        for (i in start..end) {
            if (nodes[i].diff < d) {
                if (p != i) {
                    swap(nodes, p, i)
                }
                ++p
            }
        }
        swap(nodes, p, end)
        return p
    }

    private fun placeMedian3(nodes: MutableList<Node>, start: Int, mid: Int, end: Int) {
        if (nodes[start].diff > nodes[mid].diff) {
            swap(nodes, start, mid)
        }
        if (nodes[start].diff > nodes[end].diff) {
            swap(nodes, start, end)
        }
        if (nodes[mid].diff > nodes[end].diff) {
            swap(nodes, mid, end)
        }
    }

    private fun swap(nodes: MutableList<Node>, i: Int, j: Int) {
        val temp = nodes[i]
        nodes[i] = nodes[j]
        nodes[j] = temp
    }

    private fun collect(root: TreeNode?, nodes: MutableList<Node>, target: Double) {
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

