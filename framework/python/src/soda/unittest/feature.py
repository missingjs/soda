from typing import *

class ObjectFeature:

    def hash(self, obj: Any) -> int:
        return hash(obj)

    def isEqual(self, a: Any, b: Any) -> bool:
        return a == b
