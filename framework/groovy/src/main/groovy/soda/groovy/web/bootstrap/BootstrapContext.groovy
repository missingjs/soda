package soda.groovy.web.bootstrap

import soda.groovy.web.WebUtils

import java.nio.charset.StandardCharsets

class BootstrapContext {

    private final String key

    private final String md5

    private ClassLoader classLoader

    BootstrapContext(String key, byte[] artifact) {
        this.key = key
        md5 = WebUtils.md5Hex(artifact)
        def parentLoader = BootstrapContext.class.getClassLoader()
        classLoader = new GroovyClassLoader(parentLoader)
        classLoader.parseClass(new String(artifact, StandardCharsets.UTF_8))
    }

    String getKey() {
        return key
    }

    String getMd5() {
        return md5
    }

    ClassLoader getClassLoader() {
        return classLoader
    }

}
