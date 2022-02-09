using Newtonsoft.Json.Linq;
using Soda.Unittest.Conv;

namespace Soda.Unittest;

public class StructTester
{
    private readonly Type structType;

    public StructTester(Type st)
    {
        structType = st;
    }

    public JToken Test(IList<string> operations, JToken parameters)
    {
        var ctor = structType.GetConstructors()[0];
        var ctorParamTypes = ctor.GetParameters().Select(e => e.ParameterType).ToList();
        var ctorParams = Utils.ParseArguments(ctorParamTypes, parameters[0] as JArray);
        var obj = ctor.Invoke(ctorParams.ToArray());
        var res = new List<object>();
        res.Add(JValue.CreateNull());
        for (int i = 1; i < parameters.Count(); ++i) {
            var method = structType.GetMethod(Utils.Capitalize(operations[i]));
            var paramTypes = method.GetParameters().Select(e => e.ParameterType).ToList();
            var methodParams = Utils.ParseArguments(paramTypes, parameters[i] as JArray);
            var r = method.Invoke(obj, methodParams.ToArray());
            if (r != null) {
                var conv = ConverterFactory.Create(method.ReturnType);
                res.Add(conv.ToJsonSerializable(r));
            } else {
                res.Add(JValue.CreateNull());
            }
        }
        return JToken.FromObject(res);
    }
}