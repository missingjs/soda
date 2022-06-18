package soda.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarFileBytesClassLoader extends ClassLoader {

    private Map<String, byte[]> classMap = new HashMap<>();

    public JarFileBytesClassLoader(byte[] bytes, ClassLoader parent) throws IOException {
        super(parent);
        init(bytes);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (classMap.containsKey(name)) {
            var bytes = classMap.get(name);
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }

    private void init(byte[] bytes) throws IOException {
        var buffer = new byte[1024];
        try (ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
             JarInputStream jin = new JarInputStream(bin)
        ) {
            JarEntry entry = null;
            while ((entry = jin.getNextJarEntry()) != null) {
                var name = entry.getRealName();
                if (!name.endsWith(".class")) {
                    continue;
                }
                var className = name.replaceAll("[.]class", "").replaceAll("/", ".");
                var bout = new ByteArrayOutputStream();
                var readSize = 0;
                while ((readSize = jin.read(buffer, 0, buffer.length)) != -1) {
                    bout.write(buffer, 0, readSize);
                }
                var classBytes = bout.toByteArray();
                classMap.put(className, classBytes);
            }
        }
    }

}

