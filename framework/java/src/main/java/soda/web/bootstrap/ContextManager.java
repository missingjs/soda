package soda.web.bootstrap;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class ContextManager {

    private final Map<String, BootstrapContext> contextMap = Collections.synchronizedMap(new WeakHashMap<>());

    public BootstrapContext register(String key, byte[] artifact) {
        var context = new BootstrapContext(key, artifact);
        contextMap.put(key, context);
        return context;
    }

    public Optional<BootstrapContext> get(String key) {
        return Optional.ofNullable(contextMap.get(key));
    }

}
