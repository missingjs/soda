package soda.web.bootstrap;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ContextManager {

    private final Map<String, BootstrapContext> contextMap = new ConcurrentHashMap<>();

    public BootstrapContext register(String key, byte[] artifact) {
        var context = new BootstrapContext(key, artifact);
        contextMap.put(key, context);
        return context;
    }

    public Optional<BootstrapContext> get(String key) {
        return Optional.ofNullable(contextMap.get(key));
    }

}
