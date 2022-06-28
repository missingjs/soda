package soda.unittest.function;

@FunctionalInterface
public interface Function1<P1, R> extends BaseFun {
    R apply(P1 p1);
}
