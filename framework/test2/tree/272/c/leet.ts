import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue,
    PriorityQueueItem
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
    let nodes = [];
    collect(root, nodes, target);
    quickSelect(nodes, 0, nodes.length-1, k);
    return nodes.slice(0, k).map(x => x.value);
}

function quickSelect(nodes: Node[], start: number, end: number, index: number) {
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

function partition(nodes: Node[], start: number, end: number, pivot: number): number {
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

function placeMedian3(nodes: Node[], start: number, mid: number, end: number) {
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

function swap(arr: Node[], i: number, j: number) {
    [arr[i], arr[j]] = [arr[j], arr[i]];
}

function collect(root: TreeNode, nodes: Node[], target: number) {
    if (!root) {
        return;
    }
    nodes.push(Node.withTarget(root.val, target));
    collect(root.left, nodes, target);
    collect(root.right, nodes, target);
}

// step [2]: setup function/return/arguments
let taskFunc = closestKValues;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray<number>('number', false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
