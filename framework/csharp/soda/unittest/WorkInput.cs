using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace soda.unittest;

public class WorkInput
{
    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("expected")]
    public JToken Expected { get; set; }

    [JsonProperty("args")]
    public JArray Args { get; set; }
}