const {
    AvlTree,
    BinarySearchTree
} = require('@datastructures-js/binary-search-tree');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');
const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

// step [1]: implement solution function
/**
 * @param {number[]} nums
 * @param {number} k
 * @param {number} t
 * @return {boolean}
 */
var containsNearbyAlmostDuplicate = function(nums, k, t) {
    var imap = new AvlTree();
    var i = 0;
    var j = 0;
    while (j < nums.length) {
        if (j - i <= k) {
            var val = nums[j++];
            var lower = val - t;
            var upper = val + t;
            var node = imap.upperBound(lower);
            if (node != null && node.getKey() <= upper) {
                return true;
            }
            node = imap.find(val);
            var c = node ? node.getValue() : 0;
            imap.insert(val, c + 1);
        } else {
            var val = nums[i++];
            var c = imap.find(val).getValue() - 1;
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
const work = TestWork.create(containsNearbyAlmostDuplicate);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

