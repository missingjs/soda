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
class VirIndex:
    def __init__(self, nums):
        self.nums = nums

    def __getitem__(self, index):
        return self.nums[self.mapIndex(index)]

    def __setitem__(self, index, value):
        self.nums[self.mapIndex(index)] = value

    def mapIndex(self, i):
        N = len(self.nums)
        if (N&1) == 1 or i > ((N-1) >> 1):
            return (((N-i) << 1) - 1) % N
        else:
            return N - 2 - (i << 1)

class Solution:
    def wiggleSort(self, nums: List[int]) -> None:
        """
        Do not return anything, modify nums in-place instead.
        """
        vi = VirIndex(nums)
        self.quickSelect(vi, 0, len(nums)-1, (len(nums)-1)//2)

    def quickSelect(self, vi, start, end, k):
        while start < end:
            p = self.partition(vi, start, end)
            if k >= p[0] and k <= p[1]:
                return
            if k > p[1]:
                start = p[1] + 1
            else:
                end = p[0] - 1

    def partition(self, vi, start, end):
        mid = (start + end) // 2
        pivot = self.getMedian(vi[start], vi[mid], vi[end])
        p = start
        z = end + 1
        q = start
        while q < z:
            if vi[q] < pivot:
                vi[p], vi[q] = vi[q], vi[p]
                p += 1
                q += 1
            elif vi[q] == pivot:
                q += 1
            else:
                z -= 1
                vi[z], vi[q] = vi[q], vi[z]
        return [p, z-1]

    def getMedian(self, a, b, c):
        if a >= b:
            return b if b >= c else min(a, c)
        else:
            return a if a >= c else min(b, c)

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().wiggleSort)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    def validate(e, nums):
        for i in range(1, len(nums)):
            if i % 2 != 0 and nums[i] <= nums[i-1] or i % 2 == 0 and nums[i] >= nums[i-1]:
                return False
        return True
    work.validator = validate
    work.compareSerial = True
    print(work.run(sys.stdin.read()))

