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
from soda.leetcode.nest import NestedInteger
from soda.unittest.util import init_logging

logger = logging.getLogger(__name__)

# step [1]: implement class Solution
class Solution:
    def flatNested(self, niList: List[NestedInteger]) -> List[int]:
        res = []
        itr = NestedIterator(niList)
        while itr.hasNext():
            res.append(itr.next())
        return res

class Node:
    def __init__(self, niList):
        self.niList = niList
        self.index = 0

    def isEnd(self):
        return self.index >= len(self.niList)

    def value(self):
        return self.current().getInteger()

    def current(self):
        return self.niList[self.index]

class NestedIterator:
    def __init__(self, nestedList: [NestedInteger]):
        self.stk = [Node(nestedList)]
        self.locate()

    def locate(self):
        while self.stk:
            if self.stk[-1].isEnd():
                self.stk.pop()
                if self.stk:
                    self.stk[-1].index += 1
            elif self.stk[-1].current().isInteger():
                break
            else:
                self.stk.append(Node(self.stk[-1].current().getList()))
    
    def next(self) -> int:
        value = self.stk[-1].value()
        self.stk[-1].index += 1
        self.locate()
        return value
    
    def hasNext(self) -> bool:
        return self.stk and not self.stk[-1].isEnd()

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().flatNested)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

