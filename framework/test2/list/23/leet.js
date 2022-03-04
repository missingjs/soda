const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {ListNode[]} lists
 * @return {ListNode}
 */
var mergeKLists = function(lists) {
    var qu = new MinPriorityQueue({ priority: n => n.val });
    for (var L of lists) {
        if (L) {
            qu.enqueue(L);
        }
    }

    var head = new ListNode;
    var tail = head;
    while (!qu.isEmpty()) {
        var t = qu.dequeue().element;
        var L = t.next;
        if (L) {
            qu.enqueue(L);
        }
        tail.next = t;
        tail = t;
    }
    return head.next;
};

// step [2]: setup function/return/arguments
const work = TestWork.create(mergeKLists);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

