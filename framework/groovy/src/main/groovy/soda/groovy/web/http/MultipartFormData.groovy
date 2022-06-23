package soda.groovy.web.http

class MultipartFormData {

    private Map<String, List<Part>> partData

    MultipartFormData(Map<String, List<Part>> partData) {
        this.partData = new HashMap<>(partData)
    }

    Optional<String> firstValue(String key) {
        def v = partData.get(key, [])
        return Optional.ofNullable(v.empty ? null : v[0].bodyString())
    }

    List<String> values(String key) {
        return partData.get(key, []).collect { it.bodyString() }
    }

    Optional<byte[]> firstFile(String key) {
        def v = partData.get(key, [])
        return Optional.ofNullable(v.empty ? null : v[0].payload)
    }

    List<byte[]> files(String key) {
        return partData.get(key, []).collect { it.payload }
    }

}
