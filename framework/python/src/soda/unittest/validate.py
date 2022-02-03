from typing import *

from .featurefactory import (
    FeatureFactory,
    ListFeature,
    UnorderListFeature
)

class Validators:

    @classmethod
    def forList(cls, objType: type, ordered: bool) -> Callable[[Any,Any],bool]:
        d = FeatureFactory.create(objType)
        return ListFeature(d).isEqual if ordered else UnorderListFeature(d).isEqual

    @classmethod
    def forList2d(cls, objType: type, dim1Ordered: bool, dim2Ordered: bool) -> Callable[[Any,Any],bool]:
        elemFeat = FeatureFactory.create(objType)
        d = ListFeature(elemFeat) if dim2Ordered else UnorderListFeature(elemFeat)
        return ListFeature(d).isEqual if dim1Ordered else UnorderListFeature(d).isEqual
