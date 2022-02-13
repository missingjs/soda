const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {TreeNode} root
 * @return {TreeNode}
 */
var mirror = function(root) {
    if (!root) {
        return null;
    }
    mirror(root.left);
    mirror(root.right);
    [root.left, root.right] = [root.right, root.left];
    return root;
};

const work = TestWork.create(mirror);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

