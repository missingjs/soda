import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import { TestWork, Utils } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {number[]} nums
 * @param {number} k
 * @param {number} t
 * @return {boolean}
 */
function containsNearbyAlmostDuplicate(nums: number[], k: number, t: number): boolean {
    let imap = new AvlTree<number, number>();
    let i = 0;
    let j = 0;
    while (j < nums.length) {
        if (j - i <= k) {
            let val = nums[j++];
            let lower = val - t;
            let upper = val + t;
            let node = imap.upperBound(lower);
            if (node != null && node.getKey() <= upper) {
                return true;
            }
            node = imap.find(val);
            let c = node ? node.getValue() : 0;
            imap.insert(val, c + 1);
        } else {
            let val = nums[i++];
            let c = imap.find(val).getValue() - 1;
            if (c == 0) {
                imap.remove(val);
            } else {
                imap.insert(val, c);
            }
        }
    }
    return false;
};

// step [2]: setup function/return/arguments
let taskFunc = containsNearbyAlmostDuplicate;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
