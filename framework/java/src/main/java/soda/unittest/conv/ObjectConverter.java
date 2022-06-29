package soda.unittest.conv;

public interface ObjectConverter<R, J> extends ObjConv {

    R fromJsonSerializable(J j);

    J toJsonSerializable(R r);

}
