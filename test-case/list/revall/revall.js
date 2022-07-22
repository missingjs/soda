const {ListNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {ListNode[]} lists
 * @return {ListNode[]}
 */
var reverseAll = function(lists) {
    for (let i = 0; i < lists.length; ++i) {
        lists[i] = reverse(lists[i]);
    }
    let [i, j] = [0, lists.length-1];
    while (i < j) {
        [lists[i], lists[j]] = [lists[j], lists[i]];
        ++i;
        --j;
    }
    return lists;
};

function reverse(head) {
    let h = null;
    while (head) {
        let next = head.next;
        head.next = h;
        h = head;
        head = next;
    }
    return h;
}

const work = TestWork.create(reverseAll);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

