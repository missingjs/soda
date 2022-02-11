const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {number[]} nums1
 * @param {number[]} nums2
 * @return {number[]}
 */
var intersect = function(nums1, nums2) {
    if (nums1.length > nums2.length) {
        return intersect(nums2, nums1);
    }
    let mset = new Set();
    let res = new Set();
    for (let n of nums1) {
        mset.add(n);
    }
    for (let b of nums2) {
        if (mset.has(b)) {
            res.add(b);
        }
    }
    return Array.from(res);
};

const work = TestWork.create(intersect);
work.validator = Validators.forArray('number', false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

