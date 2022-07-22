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
    let nodes = [];
    collect(root, nodes, target);
    nodes.sort((x, y) => x.diff - y.diff);
    return nodes.slice(0, k).map(x => x.value);
};

function collect(root, nodes, target) {
    if (!root) {
        return;
    }
    nodes.push(Node.withTarget(root.val, target));
    collect(root.left, nodes, target);
    collect(root.right, nodes, target);
}

// step [2]: setup function/return/arguments
const work = TestWork.create(closestKValues);
// work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray('number', false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

