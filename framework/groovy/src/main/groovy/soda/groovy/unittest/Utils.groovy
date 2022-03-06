package soda.groovy.unittest

import java.lang.reflect.Method

class Utils {

    static findMethod(Class<?> jobClass, String methodName) {
        def ms = jobClass.methods.grep{ it.name == methodName }
        if (ms) {
            return ms[0]
        }
        throw new NoSuchMethodException(methodName)
    }
}
