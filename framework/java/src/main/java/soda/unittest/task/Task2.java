package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.function.Function2;

public class Task2<P1, P2, R> extends TaskBase<R> {

    public Task2(Function2<P1, P2, R> func) {
        super(func);
    }

    @Override
    protected R run() {
        return Utils.<Function2<P1,P2,R>>cast(taskFunction).apply(arg(0), arg(1));
    }
}
