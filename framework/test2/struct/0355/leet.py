#!/usr/bin/env python3
from collections import Counter, defaultdict, deque
import heapq
import json
import logging
import math
import random
import time
from typing import *
import sys

from soda.leetcode.bitree import *
from soda.leetcode.graph import *
from soda.leetcode.linklist import *
from soda.unittest.util import init_logging

logger = logging.getLogger(__name__)

# step [1]: implement class Solution
# class Solution: pass
class Tweet:
    def __init__(self, _id, ts):
        self.id = _id
        self.ts = ts

class Node:
    def __init__(self, tweets):
        self.tweets = tweets
        self.index = len(tweets) - 1
    def current(self):
        return self.tweets[self.index]
    def isEnd(self):
        return self.index == -1

class Twitter:

    def __init__(self):
        self.timestamp = 0
        self.tweets = defaultdict(deque)
        self.follows = defaultdict(set)
        self.Limit = 10

    def postTweet(self, userId: int, tweetId: int) -> None:
        self.tweets[userId].append(Tweet(tweetId, self.nextTimestamp()))

    def getNewsFeed(self, userId: int) -> List[int]:
        s = list(self.follows[userId])
        s.append(userId)
        pq = []
        for user in s:
            tq = self.tweets[user]
            if len(tq) > 0:
                node = Node(tq)
                heapq.heappush(pq, (-node.current().ts, node))
        res = []
        i = 0
        while i < self.Limit and len(pq) > 0:
            _, node = heapq.heappop(pq)
            res.append(node.current().id)
            node.index -= 1
            if not node.isEnd():
                heapq.heappush(pq, (-node.current().ts, node))
            i += 1
        return res

    def follow(self, followerId: int, followeeId: int) -> None:
        self.follows[followerId].add(followeeId)

    def unfollow(self, followerId: int, followeeId: int) -> None:
        self.follows[followerId].discard(followeeId)

    def nextTimestamp(self):
        self.timestamp += 1
        return self.timestamp

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    # work = TestWork(Solution().FUNCTION)
    # OR use struct tester
    work = TestWork.forStruct(Twitter)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

