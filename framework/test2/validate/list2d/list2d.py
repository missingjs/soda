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
    def groupByLength(self, strs: List[str]) -> List[List[str]]:
        random.shuffle(strs)
        group = defaultdict(list)
        for s in strs:
            group[len(s)].append(s)
        keys = list(group.keys())
        random.shuffle(keys)
        return list(map(lambda x: group[x], keys))

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().groupByLength)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    work.validator = Validators.forList2d(str, False, False)
    work.compareSerial = True
    work.run()

