const {ListNode, NestedInteger} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {NestedInteger[]} niList
 * @return {number[]}
 */
var flatNested = function(niList) {
    let res = [];
    let iter = new NestedIterator(niList);
    while (iter.hasNext()) {
        res.push(iter.next());
    }
    return res;
};

class Node {
    constructor(niList) {
        this.niList = niList;
        this.index = 0;
    }
    isEnd() {
        return this.index >= this.niList.length;
    }
    value() {
        return this.current().getInteger();
    }
    current() {
        return this.niList[this.index];
    }
}

/**
 * @constructor
 * @param {NestedInteger[]} nestedList
 */
var NestedIterator = function(nestedList) {
    this.stk = [new Node(nestedList)];
    this.locate();
};

NestedIterator.prototype.locate = function() {
    while (this.stk.length > 0) {
        if (this.stk.at(-1).isEnd()) {
            this.stk.pop();
            if (this.stk.length > 0) {
                ++this.stk.at(-1).index;
            }
        } else if (this.stk.at(-1).current().isInteger()) {
            break;
        } else {
            this.stk.push(new Node(this.stk.at(-1).current().getList()));
        }
    }
};


/**
 * @this NestedIterator
 * @returns {boolean}
 */
NestedIterator.prototype.hasNext = function() {
    return this.stk.length > 0 && !this.stk.at(-1).isEnd();
};

/**
 * @this NestedIterator
 * @returns {integer}
 */
NestedIterator.prototype.next = function() {
    let value = this.stk.at(-1).value();
    ++this.stk.at(-1).index;
    this.locate();
    return value;
};

const work = TestWork.create(flatNested);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

