const {
    AvlTree,
    BinarySearchTree
} = require('@datastructures-js/binary-search-tree');
const {
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');
const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

// step [1]: implement solution function
class Node {
    constructor(d, v) {
        this.diff = d;
        this.value = v;
    }

    static withTarget(value, target) {
        return new Node(Math.abs(target - value), value);
    }
}

/**
 * @param {TreeNode} root
 * @param {number} target
 * @param {number} k
 * @return {number[]}
 */
var closestKValues = function(root, target, k) {
    var queue = new MaxPriorityQueue({ priority: e => e.diff });
    solve(root, target, k, queue);
    var res = [];
    while (!queue.isEmpty()) {
        res.push(queue.dequeue().element.value);
    }
    return res;
};

function solve(root, target, k, queue) {
    if (!root) {
        return;
    }

    var node = Node.withTarget(root.val, target);
    if (queue.size() == k) {
        if (node.diff < queue.front().element.diff) {
            queue.dequeue();
            queue.enqueue(node);
        }
    } else {
        queue.enqueue(node);
    }

    solve(root.left, target, k, queue);
    solve(root.right, target, k, queue);
}

// step [2]: setup function/return/arguments
const work = TestWork.create(closestKValues);
// work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray('number', false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

