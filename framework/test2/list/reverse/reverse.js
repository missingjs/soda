const {ListNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {ListNode} head
 * @return {ListNode}
 */
var reverse = function(head) {
    let h = null;
    while (head) {
        let next = head.next;
        head.next = h;
        h = head;
        head = next;
    }
    return h;
};

const work = TestWork.create(reverse);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

