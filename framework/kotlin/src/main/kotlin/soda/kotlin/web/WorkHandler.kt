package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor

class WorkHandler : BaseHandler() {

    private val maxTimeoutMillis: Long = 9000

    private val timeoutMillis = maxTimeoutMillis

    override fun handleWork(exchange: HttpExchange): String {
        val content = readPostBody(exchange)
        Logger.info("request: $content")

        val req = Json.decodeFromString<WorkRequest>(content)
        val task = fun(): String {
            val classLoader = ClassLoaderCache.getForJar(req.classpath)
                ?: throw RuntimeException("no class loader for ${req.classpath}")
            val className = req.bootClass
            val kClass = classLoader.loadClass(className).kotlin
            val ctor = kClass.primaryConstructor
                ?: throw NoSuchMethodError("no constructor for class $className")
            val obj = ctor.call()
            val invoke = kClass.memberFunctions.find { it.name == "invoke" }
                ?: throw NoSuchMethodError("no method invoke found for class $className")
            return invoke.call(obj, req.testCase) as String
        }

        val job = TimeLimitedWork(task)
        val future = job.start()
        try {
            future.get(timeoutMillis, TimeUnit.MILLISECONDS)
        } catch (ex: TimeoutException) {
            job.kill()
            throw RuntimeException("work execution timeout", ex)
        }

        return ""
    }
}