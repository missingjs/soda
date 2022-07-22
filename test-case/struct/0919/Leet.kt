import java.util.ArrayDeque
import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class CBTInserter(private val root: TreeNode?) {

    private val qu = ArrayDeque<TreeNode>()

    init {
        if (root != null) {
            qu.offerLast(root)
            while (!qu.isEmpty()) {
                val node = qu.peekFirst()
                if (node.left == null) {
                    break
                }
                qu.offerLast(node.left)
                if (node.right == null) {
                    break
                }
                qu.offerLast(node.right)
                qu.pollFirst()
            }
        }
    }

    fun insert(`val`: Int): Int {
        val node = TreeNode(`val`)
        val head = qu.peekFirst()
        qu.offerLast(node)
        if (head.left == null) {
            head.left = node
        } else {
            head.right = node
            qu.pollFirst()
        }
        return head.`val`
    }

    fun get_root(): TreeNode? {
        return root
    }

}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        // val work = GenericTestWork.create(Solution()::add)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        val work = GenericTestWork.forStruct(CBTInserter::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Leet()(Utils.fromStdin()))
}

