using Newtonsoft.Json.Linq;

namespace Soda.Unittest.Conv;

public class ConverterBase {}

public class ObjectConverter : ConverterBase
{
    private readonly Func<JToken, object> parser;

    private readonly Func<object, JToken> serializer;

    public ObjectConverter(Func<JToken, object> p, Func<object, JToken> s)
    {
        parser = p;
        serializer = s;
    }

    public object fromJsonSerializable(JToken js)
    {
        return parser(js);
    }

    public JToken toJsonSerializable(object obj)
    {
        return serializer(obj);
    }
}

public class ObjectConverter<T> : ConverterBase
{
    private readonly Func<JToken, T> parser;

    private readonly Func<T, JToken> serializer;

    public ObjectConverter(Func<JToken, T> p, Func<T, JToken> s)
    {
        parser = p;
        serializer = s;
    }

    public T fromJsonSerializable(JToken js)
    {
        return parser(js);
    }

    public JToken toJsonSerializable(T obj)
    {
        return serializer(obj);
    }
}