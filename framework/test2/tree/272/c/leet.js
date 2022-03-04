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
    quickSelect(nodes, 0, nodes.length-1, k);
    return nodes.slice(0, k).map(x => x.value);
};

function quickSelect(nodes, start, end, index) {
    while (start < end) {
        let mid = Math.floor((start + end) / 2);
        placeMedian3(nodes, start, end, mid);
        let k = partition(nodes, start, end, mid);
        if (k === index) {
            return;
        } else if (k > index) {
            end = k - 1;
        } else {
            start = k + 1;
        }
    }
}

function partition(nodes, start, end, pivot) {
    let d = nodes[pivot].diff;
    swap(nodes, pivot, end);
    let p = start;
    for (let i = start; i <= end; ++i) {
        if (nodes[i].diff < d) {
            if (p != i) {
                swap(nodes, p, i);
            }
            ++p;
        }
    }
    swap(nodes, p, end);
    return p;
}

function placeMedian3(nodes, start, mid, end) {
    if (nodes[start].diff > nodes[mid].diff) {
        swap(nodes, start, mid);
    }
    if (nodes[start].diff > nodes[end].diff) {
        swap(nodes, start, end);
    }
    if (nodes[mid].diff > nodes[end].diff) {
        swap(nodes, mid, end);
    }
}

function swap(arr, i, j) {
    [arr[i], arr[j]] = [arr[j], arr[i]];
}

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

