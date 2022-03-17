import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {number[]} nums1
 * @param {number[]} nums2
 * @return {number[]}
 */
function intersect(nums1: number[], nums2: number[]): number[] {
    if (nums1.length > nums2.length) {
        return intersect(nums2, nums1);
    }
    let mset = new Set<number>();
    let res = new Set<number>();
    for (let n of nums1) {
        mset.add(n);
    }
    for (let b of nums2) {
        if (mset.has(b)) {
            res.add(b);
        }
    }
    return Array.from<number>(res);
}

// step [2]: setup function/return/arguments
let taskFunc = intersect;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray<number>('number', false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
