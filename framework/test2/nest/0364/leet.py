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
from soda.leetcode.nest import *
from soda.unittest.util import init_logging

logger = logging.getLogger(__name__)

# step [1]: implement class Solution
# class Solution: pass
class Info:
    def __init__(self, s, p, m):
        self.sum = s
        self.product = p
        self.maxDepth = m

class Solution:
    def depthSumInverse(self, nestedList: List[NestedInteger]) -> int:
        info = self.getInfo(nestedList, 1)
        return (info.maxDepth + 1) * info.sum - info.product

    def getInfo(self, nestedList, depth):
        _sum = 0
        product = 0
        maxDepth = depth
        for ni in nestedList:
            if ni.isInteger():
                val = ni.getInteger()
                _sum += val
                product += val * depth
                maxDepth = max(maxDepth, depth)
            else:
                res = self.getInfo(ni.getList(), depth+1)
                _sum += res.sum
                product += res.product
                maxDepth = max(maxDepth, res.maxDepth)
        return Info(_sum, product, maxDepth)

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().depthSumInverse)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

