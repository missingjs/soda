package soda.web.http;

import soda.web.exception.ParameterMissingException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultipartFormData {

    private final Map<String, List<Part>> partData;

    public MultipartFormData(Map<String, List<Part>> partData) {
        this.partData = new HashMap<>(partData);
    }

    public Optional<String> firstValueOpt(String key) {
        return elementOpt(key, Part::bodyString);
    }

    public String firstValue(String key) throws ParameterMissingException {
        return firstElement(key, Part::bodyString);
    }

    public List<String> values(String key) {
        return elements(key, Part::bodyString);
    }

    public Optional<byte[]> firstFileOpt(String key) {
        return elementOpt(key, p -> p.payload);
    }

    public byte[] firstFile(String key) {
        return firstElement(key, p -> p.payload);
    }

    public List<byte[]> files(String key) {
        return elements(key, p -> p.payload);
    }

    private <T> List<T> elements(String key, Function<Part, T> mapper) {
        var parts = partData.getOrDefault(key, Collections.emptyList());
        return parts.stream().map(mapper).collect(Collectors.toList());
    }

    private <T> Optional<T> elementOpt(String key, Function<Part, T> mapper) {
        var es = elements(key, mapper);
        return Optional.ofNullable(es.isEmpty() ? null : es.get(0));
    }

    private <T> T firstElement(String key, Function<Part, T> mapper) {
        return elementOpt(key, mapper).orElseThrow(() -> new ParameterMissingException(key));
    }

}
