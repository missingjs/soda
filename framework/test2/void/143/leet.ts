import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} from '@datastructures-js/priority-queue';
import { ListNode, NestedInteger, TreeNode } from 'soda/leetcode';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {ListNode} head
 * @return {void} Do not return anything, modify head in-place instead.
 */
function reorderList(head: ListNode | null): void {
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

function reverse(head: ListNode | null): ListNode | null {
    let q = null;
    while (head) {
        let next = head.next;
        head.next = q;
        q = head;
        head = next;
    }
    return q;
}

function merge(L1: ListNode | null, L2: ListNode | null) {
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
let taskFunc = reorderList;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
