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
function lowestCommonAncestor(root: TreeNode | null, p: TreeNode | null, q: TreeNode | null): TreeNode | null {
    let stk = [root];
    let last = root;
    let foundOne = false;
    let index = -1;

    if (root == p || root == q) {
        foundOne = true;
        index = 0;
    }

    while (stk.length > 0) {
        let node = stk[stk.length-1];
        if (node.left && last != node.left && last != node.right) {
            if (node.left == p || node.left == q) {
                if (!foundOne) {
                    index = stk.length;
                    foundOne = true;
                } else {
                    return stk[index];
                }
            }
            stk.push(node.left);
        } else if (node.right && last != node.right) {
            if (node.right == p || node.right == q) {
                if (!foundOne) {
                    index = stk.length;
                    foundOne = true;
                } else {
                    return stk[index];
                }
            }
            stk.push(node.right);
        } else {
            last = node;
            if (index == stk.length - 1) {
                --index;
            }
            stk.pop();
        }
    }
    return null;
}

/**
 * @param {TreeNode} root
 * @param {number} p
 * @param {number} q
 * @return {number}
 */
function driver(root: TreeNode, p: number, q: number): number {
    let pNode = findNode(root, p);
    let qNode = findNode(root, q);
    return lowestCommonAncestor(root, pNode, qNode)!.val;
}

function findNode(root: TreeNode | null, val: number): TreeNode | null {
    if (!root) {
        return null;
    }
    if (root.val == val) {
        return root;
    }
    return findNode(root.left, val) || findNode(root.right, val);
}

// step [2]: setup function/return/arguments
let taskFunc = driver;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
