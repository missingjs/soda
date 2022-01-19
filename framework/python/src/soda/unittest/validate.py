from collections import defaultdict
from abc import ABC, abstractmethod
from typing import *

class ObjectFeature(ABC):

    @abstractmethod
    def hash(self, obj: Any) -> int:
        pass

    @abstractmethod
    def isEqual(self, a: Any, b: Any) -> bool:
        pass

class XEntry:
    def __init__(self, key: int, value: Any):
        self.key = key
        self.value = value

class XMap:

    def __init__(self, feature: ObjectFeature):
        self.feat = feature
        self.dict = defaultdict(list)
        self.size = 0

    def _hash(self, key: Any) -> int:
        return self.feat.hash(key)

    def __contains__(self, key: Any) -> bool:
        e = self._entry(key)
        return e is not None

    def get(self, key: Any, default: Any = None) -> Any:
        e = self._entry(key)
        return e.value if e is not None else default

    def __getitem__(self, key: Any) -> Any:
        e = self._entry(key)
        if e is None:
            raise KeyError(key)
        return e.value

    def _entry(self, key: Any) -> XEntry:
        h = self._hash(key)
        if h not in self.dict:
            return
        for entry in self.dict[h]:
            if self.feat.isEqual(key, entry.key):
                return entry

    def __setitem__(self, key: Any, val: Any):
        e = self._entry(key)
        if e is not None:
            e.value = val
        else:
            h = self._hash(key)
            self.dict[h].append(XEntry(key, val))
            self.size += 1

    def __delitem__(self, key: Any):
        h = self._hash(key)
        if h not in self.dict:
            return
        e = None
        for entry in self.dict[h]:
            if self.feat.isEqual(key, entry.key):
                e = entry
                break
        if e is not None:
            self.dict[h].remove(e)
            self.size -= 1

class StrategyFactory:

    @classmethod
    def unorderList(cls, feat: ObjectFeature):
        def comp(a: List[Any], b: List[Any]) -> bool:
            if len(a) != len(b):
                return False
            xmap = XMap(feat)
            for e in a:
                xmap[e] = xmap.get(e, 0) + 1
            for e in b:
                if e not in xmap:
                    return False
                xmap[e] -= 1
                if xmap[e] == 0:
                    del xmap[e]
            return True
        return comp

    @classmethod
    def list(cls, feat: ObjectFeature):
        def comp(a: List[Any], b: List[Any]) -> bool:
            if len(a) != len(b):
                return False
            for i in range(len(a)):
                if not feat.isEqual(a[i], b[i]):
                    return False
            return True
        return comp

class GenericFeature(ObjectFeature):

    def hash(self, obj: Any) -> int:
        return hash(obj)

    def isEqual(self, a: Any, b: Any) -> bool:
        return a == b

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
        return StrategyFactory.list(self.elemFeat)(a, b)

class UnorderListFeature(ObjectFeature):

    def __init__(self, elemFeat: ObjectFeature):
        self.elemFeat = elemFeat

    def hash(self, obj: List[Any]) -> int:
        res = 0
        # keep low 48 bits
        mask = 0xffffffffffff
        for h in sorted(map(lambda x: self.elemFeat.hash(x), obj)):
            res = res * 133 + h
            res &= mask
        return res

    def isEqual(self, a: List[Any], b: List[Any]) -> bool:
        return StrategyFactory.unorderList(self.elemFeat)(a, b)

class FloatFeature(ObjectFeature):

    def hash(self, val: float) -> int:
        return hash(val)

    def isEqual(self, a: float, b: float) -> bool:
        return abs(a - b) < 1e-6

class Validators:

    @classmethod
    def forList(cls, objType: type, ordered: bool) -> Callable[[Any,Any],bool]:
        f = StrategyFactory
        d = cls._createFeature(objType)
        return f.list(d) if ordered else f.unorderList(d)

    @classmethod
    def forList2d(cls, objType: type, dim1Ordered: bool, dim2Ordered: bool) -> Callable[[Any,Any],bool]:
        f = StrategyFactory
        elemFeat = cls._createFeature(objType)
        d = ListFeature(elemFeat) if dim2Ordered else UnorderListFeature(elemFeat)
        return f.list(d) if dim1Ordered else f.unorderList(d)

    @classmethod
    def _createFeature(cls, objType: type) -> ObjectFeature:
        return FeatureFactory.create(objType)

featureFactoryMap = {}
featureFactoryMap[float] = FloatFeature

class FeatureFactory:

    @classmethod
    def create(cls, objType: type) -> ObjectFeature:
        return featureFactoryMap.get(objType, GenericFeature)()

validFactoryMap = {}
validFactoryMap[List[float]] = lambda: Validators.forList(float,True)
validFactoryMap[List[List[float]]] = lambda: Validators.forList2d(float,True,True)

class ValidatorFactory:

    @classmethod
    def create(cls, objType: type) -> Callable[[Any,Any],bool]:
        fact = validFactoryMap.get(objType)
        if fact:
            return fact()
        return lambda x, y: FeatureFactory.create(objType).isEqual(x, y)

