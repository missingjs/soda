package soda.groovy.unittest.conv

class ObjectConverter<T, J> {

    private Closure<T> parser

    private Closure<J> serializer

    ObjectConverter(Closure<T> p, Closure<J> s) {
        parser = p
        serializer = s
    }

    T fromJsonSerializable(J j) {
        parser.call(j)
    }

    J toJsonSerializable(T t) {
        serializer.call(t)
    }

}
