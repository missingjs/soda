const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

var Logger = function() {
    this.msgMap = new Map();
    this.Limit = 10;
    this.lastTs = -this.Limit;
};

/** 
 * @param {number} timestamp 
 * @param {string} message
 * @return {boolean}
 */
Logger.prototype.shouldPrintMessage = function(timestamp, message) {
    let T = this.lastTs;
    this.lastTs = timestamp;
    if (timestamp - T >= this.Limit) {
        this.msgMap = new Map();
        this.msgMap.set(message, timestamp);
        return true;
    }
    ;
    if (timestamp - getOrDefault(this.msgMap, message, timestamp - this.Limit) < this.Limit) {
        return false;
    }
    this.msgMap.set(message, timestamp);
    return true;
};

function getOrDefault(map, key, defVal) {
    let v = map.get(key);
    return v !== undefined ? v : defVal;
}

// const work = TestWork.create(add);
work = TestWork.forStruct(Logger);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

