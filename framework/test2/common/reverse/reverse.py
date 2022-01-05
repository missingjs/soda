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
class Solution:
    def reverseVowels(self, s: str) -> str:
        isv = [False] * 128
        for ch in "aeiouAEIOU":
            isv[ord(ch)] = True
        buf = list(s)
        i, j = 0, len(s) - 1
        while i < j:
            while i < j and not isv[ord(buf[i])]:
                i += 1
            while i < j and not isv[ord(buf[j])]:
                j -= 1
            if i < j:
                temp = buf[i]
                buf[i] = buf[j]
                buf[j] = temp
                i += 1
                j -= 1
        return ''.join(buf)

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().reverseVowels)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

