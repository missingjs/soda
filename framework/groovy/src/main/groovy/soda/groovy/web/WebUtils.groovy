package soda.groovy.web

import java.nio.charset.StandardCharsets

class WebUtils {

    static String toString(Throwable ex) {
        def out = new ByteArrayOutputStream()
        def pw = new PrintStream(out)
        ex.printStackTrace(pw)
        pw.flush()
        out.toString(StandardCharsets.UTF_8)
    }

}
