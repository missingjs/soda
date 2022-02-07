using soda.unittest.task;

namespace soda.unittest;

public static class WorkFactory
{
    public static TestWork<R> Create<R>(Action<R> act)
    {
        return Create((R p) => {act(p); return p; });
    }

    public static TestWork<R> Create<P1, R>(Func<P1, R> fn)
    {
        return new TestWork<R>(new Task<P1, R>(fn));
    }

    public static TestWork<R> Create<P1, P2, R>(Func<P1, P2, R> fn)
    {
        return new TestWork<R>(new Task<P1, P2, R>(fn));
    }

    public static TestWork<R> Create<P1, P2, P3, R>(Func<P1, P2, P3, R> fn)
    {
        return new TestWork<R>(new Task<P1, P2, P3, R>(fn));
    }

    public static TestWork<R> Create<P1, P2, P3, P4, R>(Func<P1, P2, P3, P4, R> fn)
    {
        return new TestWork<R>(new Task<P1, P2, P3, P4, R>(fn));
    }

    public static TestWork<R> Create<P1, P2, P3, P4, P5, R>(Func<P1, P2, P3, P4, P5, R> fn)
    {
        return new TestWork<R>(new Task<P1, P2, P3, P4, P5, R>(fn));
    }

    public static TestWork<R> Create<P1, P2, P3, P4, P5, P6, R>(Func<P1, P2, P3, P4, P5, P6, R> fn)
    {
        return new TestWork<R>(new Task<P1, P2, P3, P4, P5, P6, R>(fn));
    }
}