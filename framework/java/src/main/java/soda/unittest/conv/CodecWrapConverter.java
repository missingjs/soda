package soda.unittest.conv;

import soda.unittest.job.codec.ICodec;

class CodecWrapConverter implements ObjectConverter<Object, Object> {

    private ICodec<Object, Object> codec;

    @SuppressWarnings("unchecked")
    public CodecWrapConverter(ICodec<?,?> codec) {
        this.codec = (ICodec<Object, Object>) codec;
    }

    @Override
    public Object fromJsonSerializable(Object o) {
        return codec.decode(o);
    }

    @Override
    public Object toJsonSerializable(Object o) {
        return codec.encode(o);
    }
}
