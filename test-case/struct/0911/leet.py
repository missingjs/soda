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
class TopVotedCandidate:

    def __init__(self, persons: List[int], times: List[int]):
        self.N = len(persons)
        self.times = times
        self.winner = [0] * self.N
        counter = [0] * (self.N + 1)
        win = 0
        for i in range(self.N):
            counter[persons[i]] += 1
            if counter[persons[i]] >= counter[win]:
                win = persons[i]
            self.winner[i] = win

    def q(self, t: int) -> int:
        if t >= self.times[-1]:
            return self.winner[self.N-1]
        low, high = 0, self.N-1
        while low < high:
            mid = (low + high) // 2
            if t <= self.times[mid]:
                high = mid
            else:
                low = mid + 1
        return self.winner[low] if t == self.times[low] else self.winner[low-1]

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    # work = TestWork(Solution().FUNCTION)
    # OR use struct tester
    work = TestWork.forStruct(TopVotedCandidate)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

