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
class Solution:
    def matrixMultiply(self, a: List[List[float]], b: List[List[float]]) -> List[List[float]]:
        rows = len(a)
        cols = len(b[0])
        res = [[0.0] * cols for i in range(rows)]
        for i in range(rows):
            for j in range(cols):
                c = 0.0
                for k in range(len(b)):
                    c += a[i][k] * b[k][j]
                res[i][j] = c
        return res

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().matrixMultiply)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    # work.compareSerial = True
    work.run()

