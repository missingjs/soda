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
        nodes = []
        self.collect(root, nodes, target)
        self.quickSelect(nodes, 0, len(nodes)-1, k)
        return list(map(lambda n: n.value, nodes[:k]))

    def quickSelect(self, nodes, start, end, index):
        while start < end:
            mid = (start + end) // 2
            self.placeMedian3(nodes, start, mid, end)
            k = self.partition(nodes, start, end, mid)
            if k == index:
                return
            elif k > index:
                end = k - 1
            else:
                start = k + 1

    def partition(self, nodes, start, end, pivot):
        d = nodes[pivot].diff
        self.swap(nodes, pivot, end)
        p = start
        for i in range(start, end+1):
            if nodes[i].diff < d:
                if p != i:
                    self.swap(nodes, p, i)
                p += 1
        self.swap(nodes, p, end)
        return p

    def placeMedian3(self, nodes, start, mid, end):
        if nodes[start].diff > nodes[mid].diff:
            self.swap(nodes, start, mid)
        if nodes[start].diff > nodes[end].diff:
            self.swap(nodes, start, end)
        if nodes[mid].diff > nodes[end].diff:
            self.swap(nodes, mid, end)

    def collect(self, root, nodes, target):
        if not root:
            return
        nodes.append(Node.withTarget(root.val, target))
        self.collect(root.left, nodes, target)
        self.collect(root.right, nodes, target)

    def swap(self, arr, i, j):
        arr[i], arr[j] = arr[j], arr[i]

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

