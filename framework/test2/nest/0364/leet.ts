import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} from '@datastructures-js/priority-queue';
import { ListNode, NestedInteger } from 'soda/leetcode';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {NestedInteger[]} nestedList
 * @return {number}
 */
function depthSumInverse(nestedList: NestedInteger[]): number {
    let info = getInfo(nestedList, 1);
    return (info.maxDepth + 1) * info.sum - info.product;
}

class Info {
    constructor(
        public sum: number,
        public product: number,
        public maxDepth: number) {
    }
}

function getInfo(nestedList: NestedInteger[], depth: number): Info {
    let sum = 0;
    let product = 0;
    let maxDepth = depth;
    for (let ni of nestedList) {
        if (ni.isInteger()) {
            let val = ni.getInteger();
            sum += val;
            product += val * depth;
            maxDepth = Math.max(maxDepth, depth);
        } else {
            let res = getInfo(ni.getList(), depth+1);
            sum += res.sum;
            product += res.product;
            maxDepth = Math.max(maxDepth, res.maxDepth);
        }
    }
    return new Info(sum, product, maxDepth);
}

// step [2]: setup function/return/arguments
let taskFunc = depthSumInverse;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
