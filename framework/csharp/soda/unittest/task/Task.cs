using System.Reflection;

namespace Soda.Unittest.Task;

public class Task<P1, R> : TaskBase<R>
{
    private readonly Func<P1, R> targetFunc;

    public Task(Func<P1, R> targetFunc)
    : base(RuntimeReflectionExtensions.GetMethodInfo(targetFunc))
    {
        this.targetFunc = targetFunc;
    }
    
    protected override R run()
    {
        return targetFunc(arg<P1>(0));
    }
}

public class Task<P1, P2, R> : TaskBase<R>
{
    private readonly Func<P1, P2, R> targetFunc;

    public Task(Func<P1, P2, R> targetFunc) 
    : base(RuntimeReflectionExtensions.GetMethodInfo(targetFunc))
    {
        this.targetFunc = targetFunc;
    }
    
    protected override R run()
    {
        return targetFunc(arg<P1>(0), arg<P2>(1));
    }
}

public class Task<P1, P2, P3, R> : TaskBase<R>
{
    private readonly Func<P1, P2, P3, R> targetFunc;

    public Task(Func<P1, P2, P3, R> targetFunc) 
    : base(RuntimeReflectionExtensions.GetMethodInfo(targetFunc))
    {
        this.targetFunc = targetFunc;
    }
    
    protected override R run()
    {
        return targetFunc(arg<P1>(0), arg<P2>(1), arg<P3>(2));
    }
}

public class Task<P1, P2, P3, P4, R> : TaskBase<R>
{
    private readonly Func<P1, P2, P3, P4, R> targetFunc;

    public Task(Func<P1, P2, P3, P4, R> targetFunc) 
    : base(RuntimeReflectionExtensions.GetMethodInfo(targetFunc))
    {
        this.targetFunc = targetFunc;
    }
    
    protected override R run()
    {
        return targetFunc(arg<P1>(0), arg<P2>(1), arg<P3>(2), arg<P4>(3));
    }
}

public class Task<P1, P2, P3, P4, P5, R> : TaskBase<R>
{
    private readonly Func<P1, P2, P3, P4, P5, R> targetFunc;

    public Task(Func<P1, P2, P3, P4, P5, R> targetFunc) 
    : base(RuntimeReflectionExtensions.GetMethodInfo(targetFunc))
    {
        this.targetFunc = targetFunc;
    }
    
    protected override R run()
    {
        return targetFunc(arg<P1>(0), arg<P2>(1), arg<P3>(2), arg<P4>(3), arg<P5>(4));
    }
}

public class Task<P1, P2, P3, P4, P5, P6, R> : TaskBase<R>
{
    private readonly Func<P1, P2, P3, P4, P5, P6, R> targetFunc;

    public Task(Func<P1, P2, P3, P4, P5, P6, R> targetFunc) 
    : base(RuntimeReflectionExtensions.GetMethodInfo(targetFunc))
    {
        this.targetFunc = targetFunc;
    }
    
    protected override R run()
    {
        return targetFunc(arg<P1>(0), arg<P2>(1), arg<P3>(2), arg<P4>(3), arg<P5>(4), arg<P6>(5));
    }
}
