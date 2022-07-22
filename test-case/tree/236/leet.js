const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {TreeNode} root
 * @param {TreeNode} p
 * @param {TreeNode} q
 * @return {TreeNode}
 */
var lowestCommonAncestor = function(root, p, q) {
    let stk = [root];
    let last = root;
    let foundOne = false;
    let index = -1;

    if (root == p || root == q) {
        foundOne = true;
        index = 0;
    }

    while (stk.length > 0) {
        let node = stk[stk.length-1];
        if (node.left && last != node.left && last != node.right) {
            if (node.left == p || node.left == q) {
                if (!foundOne) {
                    index = stk.length;
                    foundOne = true;
                } else {
                    return stk[index];
                }
            }
            stk.push(node.left);
        } else if (node.right && last != node.right) {
            if (node.right == p || node.right == q) {
                if (!foundOne) {
                    index = stk.length;
                    foundOne = true;
                } else {
                    return stk[index];
                }
            }
            stk.push(node.right);
        } else {
            last = node;
            if (index == stk.length - 1) {
                --index;
            }
            stk.pop();
        }
    }
    return null;
};

/**
 * @param {TreeNode} root
 * @param {Integer} p
 * @param {Integer} q
 * @return {Integer}
 */
var driver = function(root, p, q) {
    let pNode = findNode(root, p);
    let qNode = findNode(root, q);
    return lowestCommonAncestor(root, pNode, qNode).val;
}

function findNode(root, val) {
    if (!root) {
        return null;
    }
    if (root.val == val) {
        return root;
    }
    return findNode(root.left, val) || findNode(root.right, val);
}

// step [2]: setup function/return/arguments
const work = TestWork.create(driver);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

