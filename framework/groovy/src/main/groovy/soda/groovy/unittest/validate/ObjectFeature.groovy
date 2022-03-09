package soda.groovy.unittest.validate

class ObjectFeature<T> {

    private Closure<Long> hashClosure

    private Closure<Boolean> equalClosure

    ObjectFeature(Closure<Long> hashClosure, Closure<Boolean> equalClosure) {
        this.hashClosure = hashClosure
        this.equalClosure = equalClosure
    }

    long hash(T obj) {
        hashClosure.call(obj)
    }

    boolean isEqual(T x, T y) {
        equalClosure.call(x, y)
    }

}
