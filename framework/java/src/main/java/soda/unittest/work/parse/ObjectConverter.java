package soda.unittest.work.parse;

public interface ObjectConverter<R,J> {

    R fromJsonSerializable(J j);

    J toJsonSerializable(R r);

}
