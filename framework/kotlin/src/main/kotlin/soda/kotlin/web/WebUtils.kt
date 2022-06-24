package soda.kotlin.web

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

internal object WebUtils {

    fun findOne(text: String, pattern: String, group: Int): String? {
        return pattern.toRegex().find(text)?.groups?.get(group)?.value
    }

    fun findOne(text: String, pattern: String): String? {
        return findOne(text, pattern, 1)
    }

    fun toByteArray(input: InputStream): ByteArray {
        val buf = ByteArray(1024){0}
        val outs = ByteArrayOutputStream()
        var size = 0
        while (true) {
            size = input.read(buf)
            if (size == -1) {
                break
            }
            outs.write(buf, 0, size)
        }
        return outs.toByteArray()
    }

    fun toString(ex: Throwable): String {
        val out = ByteArrayOutputStream()
        val pw = PrintStream(out)
        return pw.use {
            ex.printStackTrace(it)
            it.flush()
            out.toString(StandardCharsets.UTF_8)
        }
    }

}
