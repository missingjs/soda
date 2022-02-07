using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Soda.Unittest;

public class WorkInput
{
    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("expected")]
    public JToken Expected { get; set; }

    [JsonProperty("args")]
    public JArray Args { get; set; }
}