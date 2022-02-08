using Newtonsoft.Json;
using Soda.Unittest.Conv;
using Soda.Unittest.Task;
using Soda.Unittest.Validate;

namespace Soda.Unittest;

public class TestWork<R>
{
    private ITaskProxy<R> proxy;

    public bool CompareSerial { get; set; }

    private Func<R, R, bool> validator;

    public TestWork(ITaskProxy<R> proxy)
    {
        this.proxy = proxy;
    }

    public string Run(string text)
    {
        var input = JsonConvert.DeserializeObject<WorkInput>(text);
        var result = proxy.Execute(input);

        var output = new WorkOutput();
        output.Id = input.Id;
        output.Elapse = proxy.GetElapseMillis();

        var retType = proxy.GetReturnType();
        var resConv = ConverterFactory.create<R>();
        output.Result = resConv.toJsonSerializable(result);

        bool success = true;
        if (input.Expected != null) {
            if (CompareSerial && validator == null) {
                var a = JsonConvert.SerializeObject(input.Expected);
                var b = JsonConvert.SerializeObject(output.Result);
                success = object.Equals(a, b);
            } else {
                var expect = resConv.fromJsonSerializable(input.Expected);
                if (validator != null) {
                    success = validator(expect, result);
                } else {
                    success = FeatureFactory.create<R>().IsEqual(expect, result);
                }
            }
        }
        output.Success = success;
        return JsonConvert.SerializeObject(output);
    }

    public void SetValidator(Func<R, R, bool> v)
    {
        validator = v;
    }
}
