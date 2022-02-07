using Newtonsoft.Json.Linq;

namespace soda.unittest;

public static class Utils
{
    public static string readStdin()
    {
        using (var sr = new StreamReader(Console.OpenStandardInput(), Console.InputEncoding))
        {
            return sr.ReadToEnd();
        }
    }

    public static IList<object> parseArguments(IList<Type> types, JArray rawParams)
    {
        // TODO
        return new List<object>();
    }

    public static T Fn<T>(T t)
    {
        return t;
    }
}