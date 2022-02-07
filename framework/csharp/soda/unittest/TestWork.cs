using Newtonsoft.Json;
using soda.unittest.task;

namespace soda.unittest;

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
        
        return text;
    }

    public void SetValidator(Func<R, R, bool> v)
    {
        validator = v;
    }
}
