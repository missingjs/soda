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
    def deserialize(self, s: str) -> NestedInteger:
        self.p = 0
        return self.parse(s)

    def parse(self, s) -> NestedInteger:
        if s[self.p] == '[':
            self.p += 1
            root = NestedInteger()
            while s[self.p] != ']':
                root.add(self.parse(s))
                if s[self.p] == ',':
                    self.p += 1
            self.p += 1
            return root

        negative = False
        if s[self.p] == '-':
            self.p += 1
            negative = True

        value = 0
        while self.p < len(s) and s[self.p] >= '0' and s[self.p] <= '9':
            value = value * 10 + ord(s[self.p]) - ord('0')
            self.p += 1

        if negative:
            value = 0 - value
        return NestedInteger(value)

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().deserialize)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

