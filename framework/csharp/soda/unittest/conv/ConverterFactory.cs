using Newtonsoft.Json.Linq;
using Soda.Leetcode;

namespace Soda.Unittest.Conv;

public static class ConverterFactory
{
    private static readonly IDictionary<Type, Func<ObjectConverter>> factoryMap 
        = new Dictionary<Type, Func<ObjectConverter>>();

    private static readonly IDictionary<Type, Func<ConverterBase>> genericFactoryMap
         = new Dictionary<Type, Func<ConverterBase>>();

    private static void registerFactory<T>(Func<ObjectConverter<T>> genericFactory)
    {
        factoryMap[typeof(T)] = () => {
            var conv = genericFactory();
            return new ObjectConverter(
                (JToken js) => conv.FromJsonSerializable(js),
                (object obj) => conv.ToJsonSerializable((T) obj)
            );
        };
        genericFactoryMap[typeof(T)] = genericFactory;
    }

    private static void registerFactory<T, M>(Func<M, T> parser, Func<T, M> serializer)
    {
        registerFactory(() => new ObjectConverter<T>(
            (JToken js) => parser(js.ToObject<M>()),
            (T obj) => JToken.FromObject(serializer(obj))
        ));
    }

    static ConverterFactory()
    {
        // ListNode
        registerFactory(fn(ListFactory.Create), fn(ListFactory.Dump));

        // IList<ListNode>
        registerFactory(
            (IList<IList<int>> d) => d.Select(ListFactory.Create).ToList(),
            (IList<ListNode> t) => t.Select(ListFactory.Dump).ToList()
        );

        // NestedInteger
        registerFactory(() => NestedIntegerConverter.Create());

        // IList<NestedInteger>
        registerFactory(() => NestedIntegerConverter.CreateForList());

        // TreeNode
        registerFactory(fn(TreeFactory.Create), fn(TreeFactory.Dump));
    }

    private static T fn<T>(T t)
    {
        return t;
    }

    public static ObjectConverter Create(Type type)
    {
        if (factoryMap.ContainsKey(type)) {
            return factoryMap[type]();
        }
        return new ObjectConverter(
            (JToken js) => js.ToObject(type), 
            (object obj) => JToken.FromObject(obj)
        );
    }

    public static ObjectConverter<T> Create<T>()
    {
        var type = typeof(T);
        if (genericFactoryMap.ContainsKey(type)) {
            return (ObjectConverter<T>) genericFactoryMap[type]();
        }
        return new ObjectConverter<T>(
            (JToken js) => js.ToObject<T>(),
            (T obj) => JToken.FromObject(obj)
        );
    }
}