import json
import sys
import time
from typing import *

from soda.leetcode.linklist import ListNode, ListFactory
from soda.leetcode.bitree import TreeNode, TreeFactory

class TestInput:

    def __init__(self, obj):
        self.obj = obj

    @property
    def expected(self):
        return self.obj['expected']

    @property
    def id(self):
        return self.obj['id']

    @property
    def args(self):
        return self.obj['args']

dataConv = {
    ListNode: {
        'p': ListFactory.create,
        's': ListFactory.dump
    },
    TreeNode: {
        'p': TreeFactory.create,
        's': TreeFactory.dump
    },
    '*': {
        'p': lambda x: x,
        's': lambda x: x
    }
}

class TestWork:

    def __init__(self, function):
        self.function = function
        hints = list(get_type_hints(function).values())
        self.argumentTypes = hints[:-1]
        self.returnType = hints[-1]
        self.compareSerial = False

        self.argumentParsers = [None] * len(self.argumentTypes)
        self.resultSerializer = None
        self.resultParser = None
        self.default_validator = lambda x, y: x == y
        self.validator = self.default_validator

    @classmethod
    def forStruct(cls, structClass):
        from .structure import structTester
        return cls(structTester(structClass))

    def setArgumentParser(self, index, parser):
        self.argumentParsers[index] = parser

    def run(self):
        testInput = TestInput(json.load(sys.stdin))
        arguments = tuple(self.fromSerial(v,t,p) for v,t,p in zip(
                    testInput.args, self.argumentTypes, self.argumentParsers))

        startTime = time.time()
        result = self.function(*arguments)
        endTime = time.time()
        elapseMillis = (endTime - startTime) * 1000

        resultSerial = self.toSerial(result, self.returnType, self.resultSerializer)

        output = {
            'id': testInput.id,
            'result': resultSerial,
            'elapse': elapseMillis
        }

        success = True
        if testInput.expected is not None:
            if self.validator == self.default_validator and self.compareSerial:
                success = (testInput.expected == resultSerial)
            else:
                expect = self.fromSerial(testInput.expected, self.returnType, self.resultParser)
                success = self.validator(expect, result)

        output['success'] = success
        print(json.dumps(output))

    def toSerial(self, workValue, workType, serializer):
        if serializer is None:
            serializer = dataConv.get(workType, dataConv['*'])['s']
        return serializer(workValue)

    def fromSerial(self, jsonValue, workType, parser):
        if parser is None:
            parser = dataConv.get(workType, dataConv['*'])['p']
        return parser(jsonValue)

