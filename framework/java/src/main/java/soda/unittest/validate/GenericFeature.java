package soda.unittest.validate;

import java.util.Objects;

public class GenericFeature<T> implements ObjectFeature<T> {
    @Override
    public long hash(T obj) {
        return obj.hashCode();
    }

    @Override
    public boolean isEqual(T a, T b) {
        return Objects.equals(a, b);
    }
}
