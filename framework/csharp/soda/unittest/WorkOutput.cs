using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Soda.Unittest;

public class WorkOutput
{
    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("success")]
    public bool Success { get; set; }

    [JsonProperty("result")]
    public JToken Result { get; set; }

    [JsonProperty("elapse")]
    public double Elapse { get; set; }
}