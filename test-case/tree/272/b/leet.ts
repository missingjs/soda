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
    let nodes = [];
    collect(root, nodes, target);
    nodes.sort((x, y) => x.diff - y.diff);
    return nodes.slice(0, k).map(x => x.value);
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
