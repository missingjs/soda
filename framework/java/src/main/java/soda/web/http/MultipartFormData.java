package soda.web.http;

import soda.web.exception.ParameterMissingException;

import java.util.*;
import java.util.stream.Collectors;

public class MultipartFormData {

    private final Map<String, List<Part>> partData;

    public MultipartFormData(Map<String, List<Part>> partData) {
        this.partData = new HashMap<>(partData);
    }

    public Optional<String> firstValueOpt(String key) {
        var v = partData.getOrDefault(key, Collections.emptyList());
        return Optional.ofNullable(v.isEmpty() ? null : v.get(0).bodyString());
    }

    public String firstValue(String key) throws ParameterMissingException {
        return firstValueOpt(key).orElseThrow(() -> new ParameterMissingException(key));
    }

    public List<String> values(String key) {
        var v = partData.getOrDefault(key, Collections.emptyList());
        return v.stream().map(Part::bodyString).collect(Collectors.toList());
    }

    public Optional<byte[]> firstFileOpt(String key) {
        var v = partData.getOrDefault(key, Collections.emptyList());
        return Optional.ofNullable(v.isEmpty() ? null : v.get(0).payload);
    }

    public byte[] firstFile(String key) {
        return firstFileOpt(key).orElseThrow(() -> new ParameterMissingException(key));
    }

    public List<byte[]> files(String key) {
        var v = partData.getOrDefault(key, Collections.emptyList());
        return v.stream().map(p -> p.payload).collect(Collectors.toList());
    }

}
