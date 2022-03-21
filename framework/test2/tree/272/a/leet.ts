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
class Node {
    constructor(public diff: number, public value: number) {
    }

    static withTarget(value: number, target: number): Node {
        return new Node(Math.abs(target - value), value);
    }
}

/**
 * @param {TreeNode} root
 * @param {number} target
 * @param {number} k
 * @return {number[]}
 */
function closestKValues(root: TreeNode | null, target: number, k: number): number[] {
    let queue = new MaxPriorityQueue<Node>({ priority: e => e.diff });
    solve(root, target, k, queue);
    let res = [];
    while (!queue.isEmpty()) {
        res.push(queue.dequeue()['element'].value);
    }
    return res;
}

function solve(root: TreeNode, target: number, k: number, queue: MaxPriorityQueue<Node>) {
    if (!root) {
        return;
    }

    let node = Node.withTarget(root.val, target);
    if (queue.size() == k) {
        if (node.diff < queue.front()['element'].diff) {
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
let taskFunc = closestKValues;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray<number>('number', false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
