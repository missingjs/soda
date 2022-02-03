from typing import *

from .featurefactory import (
    FeatureFactory,
    ListFeature,
    UnorderedListFeature
)

class Validators:

    @classmethod
    def forList(cls, objType: type, ordered: bool) -> Callable[[Any,Any],bool]:
        return cls._for_list(ordered, FeatureFactory.create(objType))

    @classmethod
    def forList2d(cls, objType: type, dim1Ordered: bool, dim2Ordered: bool) -> Callable[[Any,Any],bool]:
        elemFeat = FeatureFactory.create(objType)
        d = ListFeature(elemFeat) if dim2Ordered else UnorderedListFeature(elemFeat)
        return cls._for_list(dim1Ordered, d)

    @classmethod
    def _for_list(cls, ordered: bool, elemFeat: 'ObjectFeature') -> Callable[[Any,Any],bool]:
        return ListFeature(elemFeat).isEqual if ordered else UnorderedListFeature(elemFeat).isEqual
