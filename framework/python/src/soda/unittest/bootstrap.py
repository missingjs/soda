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

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().FUNCTION)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    # work.setArgumentParser(index, (a) => b)
    # work.resultSerializer = (r) => s
    # work.resultParser = (s) => r
    work.run()

