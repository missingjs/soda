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
    def reorderList(self, head: Optional[ListNode]) -> None:
        """
        Do not return anything, modify head in-place instead.
        """
        fast = slow = head
        while fast.next and fast.next.next:
            slow = slow.next
            fast = fast.next.next
        
        if slow == fast:
            return

        r = self.reverse(slow.next)
        slow.next = None
        self.merge(head, r)

    def reverse(self, head):
        q = None
        while head:
            Next = head.next
            head.next = q
            q = head
            head = Next
        return q

    def merge(self, L1, L2):
        t = h = ListNode()
        while L1 and L2:
            t.next = L1
            t = L1
            L1 = L1.next
            t.next = L2
            t = L2
            L2 = L2.next
        t.next = L1 or L2 

if __name__ == '__main__':
    init_logging()
    from soda.unittest import Validators
    from soda.unittest.work import TestWork

    # step [2]: setup function
    # Attention! FUNCTION must use type hint, including arguments and return type
    work = TestWork(Solution().reorderList)
    # OR use struct tester
    # work = TestWork.forStruct(CLASS)

    # step [3]: setup other options
    # work.validator = (e,r) => bool
    work.compareSerial = True
    print(work.run(sys.stdin.read()))

