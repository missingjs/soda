package soda.unittest.function;

@FunctionalInterface
public interface Function2<P1,P2,R> extends BaseFun {
    R apply(P1 p1, P2 p2);
}
