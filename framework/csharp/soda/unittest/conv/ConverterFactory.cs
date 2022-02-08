using Newtonsoft.Json.Linq;

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
                (JToken js) => conv.fromJsonSerializable(js),
                (object obj) => conv.toJsonSerializable((T) obj)
            );
        };
        genericFactoryMap[typeof(T)] = genericFactory;
    }

    static ConverterFactory()
    {
        // TODO
    }

    public static ObjectConverter create(Type type)
    {
        if (factoryMap.ContainsKey(type)) {
            return factoryMap[type]();
        }
        return new ObjectConverter(
            (JToken js) => js.ToObject(type), 
            (object obj) => JToken.FromObject(obj)
        );
    }

    public static ObjectConverter<T> create<T>()
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