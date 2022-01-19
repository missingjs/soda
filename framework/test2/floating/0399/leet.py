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
    def calcEquation(self, equations: List[List[str]], values: List[float], queries: List[List[str]]) -> List[float]:
        indexMap = self.getIndexMap(equations)
        N = len(indexMap)
        table = [[-1.0] * N for i in range(N)]
        
        for k in range(len(values)):
            p = equations[k]
            i = indexMap[p[0]]
            j = indexMap[p[1]]
            table[i][j] = values[k]
            table[j][i] = 1.0 / values[k]

        res = [0.0] * len(queries)
        for i in range(len(res)):
            a = queries[i][0]
            b = queries[i][1]
            ai = indexMap.get(a)
            bi = indexMap.get(b)
            if ai is None or bi is None:
                res[i] = -1.0
                continue
            if ai == bi:
                res[i] = 1.0
                continue
            visited = [False] * N
            res[i] = self.dfs(ai, bi, table, visited)
        return res

    def getIndexMap(self, eqs):
        imap = {}
        for e in eqs:
            a, b = e
            if a not in imap:
                imap[a] = len(imap)
            if b not in imap:
                imap[b] = len(imap)
        return imap

    def dfs(self, ai, bi, table, visited):
        if table[ai][bi] >= 0.0:
            return table[ai][bi]

        visited[ai] = True
        res = -1.0
        for adj in range(len(table)):
            if table[ai][adj] >= 0.0 and not visited[adj]:
                v = self.dfs(adj, bi, table, visited)
                if v >= 0.0:
                    res = table[ai][adj] * v
                    break
        table[ai][bi] = res
        table[bi][ai] = 1.0 / res
        return res

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().calcEquation)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    # work.compareSerial = True
    work.run()

