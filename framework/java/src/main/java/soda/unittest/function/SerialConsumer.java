package soda.unittest.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface SerialConsumer<T> extends Consumer<T>, BaseFun {
}
