package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.WorkInput;
import soda.unittest.function.BaseFun;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

abstract class TaskBase<R> implements TaskProxy<R> {

    private double elapseMillis;

    private Type returnType;

    private List<Type> argTypes;

    private List<Object> args;

    protected Method method;

    protected boolean voidFunc;

    protected BaseFun taskFunction;

    TaskBase(BaseFun function) {
        this.taskFunction = function;
        init(Utils.lambdaToMethod(function));
    }

    TaskBase(Method methodInfo, BaseFun taskFunction) {
        this.taskFunction = taskFunction;
        init(methodInfo);
    }

    private void init(Method method) {
        this.method = method;
        var ats = method.getGenericParameterTypes();
        argTypes = Arrays.stream(ats).collect(Collectors.toList());
        var retType = method.getGenericReturnType();
        voidFunc = retType.equals(void.class);
        returnType = voidFunc ? argTypes.get(0) : retType;
    }

    protected abstract R run();

    @SuppressWarnings("unchecked")
    protected <A> A arg(int index) {
        return (A) args.get(index);
    }

    @Override
    public R execute(WorkInput input) {
        args = Utils.parseArguments(argTypes, input.args);
        var startNano = System.nanoTime();
        var result = run();
        var endNano = System.nanoTime();
        elapseMillis = (endNano - startNano) / 1e6;
        return result;
    }

    @Override
    public Type getReturnType() {
        return returnType;
    }

    @Override
    public Type[] getArgumentTypes() {
        return argTypes.toArray(Type[]::new);
    }

    @Override
    public Object[] getArguments() {
        return args.toArray(Object[]::new);
    }

    @Override
    public double getElapseMillis() {
        return elapseMillis;
    }

}
