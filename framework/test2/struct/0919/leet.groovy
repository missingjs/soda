import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class CBTInserter {

    private def qu = new ArrayDeque<>()
    private TreeNode root

    CBTInserter(TreeNode root) {
        this.root = root
        qu.offerLast(root)
        while (!qu.isEmpty()) {
            var node = qu.peekFirst()
            if (!node.left) {
                break
            }
            qu.offerLast(node.left)
            if (!node.right) {
                break
            }
            qu.offerLast(node.right)
            qu.pollFirst()
        }
    }
    
    int insert(int val) {
        def node = new TreeNode(val)
        def head = qu.peekFirst()
        qu.offerLast(node)
        if (!head.left) {
            head.left = node
        } else {
            head.right = node
            qu.pollFirst()
        }
        head.val
    }
    
    TreeNode get_root() {
        root
    }
}

class LeetWork {
    String call(String input) {
        // def work = TestWork.create(new Solution().&add)
        def work = TestWork.forStruct(CBTInserter)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new LeetWork()(System.in.getText('UTF-8'))
