from collections import defaultdict
from typing import *

from .feature import ObjectFeature
from .unordered import UnorderedListFeature

class ListFeature(ObjectFeature):

    def __init__(self, elemFeat: ObjectFeature):
        self.elemFeat = elemFeat

    def hash(self, obj: List[Any]) -> int:
        res = 0
        # keep low 48 bits
        mask = 0xffffffffffff
        for e in obj:
            h = self.elemFeat.hash(e)
            res = res * 133 + h
            res &= mask
        return res

    def isEqual(self, a: List[Any], b: List[Any]) -> bool:
        if len(a) != len(b):
            return False
        for i in range(len(a)):
            if not self.elemFeat.isEqual(a[i], b[i]):
                return False
        return True

class FloatFeature(ObjectFeature):

    def hash(self, val: float) -> int:
        return hash(val)

    def isEqual(self, a: float, b: float) -> bool:
        return abs(a - b) < 1e-6

class FeatureFactory:

    factoryMap = defaultdict(lambda: ObjectFeature)

    @classmethod
    def create(cls, objType: type) -> ObjectFeature:
        return cls.factoryMap[objType]()

    @classmethod
    def register(cls):
        cls.factoryMap[float] = FloatFeature
        cls.factoryMap[List[float]] = lambda : ListFeature(cls.create(float))
        cls.factoryMap[List[List[float]]] = lambda : ListFeature(cls.create(List[float]))

FeatureFactory.register()
