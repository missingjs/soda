package soda.unittest.validate;

public interface ObjectFeature<T> {

    long hash(T obj);

    boolean isEqual(T a, T b);

}
