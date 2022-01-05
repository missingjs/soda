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
class NumMatrix:

    def __init__(self, matrix: List[List[int]]):
        self.rows = len(matrix)+1
        self.cols = len(matrix[0])+1
        self.mx = [[0] * len(matrix[0]) for i in range(len(matrix))]
        self.bit = [[0] * self.cols for i in range(self.rows)]
        for i in range(self.rows-1):
            for j in range(self.cols-1):
                self.update(i, j, matrix[i][j])

    def update(self, row: int, col: int, val: int) -> None:
        diff = val - self.mx[row][col]
        self.mx[row][col] = val
        i = row + 1
        while i < self.rows:
            j = col + 1
            while j < self.cols:
                self.bit[i][j] += diff
                j += (j & -j)
            i += (i & -i)

    def sumRegion(self, row1: int, col1: int, row2: int, col2: int) -> int:
        return self.query(row1, col1) - self.query(row1, col2+1) - self.query(row2+1, col1) + self.query(row2+1, col2+1)

    def query(self, r, c):
        res = 0
        i = r
        while i > 0:
            j = c
            while j > 0:
                res += self.bit[i][j]
                j -= (j & -j)
            i -= (i & -i)
        return res
        

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    # work = TestWork(Solution().FUNCTION)
    # OR use struct tester
    work = TestWork.forStruct(NumMatrix)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

