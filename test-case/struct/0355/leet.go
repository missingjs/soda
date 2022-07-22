package main

import (
    "container/heap"

    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

type Tweet struct {
    id int
    ts int
}

type Node struct {
    tweets map[int][]Tweet
    key    int
    index  int
}

func newNode(t map[int][]Tweet, key int) *Node {
    return &Node {
        tweets: t,
        key: key,
        index: len(t[key]) - 1,
    }
}

func (n *Node) current() Tweet {
    return n.tweets[n.key][n.index]
}

func (n *Node) isEnd() bool {
    return n.index == -1
}

type Twitter struct {
    timestamp int
    tweets map[int][]Tweet
    follows map[int]map[int]bool
}

var Limit = 10

func Constructor() Twitter {
    return Twitter {
        timestamp: 0,
        tweets: make(map[int][]Tweet),
        follows: make(map[int]map[int]bool),
    }
}


func (this *Twitter) PostTweet(userId int, tweetId int)  {
    this.tweets[userId] = append(this.tweets[userId], Tweet{tweetId, this.nextTimestamp()})
}

func (this *Twitter) nextTimestamp() int {
    this.timestamp++
    return this.timestamp
}


func (this *Twitter) GetNewsFeed(userId int) []int {
    pq := make(NodePQ, 0)
    var s []int
    if _, ok := this.follows[userId]; ok {
        for k := range this.follows[userId] {
            s = append(s, k)
        }
    }
    s = append(s, userId)

    for _, user := range s {
        if len(this.tweets[user]) > 0 {
            heap.Push(&pq, newNode(this.tweets, user))
        }
    }

    res := make([]int, 0)
    for i := 0; i < Limit && len(pq) > 0; i++ {
        node := heap.Pop(&pq).(*Node)
        res = append(res, node.current().id)
        node.index--
        if !node.isEnd() {
            heap.Push(&pq, node)
        }
    }
    return res
}


func (this *Twitter) Follow(followerId int, followeeId int)  {
    if _, ok := this.follows[followerId]; !ok {
        this.follows[followerId] = make(map[int]bool)
    }
    this.follows[followerId][followeeId] = true
}


func (this *Twitter) Unfollow(followerId int, followeeId int)  {
    if _, ok := this.follows[followerId]; ok {
        if _, ok2 := this.follows[followerId][followeeId]; ok2 {
            delete(this.follows[followerId], followeeId)
        }
    }
}

type NodePQ []*Node

func (pq NodePQ) Len() int { return len(pq) }

func (pq NodePQ) Less(i, j int) bool {
    return pq[i].current().ts > pq[j].current().ts
}

func (pq NodePQ) Swap(i, j int) {
    pq[i], pq[j] = pq[j], pq[i]
}

func (pq *NodePQ) Push(x interface{}) {
    *pq = append(*pq, x.(*Node))
}

func (pq *NodePQ) Pop() interface{} {
    a := *pq
    n := len(a)
    item := a[n-1]
    *pq = a[:n-1]
    return item
}

func main() {
    // create tester by function
    // work := unittest.CreateWork(FUNCTION)
    // OR create by struct factory
    work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
