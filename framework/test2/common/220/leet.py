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

from sortedcontainers import SortedDict
# step [1]: implement class Solution
class Solution:
    def containsNearbyAlmostDuplicate(self, nums: List[int], k: int, t: int) -> bool:
        tmap = SortedDict()
        i = j = 0
        while j < len(nums):
            if (j - i <= k):
                val = nums[j]
                j += 1
                lower = val - t
                upper = val + t
                index = tmap.bisect_left(lower)
                if index < len(tmap) and tmap.keys()[index] <= upper:
                    return True
                tmap[val] = tmap.get(val, 0) + 1
            else:
                val = nums[i]
                i += 1
                tmap[val] -= 1
                if tmap[val] == 0:
                    tmap.pop(val)
        return False

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().containsNearbyAlmostDuplicate)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    print(work.run(sys.stdin.read()))

