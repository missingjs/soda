from collections import defaultdict
from typing import *

from .feature import ObjectFeature

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

class UnorderedListFeature(ObjectFeature):

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
        if len(a) != len(b):
            return False
        xmap = XMap(self.elemFeat)
        for e in a:
            xmap[e] = xmap.get(e, 0) + 1
        for e in b:
            if e not in xmap:
                return False
            xmap[e] -= 1
            if xmap[e] == 0:
                del xmap[e]
        return True
