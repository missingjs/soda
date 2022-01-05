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
class Solution:
    def integerBreak(self, n: int) -> int:
        self.memo = [0] * 59
        return self.solve(n)

    def solve(self, n):
        if n == 1:
            return 1
        if self.memo[n] > 0:
            return self.memo[n]
        res = 0
        for i in range(1, n):
            res = max(i * (n-i), i * self.solve(n-i), res)
        self.memo[n] = res
        return res

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().integerBreak)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

