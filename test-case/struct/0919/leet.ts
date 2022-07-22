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
class CBTInserter {
    private qu: TreeNode[];
    private root: TreeNode | null;

    /**
     * @param {TreeNode} root
     */
    constructor(root: TreeNode | null) {
        let qu = [];
        this.qu = qu;
        this.root = root;
        if (!root) {
            return;
        }
        qu.push(root);
        while (qu.length > 0) {
            let node = qu[0];
            if (!node.left) {
                break;
            }
            qu.push(node.left);
            if (!node.right) {
                break;
            }
            qu.push(node.right);
            qu.shift();
        }
    }

    /** 
     * @param {number} val
     * @return {number}
     */
    insert(val: number): number {
        let node = new TreeNode(val);
        let head = this.qu[0];
        this.qu.push(node);
        if (!head.left) {
            head.left = node;
        } else {
            head.right = node;
            this.qu.shift();
        }
        return head.val;
    }

    /**
     * @return {TreeNode}
     */
    get_root(): TreeNode | null {
        return this.root;
    }
}

// step [2]: setup function/return/arguments
// let taskFunc = add;
// const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
let work = TestWork.forStruct(CBTInserter);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
