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
 */
var CBTInserter = function(root) {
    let qu = [];
    this.qu = qu;
    this.root = root;
    if (!root) {
        return;
    }
    qu.push(root);
    while (qu.length > 0) {
        let node = qu[0];
        if (!node.left) {
            break;
        }
        qu.push(node.left);
        if (!node.right) {
            break;
        }
        qu.push(node.right);
        qu.shift();
    }
};

/** 
 * @param {number} val
 * @return {number}
 */
CBTInserter.prototype.insert = function(val) {
    let node = new TreeNode(val);
    let head = this.qu[0];
    this.qu.push(node);
    if (!head.left) {
        head.left = node;
    } else {
        head.right = node;
        this.qu.shift();
    }
    return head.val;
};

/**
 * @return {TreeNode}
 */
CBTInserter.prototype.get_root = function() {
    return this.root;
};

// step [2]: setup function/return/arguments
// const work = TestWork.create(add);
work = TestWork.forStruct(CBTInserter);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

