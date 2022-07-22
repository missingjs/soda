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
class Solution:
    def lowestCommonAncestor(self, root: 'TreeNode', p: 'TreeNode', q: 'TreeNode') -> 'TreeNode':
        stk = [root]
        last = root
        found_one = False
        index = -1
        
        if root == p or root == q:
            found_one = True
            index = 0

        while stk:
            node = stk[-1]
            if node.left and last != node.left and last != node.right:
                if node.left == p or node.left == q:
                    if not found_one:
                        index = len(stk)
                        found_one = True
                    else:
                        return stk[index]
                stk.append(node.left)
            elif node.right and last != node.right:
                if node.right == p or node.right == q:
                    if not found_one:
                        index = len(stk)
                        found_one = True
                    else:
                        return stk[index]
                stk.append(node.right)
            else:
                last = node
                if index == len(stk) - 1:
                    index -= 1
                stk.pop()

def drive(root: 'TreeNode', p: int, q: int) -> int:
    pNode = findNode(root, p)
    qNode = findNode(root, q)
    return Solution().lowestCommonAncestor(root, pNode, qNode).val

def findNode(root, val):
    if not root:
        return
    if root.val == val:
        return root
    return findNode(root.left, val) or findNode(root.right, val)

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(drive)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    print(work.run(sys.stdin.read()))

