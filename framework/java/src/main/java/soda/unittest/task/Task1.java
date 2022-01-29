package soda.unittest.task;

import java.util.function.Function;

public class Task1<A1, R> extends TaskBase<R> {

    private final Function<A1, R> func;

    public Task1(Class<?> workClass, String methodName, Function<A1, R> func) {
        super(workClass, methodName);
        this.func = func;
    }

    @Override
    protected R run() {
        return func.apply(arg(0));
    }
}
