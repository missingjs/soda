package soda.unittest.conv;

public interface ObjectConverter<R,J> {

    R fromJsonSerializable(J j);

    J toJsonSerializable(R r);

}
