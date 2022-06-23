package soda.web.http;

import java.util.*;
import java.util.stream.Collectors;

public class MultipartFormData {

    private Map<String, List<Part>> partData;

    public MultipartFormData(Map<String, List<Part>> partData) {
        this.partData = new HashMap<>(partData);
    }

    public Optional<String> firstValue(String key) {
        var v = partData.getOrDefault(key, Collections.emptyList());
        return Optional.ofNullable(v.isEmpty() ? null : v.get(0).bodyString());
    }

    public List<String> values(String key) {
        var v = partData.getOrDefault(key, Collections.emptyList());
        return v.stream().map(Part::bodyString).collect(Collectors.toList());
    }

    public Optional<byte[]> firstFile(String key) {
        var v = partData.getOrDefault(key, Collections.emptyList());
        return Optional.ofNullable(v.isEmpty() ? null : v.get(0).payload);
    }

    public List<byte[]> files(String key) {
        var v = partData.getOrDefault(key, Collections.emptyList());
        return v.stream().map(p -> p.payload).collect(Collectors.toList());
    }

}
