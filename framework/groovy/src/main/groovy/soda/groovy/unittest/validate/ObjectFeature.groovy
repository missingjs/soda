package soda.groovy.unittest.validate

class ObjectFeature<T> {

    private Closure<Integer> hashClosure

    private Closure<Boolean> equalClosure

    ObjectFeature(Closure<Integer> hashClosure, Closure<Boolean> equalClosure) {
        this.hashClosure = hashClosure
        this.equalClosure = equalClosure
    }

    int hash(T obj) {
        hashClosure.call(obj)
    }

    boolean isEqual(T x, T y) {
        equalClosure.call(x, y)
    }

}
