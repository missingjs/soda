package soda.unittest.validate;

import java.util.function.BiPredicate;

public interface Validator<T> extends BiPredicate<T, T> {
}
