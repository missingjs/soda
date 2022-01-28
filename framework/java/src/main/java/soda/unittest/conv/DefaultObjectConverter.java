package soda.unittest.conv;

public class DefaultObjectConverter implements ObjectConverter<Object, Object> {
    @Override
    public Object fromJsonSerializable(Object o) {
        return o;
    }

    @Override
    public Object toJsonSerializable(Object o) {
        return o;
    }
}
