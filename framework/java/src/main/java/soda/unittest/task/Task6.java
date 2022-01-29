package soda.unittest.task;

import soda.unittest.function.Function6;

public class Task6<P1, P2, P3, P4, P5, P6, R> extends TaskBase<R> {

    private final Function6<P1, P2, P3, P4, P5, P6, R> func;

    public Task6(Class<?> workClass, String methodName, Function6<P1, P2, P3, P4, P5, P6, R> func) {
        super(workClass, methodName);
        this.func = func;
    }

    @Override
    protected R run() {
        return func.apply(arg(0), arg(1), arg(2), arg(3), arg(4), arg(5));
    }
}
