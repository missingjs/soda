from collections import defaultdict
from abc import ABC, abstractmethod
from typing import *

class Descriptor(ABC):
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
    def __init__(self, descriptor: Descriptor):
        self.desc = descriptor
        self.dict = defaultdict(list)
        self.size = 0

    def _hash(self, key: Any) -> int:
        return self.desc.hash(key)

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
            if self.desc.isEqual(key, entry.key):
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
            if self.desc.isEqual(key, entry.key):
                e = entry
                break
        if e is not None:
            self.dict[h].remove(e)
            self.size -= 1

def unorderListComparator(desc: Descriptor):
    def comp(a: List[Any], b: List[Any]) -> bool:
        if len(a) != len(b):
            return False
        xmap = XMap(desc)
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

def listComparator(desc: Descriptor):
    def comp(a: List[Any], b: List[Any]) -> bool:
        if len(a) != len(b):
            return False
        for i in range(len(a)):
            if not desc.isEqual(a[i], b[i]):
                return False
        return True
    return comp

class DefaultDescriptor(Descriptor):
    def hash(self, obj: Any) -> int:
        return hash(obj)

    def isEqual(self, a: Any, b: Any) -> bool:
        return a == b

class ListDescriptor(Descriptor):
    def __init__(self, elemDesc: Descriptor):
        self.elemDesc = elemDesc

    def hash(self, obj: List[Any]) -> int:
        res = 0
        # keep low 48 bits
        mask = 0xffffffffffff
        for e in obj:
            h = self.elemDesc.hash(e)
            res = res * 133 + h
            res &= mask
        return res

    def isEqual(self, a: List[Any], b: List[Any]) -> bool:
        return listComparator(self.elemDesc)(a, b)

class UnorderListDescriptor(Descriptor):
    def __init__(self, elemDesc: Descriptor):
        self.elemDesc = elemDesc

    def hash(self, obj: List[Any]) -> int:
        res = 0
        # keep low 48 bits
        mask = 0xffffffffffff
        for h in sorted(map(lambda x: self.elemDesc.hash(x), obj)):
            res = res * 133 + h
            res &= mask
        return res

    def isEqual(self, a: List[Any], b: List[Any]) -> bool:
        return unorderListComparator(self.elemDesc)(a, b)

class Validators:

    @classmethod
    def forList(cls, ordered: bool):
        d = DefaultDescriptor()
        return listComparator(d) if ordered else unorderListComparator(d)

    @classmethod
    def forList2d(cls, dim1Ordered: bool, dim2Ordered: bool):
        elemDesc = DefaultDescriptor()
        d = ListDescriptor(elemDesc) if dim2Ordered else UnorderListDescriptor(elemDesc)
        return listComparator(d) if dim1Ordered else unorderListComparator(d)

