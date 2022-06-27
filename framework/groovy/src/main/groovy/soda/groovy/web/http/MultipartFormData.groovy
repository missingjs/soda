package soda.groovy.web.http

import soda.groovy.web.exception.ParameterMissingException

class MultipartFormData {

    private Map<String, List<Part>> partData

    MultipartFormData(Map<String, List<Part>> partData) {
        this.partData = new HashMap<>(partData)
    }

    Optional<String> firstValueOpt(String key) {
        return elementOpt(key, { Part it -> it.bodyString() })
    }

    String firstValue(String key) {
        return firstElement(key, { Part it -> it.bodyString() })
    }

    List<String> values(String key) {
        return elements(key, { Part it -> it.bodyString() })
    }

    Optional<byte[]> firstFileOpt(String key) {
        return elementOpt(key, { Part it -> it.payload })
    }

    byte[] firstFile(String key) {
        return firstElement(key, { Part it -> it.payload })
    }

    List<byte[]> files(String key) {
        return elements(key, { Part it -> it.payload })
    }

    private <T> List<T> elements(String key, Closure<T> mapper) {
        return partData.get(key, []).collect(mapper)
    }

    private <T> Optional<T> elementOpt(String key, Closure<T> mapper) {
        def es = elements(key, mapper)
        return Optional.ofNullable(es.empty ? null : es[0])
    }

    private <T> T firstElement(String key, Closure<T> mapper) {
        return elementOpt(key, mapper).orElseThrow {
            new ParameterMissingException(key)
        }
    }

}
