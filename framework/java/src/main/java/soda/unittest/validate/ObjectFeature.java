package soda.unittest.validate;

public interface ObjectFeature<T> extends ObjFeat {

    long hash(T obj);

    boolean isEqual(T a, T b);

}
