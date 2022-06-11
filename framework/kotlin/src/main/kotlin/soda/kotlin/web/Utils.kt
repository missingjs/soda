package soda.kotlin.web

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

internal object Utils {

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