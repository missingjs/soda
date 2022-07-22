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
class CBTInserter:

    def __init__(self, root: Optional[TreeNode]):
        qu = deque()
        self.qu = qu
        self.root = root
        if root is None:
            return
        qu.append(root)
        while qu:
            node = qu[0]
            if not node.left:
                break
            qu.append(node.left)
            if not node.right:
                break
            qu.append(node.right)
            qu.popleft()

    def insert(self, val: int) -> int:
        node = TreeNode(val)
        head = self.qu[0]
        self.qu.append(node)
        if not head.left:
            head.left = node
        else:
            head.right = node
            self.qu.popleft()
        return head.val

    def get_root(self) -> Optional[TreeNode]:
        return self.root
        

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    # work = TestWork(Solution().FUNCTION)
    # OR use struct tester
    work = TestWork.forStruct(CBTInserter)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    work.run()

