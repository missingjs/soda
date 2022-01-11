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
    def permutation(self, chars: List[str], n: int) -> List[str]:
        res = []
        buf = [None] * n
        self.solve(chars, 0, buf, 0, res)
        return res

    def solve(self, chars, i, buf, j, res):
        if j == len(buf):
            res.append(''.join(buf))
            return
        for k in range(i, len(chars)):
            temp = chars[i]
            chars[i] = chars[k]
            chars[k] = temp
            buf[j] = chars[i]
            self.solve(chars, i+1, buf, j+1, res)
            temp = chars[i]
            chars[i] = chars[k]
            chars[k] = temp

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().permutation)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    work.validator = Validators.forList(False)
    work.compareSerial = True
    work.run()

