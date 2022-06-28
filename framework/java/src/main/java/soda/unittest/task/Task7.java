package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.function.Function7;

public class Task7<P1, P2, P3, P4, P5, P6, P7, R> extends TaskBase<R> {

    public Task7(Function7<P1, P2, P3, P4, P5, P6, P7, R> func) {
        super(func);
    }

    @Override
    protected R run() {
        return Utils.<Function7<P1, P2, P3, P4, P5, P6, P7, R>>cast(taskFunction)
                .apply(arg(0), arg(1), arg(2), arg(3), arg(4), arg(5), arg(6));
    }
}
