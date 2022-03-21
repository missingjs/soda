import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} from '@datastructures-js/priority-queue';
import { ListNode, NestedInteger } from 'soda/leetcode';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
class Tweet {
    constructor(public id: number, public ts: number) {}
}

class Node {

    public index: number;

    constructor(public tweets: Tweet[]) {
        this.index = tweets.length - 1;
    }

    current(): Tweet {
        return this.tweets[this.index];
    }

    isEnd(): boolean {
        return this.index == -1;
    }
}

class Twitter {
    private timestamp: number = 0;
    private tweets: Map<number, Tweet[]>;
    private follows: Map<number, Set<number>>;
    private readonly Limit: number = 10;

    constructor() {
        this.tweets = new Map<number, Tweet[]>();
        this.follows = new Map<number, Set<number>>();
    }

    /** 
     * @param {number} userId 
     * @param {number} tweetId
     * @return {void}
     */
    postTweet(userId: number, tweetId: number): void {
        if (!this.tweets.has(userId)) {
            this.tweets.set(userId, []);
        }
        let tt = new Tweet(tweetId, this.nextTimestamp());
        this.tweets.get(userId)!.push(tt);
    }

    /** 
     * @param {number} userId
     * @return {number[]}
     */
    getNewsFeed(userId: number): number[] {
        let s = [...(this.follows.get(userId) || [])];
        s.push(userId);
        let pq = new MaxPriorityQueue<Node>({
            priority: (e: Node) => e.current().ts 
        });
        for (let user of s) {
            let tq = this.tweets.get(user) || [];
            if (tq.length > 0) {
                pq.enqueue(new Node(tq));
            }
        }
        let res: number[] = [];
        let i = 0;
        while (i < this.Limit && !pq.isEmpty()) {
            let node = pq.dequeue()['element'];
            res.push(node.current().id);
            --node.index;
            if (!node.isEnd()) {
                pq.enqueue(node);
            }
            ++i;
        }
        return res;
    }

    /** 
     * @param {number} followerId 
     * @param {number} followeeId
     * @return {void}
     */
    follow(followerId: number, followeeId: number): void {
        if (!this.follows.has(followerId)) {
            this.follows.set(followerId, new Set<number>());
        }
        this.follows.get(followerId)!.add(followeeId);
    }

    /** 
     * @param {number} followerId 
     * @param {number} followeeId
     * @return {void}
     */
    unfollow(followerId: number, followeeId: number): void {
        if (this.follows.has(followerId)) {
            this.follows.get(followerId)!.delete(followeeId);
        }
    }

    nextTimestamp(): number {
        return ++this.timestamp;
    }
}

// step [2]: setup function/return/arguments
// let taskFunc = add;
// const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
let work = TestWork.forStruct(Twitter);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
