package soda.unittest.task;

import soda.unittest.function.Function2;

public class Task2<P1, P2, R> extends TaskBase<R> {

    private final Function2<P1, P2, R> func;

    public Task2(Class<?> workClass, String methodName, Function2<P1, P2, R> func) {
        super(workClass, methodName);
        this.func = func;
    }

    @Override
    protected R run() {
        return func.apply(arg(0), arg(1));
    }
}
