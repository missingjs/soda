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
 * @param {TreeNode} root
 * @return {TreeNode}
 */
function mirror(root: TreeNode | null): TreeNode | null {
    if (!root) {
        return null;
    }
    mirror(root.left);
    mirror(root.right);
    [root.left, root.right] = [root.right, root.left];
    return root;
}

// step [2]: setup function/return/arguments
let taskFunc = mirror;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
