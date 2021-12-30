from typing import *

from soda.leetcode.bitree import TreeNode, TreeFactory
from soda.leetcode.linklist import ListNode, ListFactory
from soda.leetcode.nest import NestedInteger, parseNestedIntegers, serializeNestedIntegers

from .convert import ObjectConverter

converterFactoryMap = {}

defaultConverter = ObjectConverter(lambda x: x, lambda y: y)

def register(typeHint, funcFromSerial, funcToSerial):
    converterFactoryMap[typeHint] = ObjectConverter(funcFromSerial, funcToSerial)

def getConverter(typeHint):
    return converterFactoryMap.get(typeHint, defaultConverter)

register(ListNode, ListFactory.create, ListFactory.dump)
register(TreeNode, TreeFactory.create, TreeFactory.dump)
register(List[NestedInteger], parseNestedIntegers, serializeNestedIntegers)

