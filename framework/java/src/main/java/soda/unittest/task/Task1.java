package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.function.Function1;
import soda.unittest.function.SerialConsumer;

import java.lang.reflect.Method;

public class Task1<A1, R> extends TaskBase<R> {

    public Task1(Function1<A1, R> func) {
        this(Utils.lambdaToMethod(func), func);
    }

    Task1(Method method, Function1<A1, R> func) {
        super(method, func);
    }

    @Override
    protected R run() {
        return Utils.<Function1<A1, R>>cast(taskFunction).apply(arg(0));
    }

    public static <T> Task1<T, T> forVoid(SerialConsumer<T> func) {
        var method = Utils.lambdaToMethod(func);
        return new Task1<>(method, (T p) -> { func.accept(p); return p; });
    }

}
