package soda.kotlin.web.bootstrap

import java.util.*

class ContextManager {

    private val contextMap = Collections.synchronizedMap(WeakHashMap<String, BootstrapContext>())

    fun register(key: String, artifact: ByteArray): BootstrapContext {
        val context = BootstrapContext(key, artifact)
        contextMap[key] = context
        return context
    }

    fun get(key: String): BootstrapContext? {
        return contextMap[key]
    }

}