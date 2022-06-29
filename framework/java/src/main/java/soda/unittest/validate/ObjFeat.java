package soda.unittest.validate;

import soda.unittest.Utils;

public interface ObjFeat {
    default <T> ObjectFeature<T> as() {
        return Utils.cast(this);
    }
}
