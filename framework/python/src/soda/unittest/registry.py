import sys
from typing import *

from soda.leetcode.bitree import TreeNode, TreeFactory
from soda.leetcode.linklist import ListNode, ListFactory
from soda.leetcode.nest import *

from .convert import ObjectConverter

converterFactoryMap = {}

defaultConverter = ObjectConverter(lambda x: x, lambda y: y)

def register(typeHint, funcFromSerial, funcToSerial):
    converterFactoryMap[typeHint] = lambda: ObjectConverter(funcFromSerial, funcToSerial)

def registerFactory(typeHint, factoryFunc):
    converterFactoryMap[typeHint] = factoryFunc

def getConverter(typeHint):
    fact = converterFactoryMap.get(typeHint)
    if fact:
        return fact()
    return defaultConverter

register(ListNode, ListFactory.create, ListFactory.dump)
register(Optional[ListNode], ListFactory.create, ListFactory.dump)
register(TreeNode, TreeFactory.create, TreeFactory.dump)
register(Optional[TreeNode], TreeFactory.create, TreeFactory.dump)
register(NestedInteger, NestedIntegerFactory.parse, NestedIntegerFactory.serialize)
register(List[NestedInteger], parseNestedIntegers, serializeNestedIntegers)
register(List[ListNode], lambda L: list(map(ListFactory.create, L)), lambda L: list(map(ListFactory.dump, L)))
registerFactory(List[Optional[ListNode]], lambda: getConverter(List[ListNode]))
