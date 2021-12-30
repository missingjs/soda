import json
import sys
import time
from typing import get_type_hints

from .registry import getConverter

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
        arguments = tuple(self.parse(v,t) for v,t in zip(testInput.args, self.argumentTypes))

        startTime = time.time()
        result = self.function(*arguments)
        endTime = time.time()
        elapseMillis = (endTime - startTime) * 1000

        resultSerial = self.serialize(result, self.returnType)

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
                expect = self.parse(testInput.expected, self.returnType)
                success = self.validator(expect, result)

        output['success'] = success
        print(json.dumps(output))

    def parse(self, serialValue, typeHint):
        return getConverter(typeHint).fromJsonSerializable(serialValue)

    def serialize(self, obj, typeHint):
        return getConverter(typeHint).toJsonSerializable(obj)

