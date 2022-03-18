import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} from '@datastructures-js/priority-queue';
import { ListNode } from 'soda/leetcode';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {ListNode[]} lists
 * @return {ListNode[]}
 */
function reverseAll(lists: ListNode[]): ListNode[] {
    for (let i = 0; i < lists.length; ++i) {
        lists[i] = reverse(lists[i]);
    }
    let [i, j] = [0, lists.length-1];
    while (i < j) {
        [lists[i], lists[j]] = [lists[j], lists[i]];
        ++i;
        --j;
    }
    return lists;
}

function reverse(head: ListNode): ListNode {
    let h = null;
    while (head) {
        let next = head.next;
        head.next = h;
        h = head;
        head = next;
    }
    return h;
}

// step [2]: setup function/return/arguments
let taskFunc = reverseAll;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
