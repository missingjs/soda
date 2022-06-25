package soda.web.bootstrap;

import soda.unittest.Utils;
import soda.web.WebUtils;

import java.io.IOException;

public class BootstrapContext {

    private final String md5;

    private ClassLoader classLoader;

    public BootstrapContext(byte[] artifact) {
        md5 = WebUtils.md5Hex(artifact);
        var parentLoader = BootstrapContext.class.getClassLoader();
        Utils.wrapEx(() -> {
            classLoader = new JarFileBytesClassLoader(artifact, parentLoader);
        });
    }

    public String getMd5() {
        return md5;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

}
