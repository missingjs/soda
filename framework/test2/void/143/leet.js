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
/**
 * @param {ListNode} head
 * @return {void} Do not return anything, modify head in-place instead.
 */
var reorderList = function(head) {
    let fast = head, slow = head;
    while (fast.next && fast.next.next) {
        slow = slow.next;
        fast = fast.next.next;
    }

    if (slow == fast) {
        return;
    }

    let r = reverse(slow.next);
    slow.next = null;
    merge(head, r);
}

function reverse(head) {
    let q = null;
    while (head) {
        let next = head.next;
        head.next = q;
        q = head;
        head = next;
    }
    return q;
}

function merge(L1, L2) {
    let t = new ListNode;
    while (L1 && L2) {
        t.next = L1;
        t = L1;
        L1 = L1.next;
        t.next = L2;
        t = L2;
        L2 = L2.next;
    }
    t.next = L1 || L2;
}

// step [2]: setup function/return/arguments
const work = TestWork.create(reorderList);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

