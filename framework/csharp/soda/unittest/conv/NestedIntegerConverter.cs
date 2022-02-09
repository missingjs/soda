using Newtonsoft.Json.Linq;
using Soda.Leetcode;

namespace Soda.Unittest.Conv;

public static class NestedIntegerConverter
{
    public static ObjectConverter<NestedInteger> Create()
    {
        return new ObjectConverter<NestedInteger>(parse, serialize);
    }

    public static ObjectConverter<IList<NestedInteger>> CreateForList()
    {
        return new ObjectConverter<IList<NestedInteger>>(
            (JToken jt) => jt.Select(e => parse(e)).ToList(),
            (IList<NestedInteger> ns) => JToken.FromObject(ns.Select(e => serialize(e)))
        );
    }

    private static NestedInteger parse(JToken jt)
    {
        if (jt.Type == JTokenType.Integer) {
            return new NestedIntegerImpl(jt.ToObject<int>());
        }
        var ni = new NestedIntegerImpl();
        foreach (var a in jt) {
            ni.Add(parse(a));
        }
        return ni;
    }

    private static JToken serialize(NestedInteger ni)
    {
        if (ni.IsInteger()) {
            return JToken.FromObject(ni.GetInteger());
        }
        var list = new JArray();
        foreach (var e in ni.GetList()) {
            list.Add(serialize(e));
        }
        return list;
    }
}