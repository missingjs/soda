package soda.unittest.work.parse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeRef<T> {

    protected TypeRef() {}

    public Type getRefType() {
        var t = this.getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            return ((ParameterizedType) t).getActualTypeArguments()[0];
        }
        return null;
    }
}
