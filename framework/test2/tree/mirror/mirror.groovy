import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    public TreeNode mirror(TreeNode root) {
        if (!root) {
            return null
        }
        mirror(root.left)
        mirror(root.right)
        def temp = root.left
        root.left = root.right
        root.right = temp
        root
    }
}

def work = TestWork.create(new Solution().&mirror)
// def work = TestWork.forStruct(STRUCT)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
