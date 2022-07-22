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
class Logger:
    def __init__(self):
        self.msgMap = {}
        self.Limit = 10
        self.lastTs = -self.Limit

    def shouldPrintMessage(self, timestamp: int, message: str) -> bool:
        T = self.lastTs
        self.lastTs = timestamp
        if timestamp - T >= self.Limit:
            self.msgMap = {}
            self.msgMap[message] = timestamp
            return True
        if timestamp - self.msgMap.get(message, timestamp - self.Limit) < self.Limit:
            return False
        self.msgMap[message] = timestamp
        return True

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    # work = TestWork(Solution().FUNCTION)
    # OR use struct tester
    work = TestWork.forStruct(Logger)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

