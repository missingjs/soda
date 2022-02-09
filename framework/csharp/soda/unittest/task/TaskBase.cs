using System.Reflection;

namespace Soda.Unittest.Task;

public abstract class TaskBase<R> : ITaskProxy<R>
{
    private double elapseMillis;

    private readonly Type returnType;

    private readonly IList<Type> argTypes;

    private IList<object> args;

    private readonly MethodInfo methodInfo;

    public TaskBase(MethodInfo info)
    {
        methodInfo = info;
        argTypes = info.GetParameters().Select(e => e.ParameterType).ToList();
        returnType = info.ReturnType;
    }

    public Type GetReturnType()
    {
        return returnType;
    }

    public Type[] GetArgumentTypes()
    {
        return argTypes.ToArray();
    }

    public object[] GetArguments()
    {
        return args.ToArray();
    }

    public R Execute(WorkInput input)
    {
        args = Utils.ParseArguments(argTypes, input.Args);
        var startTime = DateTime.Now;
        var result = run();
        var endTime = DateTime.Now;
        elapseMillis = ((TimeSpan)(endTime - startTime)).TotalMilliseconds;
        return result;
    }

    protected abstract R run();

    public double GetElapseMillis()
    {
        return elapseMillis;
    }

    protected T arg<T>(int index)
    {
        return (T) args[index];
    }
}