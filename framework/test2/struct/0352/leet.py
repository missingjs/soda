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
class SummaryRanges:

    def __init__(self):
        self.parent = [0] * 10003
        self.ancestorSet = set()

    def addNum(self, val: int) -> None:
        val += 1
        if self.parent[val] != 0:
            return
        self.parent[val] = -1
        self.ancestorSet.add(val)
        left = val - 1
        right = val + 1
        if left > 0 and self.parent[left] != 0:
            self.merge(left, val)
        if self.parent[right] != 0:
            self.merge(val, right)

    def getIntervals(self) -> List[List[int]]:
        ans = list(self.ancestorSet)
        ans.sort()
        res = [None] * len(ans)
        for i in range(len(res)):
            start = ans[i]
            end = start - self.parent[start] - 1
            res[i] = [start-1, end-1]
        return res

    def merge(self, x, y):
        ax = self.getAncestor(x)
        ay = self.getAncestor(y)
        if ax < ay:
            self.mergeAncestor(ax, ay)
        else:
            self.mergeAncestor(ay, ax)

    def mergeAncestor(self, ax, ay):
        self.parent[ax] += self.parent[ay]
        self.parent[ay] = ax
        self.ancestorSet.remove(ay)

    def getAncestor(self, x):
        if self.parent[x] < 0:
            return x
        self.parent[x] = self.getAncestor(self.parent[x])
        return self.parent[x]

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    # work = TestWork(Solution().FUNCTION)
    # OR use struct tester
    work = TestWork.forStruct(SummaryRanges)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

