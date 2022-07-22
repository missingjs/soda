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
from heapq import heappush, heappop
class Node:
    def __init__(self, d, v):
        self.diff = d
        self.value = v

    @classmethod
    def withTarget(cls, value, target):
        return Node(abs(target - value), value)

    def __lt__(self, other):
        return self.diff > other.diff

class Solution:
    def closestKValues(self, root: Optional[TreeNode], target: float, k: int) -> List[int]:
        que = []
        self.solve(root, target, k, que)

        res = []
        while que:
            res.append(heappop(que).value)
        return res

    def solve(self, root, target, k, que):
        if not root:
            return

        node = Node.withTarget(root.val, target)
        if len(que) == k:
            if node.diff < que[0].diff:
                heappop(que)
                heappush(que, node)
        else:
            heappush(que, node)

        self.solve(root.left, target, k, que)
        self.solve(root.right, target, k, que)

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().closestKValues)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    work.validator = Validators.forList(int, False)
    work.compareSerial = True
    print(work.run(sys.stdin.read()))

