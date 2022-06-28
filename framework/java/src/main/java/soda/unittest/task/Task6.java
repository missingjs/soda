package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.function.Function6;

public class Task6<P1, P2, P3, P4, P5, P6, R> extends TaskBase<R> {

    public Task6(Function6<P1, P2, P3, P4, P5, P6, R> func) {
        super(func);
    }

    @Override
    protected R run() {
        return Utils.<Function6<P1, P2, P3, P4, P5, P6, R>>cast(taskFunction)
                .apply(arg(0), arg(1), arg(2), arg(3), arg(4), arg(5));
    }
}
