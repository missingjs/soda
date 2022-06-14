package soda.kotlin.leetcode

object TreeFactory {

    fun create(data: List<Int?>): TreeNode? {
        if (data.isEmpty() || data[0] == null) {
            return null
        }

        val root = TreeNode(data[0] ?: 0)
        val qu = java.util.ArrayDeque<TreeNode?>()
        qu.offer(root)
        var i = 1
        while (i < data.size) {
            val node = qu.pollFirst()!!
            data[i]?.let {
                node.left = TreeNode(it)
                qu.offer(node.left!!)
            }
            ++i
            if (i == data.size) {
                break
            }
            data[i]?.let {
                node.right = TreeNode(it)
                qu.offer(node.right!!)
            }
            ++i
        }
        return root
    }

    fun dump(root: TreeNode?): List<Int?> {
        if (root == null) {
            return emptyList()
        }

        var curr = mutableListOf<TreeNode?>()
        var next = mutableListOf<TreeNode?>()
        val order = mutableListOf<TreeNode?>()
        curr.add(root)
        while (curr.isNotEmpty()) {
            next.clear()
            for (node in curr) {
                order.add(node)
                node?.let {
                    next.add(node.left)
                    next.add(node.right)
                }
            }
            val temp = curr
            curr = next
            next = temp
        }

        var i = order.size - 1
        while (order[i] == null) {
            i -= 1
        }

        val data = mutableListOf<Int?>()
        for (j in 0..i) {
            data.add(order[j]?.`val`)
        }
        return data
    }

}