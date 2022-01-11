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
    def findLeaves(self, root: Optional[TreeNode]) -> List[List[int]]:
        res = [[] for i in range(100)]
        r = self.solve2(root, res)
        return res[:r]

    def solve2(self, root, res):
        if root is None:
            return 0
        R = self.solve2(root.right, res)
        L = self.solve2(root.left, res)
        index = max(L, R)
        res[index].append(root.val)
        return index + 1

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().findLeaves)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    work.validator = Validators.forList2d(True, False)
    work.compareSerial = True
    work.run()

