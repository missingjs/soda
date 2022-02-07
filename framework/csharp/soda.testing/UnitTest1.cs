using System;
using System.Reflection;
using Xunit;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using soda.unittest;

namespace soda.testing;

public class UnitTest1
{
    [Fact]
    public void Test1()
    {
        var str = @"{""args"": [1, 2, [3], [""four"",null]], ""expected"": [1,2,3,4], ""id"": 1}";
        var input = JsonConvert.DeserializeObject<WorkInput>(str);
        Console.WriteLine(input.Args[2].GetType());

        var s = @"[1,2.02,3,4,""5""]";
        var r = JToken.Parse(s);
        Console.WriteLine(r[1].Type);
        var a = r.ToObject<JArray>();
        Console.WriteLine(r[4].ToObject<string>());

        var obja = new A();
        Console.WriteLine(obja.Call((int i) => obja.Func1(i), 10));
        Console.WriteLine(obja.Call(obja.Func2, 3, 4));
        obja.Call((string s) => "[" + s + "]", "simple");
        obja.Call((A ai) => ai.ToString(), new A());
        obja.Call((int j) => j - 10, 20);
        obja.CallVoid(obja.Func0, "a");
        obja.invoke(wrap(obja.Func1));
    }

    public static T wrap<T>(T t)
    {
        return t;
    }
}

class A
{
    public void Func0(string s)
    {

    }
    public int Func1(int v)
    {
        return -v;
    }

    public int Func2(int a, int b)
    {
        return a + b;
    }

    public R Call<P1,R>(Func<P1,R> f, object v)
    {
        var mi = RuntimeReflectionExtensions.GetMethodInfo(f);
        Console.WriteLine($"return type: {mi.ReturnType}");
        foreach (var t in mi.GetParameters()) {
            Console.WriteLine($"type: {t}");
            // t.ParameterType
        }
        return f((P1) v);
    }

    public void CallVoid<P1>(Action<P1> a, P1 p1)
    {
        a(p1);
    }

    public R Call<P1,P2,R>(Func<P1,P2,R> f, P1 v1, P2 v2)
    {
        return f(v1, v2);
    }

    public void invoke<P1,R>(Func<P1,R> f)
    {
        Console.WriteLine("P1,R");
    }

    public void invoke<P1,P2,R>(Func<P1,P2,R> f)
    {
        Console.WriteLine("P1,P2,R");
    }
}