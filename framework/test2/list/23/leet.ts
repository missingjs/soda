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
 * @return {ListNode}
 */
function mergeKLists(lists: ListNode[]): ListNode {
    let qu = new MinPriorityQueue<ListNode>({ priority: n => n.val });
    for (let L of lists) {
        if (L) {
            qu.enqueue(L);
        }
    }

    let head = new ListNode;
    let tail = head;
    while (!qu.isEmpty()) {
        let t = qu.dequeue().element;
        let L = t.next;
        if (L) {
            qu.enqueue(L);
        }
        tail.next = t;
        tail = t;
    }
    return head.next;
}

// step [2]: setup function/return/arguments
let taskFunc = mergeKLists;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
