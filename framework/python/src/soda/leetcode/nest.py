from typing import *

"""
This is the interface that allows for creating nested lists.
You should not implement it, or speculate about its implementation
"""
class NestedInteger:
    def __init__(self, value=None):
        """
        If value is not specified, initializes an empty list.
        Otherwise initializes a single integer equal to value.
        """
        self.isAtomic = value is not None
        if value is None:
            self.elements = []
        else:
            self.value = value

    def isInteger(self):
        """
        @return True if this NestedInteger holds a single integer, rather than a nested list.
        :rtype bool
        """
        return self.isAtomic

    def add(self, elem):
        """
        Set this NestedInteger to hold a nested list and adds a nested integer elem to it.
        :rtype void
        """
        if not self.isInteger():
            self.elements.append(elem)

    def setInteger(self, value):
        """
        Set this NestedInteger to hold a single integer equal to value.
        :rtype void
        """
        if self.isInteger():
            self.value = value

    def getInteger(self):
        """
        @return the single integer that this NestedInteger holds, if it holds a single integer
        Return None if this NestedInteger holds a nested list
        :rtype int
        """
        return self.value if self.isInteger() else None

    def getList(self):
        """
        @return the nested list that this NestedInteger holds, if it holds a nested list
        Return None if this NestedInteger holds a single integer
        :rtype List[NestedInteger]
        """
        return list(self.elements) if not self.isInteger() else []

def parseNestedIntegers(data: List) -> List[NestedInteger]:
    def parseNI(d):
        if isinstance(d, int):
            return NestedInteger(d)
        ni = NestedInteger()
        for sub in d:
            ni.add(parseNI(sub))
        return ni
    return list(map(parseNI, data))

def serializeNestedIntegers(nestedList: List[NestedInteger]) -> List:
    def serializeNI(ni):
        if ni.isInteger():
            return ni.getInteger()
        return list(map(serializeNI, ni.getList()))
    return list(map(serializeNI, nestedList))

