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

    def __init__(self, nums: List[int]):
        self.original = nums[:]

    def reset(self) -> List[int]:
        return self.original[:]

    def shuffle(self) -> List[int]:
        res = self.reset()
        for s in range(len(res), 0, -1):
            i = random.randrange(0, s)
            j = s - 1
            if i != j:
                res[i], res[j] = res[j], res[i]
        return res

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    # work = TestWork(Solution().FUNCTION)
    # OR use struct tester
    work = TestWork.forStruct(Solution)

    def validate(expect, result) -> bool:
        arguments = work.arguments
        commands = arguments[0]
        for i in range(1, len(commands)):
            cmd = commands[i]
            if cmd == 'shuffle':
                evalues = expect[i]
                rvalues = result[i]
                counts = defaultdict(int)
                for a in evalues:
                    counts[a] += 1
                for b in rvalues:
                    c = counts[b] - 1
                    if c < 0:
                        return False
        return True
    # step [3]: setup other options
    work.validator = validate
    work.compareSerial = True
    work.run()

