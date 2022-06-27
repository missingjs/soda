package soda.groovy.web.bootstrap

class ContextManager {

    private final Map<String, BootstrapContext> contextMap = Collections.synchronizedMap(new WeakHashMap<>())

    BootstrapContext register(String key, byte[] artifact) {
        def context = new BootstrapContext(key, artifact)
        contextMap.put(key, context)
        return context
    }

    Optional<BootstrapContext> get(String key) {
        return Optional.ofNullable(contextMap.get(key))
    }

}

