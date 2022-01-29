package soda.unittest.task;

import soda.unittest.function.Function3;

public class Task3<P1, P2, P3, R> extends TaskBase<R> {

    private final Function3<P1, P2, P3, R> func;

    public Task3(Class<?> workClass, String methodName, Function3<P1, P2, P3, R> func) {
        super(workClass, methodName);
        this.func = func;
    }

    @Override
    protected R run() {
        return func.apply(arg(0), arg(1), arg(2));
    }
}
