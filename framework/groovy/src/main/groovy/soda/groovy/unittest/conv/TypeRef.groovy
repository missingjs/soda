package soda.groovy.unittest.conv

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class TypeRef<T> {

    protected TypeRef() {}

    Type getRefType() {
        var t = this.getClass().getGenericSuperclass()
        if (t instanceof ParameterizedType) {
            return ((ParameterizedType) t).getActualTypeArguments()[0]
        }
        return null
    }
}
