package soda.web.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultipartFormData {

    private Map<String, List<Part>> partData;

    public MultipartFormData(Map<String, List<Part>> partData) {
        this.partData = new HashMap<>(partData);
    }

    public String firstValue(String key) {
        var v = partData.get(key);
        return v != null && v.size() > 0 ? v.get(0).bodyString() : null;
    }

    public List<String> values(String key) {
        var v = partData.get(key);
        return v != null && v.size() > 0
                ? v.stream().map(Part::bodyString).collect(Collectors.toList())
                : Collections.emptyList();
    }

    public byte[] firstFile(String key) {
        var v = partData.get(key);
        return v != null && v.size() > 0 ? v.get(0).payload : null;
    }

    public List<byte[]> files(String key) {
        var v = partData.get(key);
        return v != null && v.size() > 0
                ? v.stream().map(p -> p.payload).collect(Collectors.toList())
                : Collections.emptyList();
    }

}
