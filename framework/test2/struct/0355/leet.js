const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
  PriorityQueue,
  MinPriorityQueue,
  MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

function Tweet(id, ts) {
    this.id = id;
    this.ts = ts;
}

function Node(tweets) {
    this.tweets = tweets;
    this.index = tweets.length - 1;
}

Node.prototype.current = function() {
    return this.tweets[this.index];
};

Node.prototype.isEnd = function() {
    return this.index == -1;
};

var Twitter = function() {
    this.timestamp = 0;
    this.tweets = new Map();
    this.follows = new Map();
    this.Limit = 10;
};

/** 
 * @param {number} userId 
 * @param {number} tweetId
 * @return {void}
 */
Twitter.prototype.postTweet = function(userId, tweetId) {
    if (!this.tweets.has(userId)) {
        this.tweets.set(userId, []);
    }
    this.tweets.get(userId).push(new Tweet(tweetId, this.nextTimestamp()));
};

/** 
 * @param {number} userId
 * @return {number[]}
 */
Twitter.prototype.getNewsFeed = function(userId) {
    let s = [...(this.follows.get(userId) || [])];
    s.push(userId);
    let pq = new MaxPriorityQueue({ priority: e => e.current().ts });
    for (let user of s) {
        let tq = this.tweets.get(user) || [];
        if (tq.length > 0) {
            pq.enqueue(new Node(tq));
        }
    }
    let res = [];
    let i = 0;
    while (i < this.Limit && !pq.isEmpty()) {
        let node = pq.dequeue().element;
        res.push(node.current().id);
        --node.index;
        if (!node.isEnd()) {
            pq.enqueue(node);
        }
        ++i;
    }
    return res;
};

/** 
 * @param {number} followerId 
 * @param {number} followeeId
 * @return {void}
 */
Twitter.prototype.follow = function(followerId, followeeId) {
    if (!this.follows.has(followerId)) {
        this.follows.set(followerId, new Set());
    }
    this.follows.get(followerId).add(followeeId);
};

/** 
 * @param {number} followerId 
 * @param {number} followeeId
 * @return {void}
 */
Twitter.prototype.unfollow = function(followerId, followeeId) {
    if (this.follows.has(followerId)) {
        this.follows.get(followerId).delete(followeeId);
    }
};

Twitter.prototype.nextTimestamp = function() {
    return ++this.timestamp;
};

// const work = TestWork.create(add);
work = TestWork.forStruct(Twitter);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

