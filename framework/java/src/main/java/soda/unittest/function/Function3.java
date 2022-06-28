package soda.unittest.function;

@FunctionalInterface
public interface Function3<P1, P2, P3, R> extends BaseFun {
    R apply(P1 p1, P2 p2, P3 p3);
}
