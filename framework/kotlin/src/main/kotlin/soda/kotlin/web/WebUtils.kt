package soda.kotlin.web

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

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

    fun md5Hex(data: ByteArray): String {
        val md5 = MessageDigest.getInstance("MD5")
        md5.update(data)
        return hex(md5.digest())
    }

    fun hex(data: ByteArray): String {
        val code = "0123456789abcdef"
        val buf = CharArray(data.size * 2)
        for (i in data.indices) {
            val high = (data[i].toInt() shr 4) and 0x0f
            val low = data[i].toInt() and 0x0f
            buf[i * 2] = code[high]
            buf[i * 2 + 1] = code[low]
        }
        return String(buf)
    }

}
