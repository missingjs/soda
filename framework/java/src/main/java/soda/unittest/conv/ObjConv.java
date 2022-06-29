package soda.unittest.conv;

import soda.unittest.Utils;

interface ObjConv {
    default <R, J> ObjectConverter<R, J> as() {
        return Utils.cast(this);
    }
}
