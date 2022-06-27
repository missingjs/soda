package soda.groovy.web.http

import soda.groovy.web.exception.ParameterMissingException

class MultipartFormData {

    private Map<String, List<Part>> partData

    MultipartFormData(Map<String, List<Part>> partData) {
        this.partData = new HashMap<>(partData)
    }

    Optional<String> firstValueOpt(String key) {
        def v = partData.get(key, [])
        return Optional.ofNullable(v.empty ? null : v[0].bodyString())
    }

    String firstValue(String key) {
        return firstValueOpt(key).orElseThrow {
            new ParameterMissingException(key)
        }
    }

    List<String> values(String key) {
        return partData.get(key, []).collect { it.bodyString() }
    }

    Optional<byte[]> firstFileOpt(String key) {
        def v = partData.get(key, [])
        return Optional.ofNullable(v.empty ? null : v[0].payload)
    }

    byte[] firstFile(String key) {
        return firstFileOpt(key).orElseThrow {
            new ParameterMissingException(key)
        }
    }

    List<byte[]> files(String key) {
        return partData.get(key, []).collect { it.payload }
    }

}
