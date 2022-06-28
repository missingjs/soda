package soda.unittest.task;

import soda.unittest.Utils;

public class ReflectionTask extends TaskBase<Object> {

    private final Object solution;

    public ReflectionTask(Object solution, String methodName) {
        super(Utils.findMethod(solution.getClass(), methodName), null);
        this.solution = solution;
    }

    @Override
    protected Object run() {
        return Utils.wrapEx(() -> {
            var args = getArguments();
            method.setAccessible(true);
            var res = method.invoke(solution, args);
            return voidFunc ? args[0] : res;
        });
    }
}
